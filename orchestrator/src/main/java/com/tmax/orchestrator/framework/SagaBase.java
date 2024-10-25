package com.tmax.orchestrator.framework;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tmax.orchestrator.domain.ConsumedMessage;
import com.tmax.orchestrator.domain.Outbox;
import com.tmax.orchestrator.domain.SagaState;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class SagaBase {

    private final SagaState state;
    private final EntityManager entityManager;

    protected SagaBase(EntityManager entityManager, SagaState state) {
        this.entityManager = entityManager;
        this.state = state;
    }

    protected abstract SagaStepMessage getStepMessage(String topic);

    protected abstract SagaStepMessage getCompensatingStepMessage(String id);

    public final List<String> getStep() {
        return Arrays.asList(getClass().getAnnotation(Saga.class).step());
    }

    public final JsonNode getPayload() {
        return state.getPayload();
    }

    protected String getCurrentStep() {
        return state.getCurrentStep();
    }

    /**
     * 이미 처리된 Event 인지 확인하는 메서드입니다.
     *
     * @param eventId : eventId를 입력합니다.
     * **/
    protected boolean alreadyProcessed(UUID eventId) {
        log.debug("Looking for event with id {} in message log", eventId);
        return entityManager.find(ConsumedMessage.class, eventId) != null;
    }

    /**
     * 처리된 Event를 저장하는 메서드입니다.
     *
     * @param eventId : eventId를 입력합니다.
     * **/
    protected void processed(UUID eventId) {
        entityManager.persist(new ConsumedMessage(eventId, Instant.now()));
    }

    /**
     * kafka에서 메세지를 받은 후 다음 sub step의 비즈니스 로직을 처리합니다.
     * SagaStepStatus.SUCCEEDED면 진행, SagaStepStatus.FAILED 혹은 SagaStepStatus.COMPENSATED면 보상 트랜잭션을 진행합니다.
     *
     * @param type: step을 입력합니다.
     * @param  status : 해당 step의 status를 입력합니다.(STARTED, FAILED, SUCCEEDED, COMPENSATING, COMPENSATED)
     * **/
    protected void onStepEvent(String type, SagaStepStatus status) {

        ObjectNode stepStatus = (ObjectNode) state.getStepStatus();
        stepStatus.put(type, status.name());

        if (status == SagaStepStatus.SUCCEEDED) {
            advance();
        }
        else if (status == SagaStepStatus.FAILED || status == SagaStepStatus.COMPENSATED) {
            goBack();
        }

        EnumSet<SagaStepStatus> allStatus = EnumSet.noneOf(SagaStepStatus.class);
        Iterator<String> fieldNames = stepStatus.fieldNames();
        while (fieldNames.hasNext()) {
            allStatus.add(SagaStepStatus.valueOf(stepStatus.get(fieldNames.next()).asText()));
        }

        state.updateSagaStatus(getSagaStatus(allStatus));
    }

    /**
     * 현재 모든 SagaStepStatus를 기반으로 SagaStatus를 업데이트합니다.
     * SagaStepStatus가 모두 SUCCEEDED이면, SagaStatus는 COMPLETED 입니다.
     * SagaStepStatus가 모두 STARTED 혹은 SUCCEEDED이면, SagaStatus는 STARTED입니다.
     * SagaStepStatus가 모두 FAILED 혹은 COMPENSATED이면, SagaStatus는 ABORTED입니다.
     * 그 외의 SagaStatus는 ABORTING입니다.
     *
     * @param stepStates : 현재 모든 SagaStepStatus에서 중복을 제거한 데이터를 입력합니다.
     * **/
    private SagaStatus getSagaStatus(EnumSet<SagaStepStatus> stepStates) {
        if (containsOnly(stepStates, SagaStepStatus.SUCCEEDED)) {
            return SagaStatus.COMPLETED;
        }
        else if (containsOnly(stepStates, SagaStepStatus.STARTED, SagaStepStatus.SUCCEEDED)) {
            return SagaStatus.STARTED;
        }
        else if (containsOnly(stepStates, SagaStepStatus.FAILED, SagaStepStatus.COMPENSATED)) {
            return SagaStatus.ABORTED;
        }
        else {
            return SagaStatus.ABORTING;
        }
    }

    /**
     * 현재 모든 SagaStepStatus를 기반으로 SagaStatus를 업데이트할 수 있도록 SagaStepStatus의 값을 확인합니다.
     * **/
    private boolean containsOnly(Collection<SagaStepStatus> stepStates, SagaStepStatus status) {
        for (SagaStepStatus sagaStepStatus : stepStates) {
            if (sagaStepStatus != status) {
                return false;
            }
        }

        return true;
    }

    /**
     * 현재 모든 SagaStepStatus를 기반으로 SagaStatus를 업데이트할 수 있도록 SagaStepStatus의 값을 확인합니다.
     * **/
    private boolean containsOnly(Collection<SagaStepStatus> stepStates, SagaStepStatus status1, SagaStepStatus status2) {
        for (SagaStepStatus sagaStepStatus : stepStates) {
            if (sagaStepStatus != status1 && sagaStepStatus != status2) {
                return false;
            }
        }

        return true;
    }

    /**
     * Saga 인스턴스를 통해 다음 step을 실행할 수 있도록 Outbox 테이블을 저장합니다.
     * Debezium이 Outbox 데이터의 변경을 캡쳐하여(cdc) kafka의 특정 topic에 메세지를 발생합니다.
     * kafka의 topic은 Outbox의 aggregatetype 컬럼에 따라 결정됩니다.
     * outbox 테이블 저장 후 SagaState를 update합니다.
     * **/
    public void advance() {

        // 1. 다음 step 토픽 찾기
        String nextStep = getNextStep();

        if (nextStep == null) {
            state.updateCurrentStep(null);
            return;
        }

        // 2. 해당 step 메세지 발행
        SagaStepMessage stepMessage = getStepMessage(nextStep);

        // 이벤트 발생
        // event.fire(new SagaEvent(state.getId(), stepEvent.type, stepEvent.eventType, stepEvent.payload));

        // 3. 이벤트 발생 대신 outbox 테이블 저장
        Outbox outbox = new Outbox(state.getId(), stepMessage.type, stepMessage.eventType, stepMessage.payload);
        entityManager.persist(outbox);

        // 4. SagaState Update
        state.updateStepStatus(nextStep, SagaStepStatus.STARTED);
        state.updateCurrentStep(nextStep);
    }

    /**
     * 보상 관련 메서드입니다. 비즈니스 로직 실행 중 하나의 서비스에서 실패하면 그 전 모든 서비스의 로직을 보상 처리합니다.
     * Saga 인스턴스를 통해 이전 step을 실행할 수 있도록 Outbox 테이블을 저장합니다.
     * Debezium이 Outbox 데이터의 변경을 캡쳐하여(cdc) kafka의 특정 topic에 메세지를 발생합니다.
     * kafka의 topic은 Outbox의 aggregatetype 컬럼에 따라 결정됩니다.
     * outbox 테이블 저장 후 SagaState를 update합니다.
     * **/
    public void goBack() {
        String previousStep = getPreviousStep();

        // 1. 이전 step 토픽 찾기
        if (previousStep == null) {
            state.updateCurrentStep(null);
            return;
        }

        SagaStepMessage stepMessage = getCompensatingStepMessage(previousStep);

        // 이벤트 발생
        // event.fire(new SagaEvent(getId(), stepEvent.type, stepEvent.eventType, stepEvent.payload));

        // 3. 이벤트 발생 대신 outbox 테이블 저장
        Outbox outbox = new Outbox(state.getId(), stepMessage.type, stepMessage.eventType, stepMessage.payload);
        entityManager.persist(outbox);

        // 4. SagaState Update
        state.updateStepStatus(previousStep, SagaStepStatus.COMPENSATING);
        state.updateCurrentStep(previousStep);
    }

    /**
     * Saga클래스의 @Saga의 step 속성값을 기반으로 다음 step을 찾습니다.
     * **/
    private String getNextStep() {
        if (getCurrentStep() == null) {

            // Saga 클래스에 구현된 @Saga의 tstepIds를 List<String>로 반환
            return getStep().get(0);
        }

        int idx = getStep().indexOf(getCurrentStep());

        // 다음 단계가 없을 경우
        if (idx == getStep().size() - 1) {
            return null;
        }

        // 다음 단계가 있을 경우
        return getStep().get(idx + 1);
    }

    /**
     * Saga클래스의 @Saga의 step 속성값을 기반으로 이전 step을 찾습니다.
     * **/
    private String getPreviousStep() {
        int idx = getStep().indexOf(getCurrentStep());

        if (idx == 0) {
            return null;
        }

        return getStep().get(idx - 1);
    }
}
