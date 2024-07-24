package com.tmax.orchestrator.framework;

import com.fasterxml.jackson.databind.JsonNode;
import com.tmax.orchestrator.domain.Outbox;
import com.tmax.orchestrator.domain.SagaState;
import jakarta.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

public abstract class SagaBase {

    private final SagaState state;
    private final EntityManager entityManager;

    protected SagaBase(EntityManager entityManager, SagaState state) {
        this.entityManager = entityManager;
        this.state = state;
    }

    protected abstract SagaStepMessage getStepMessage(String topic);

    protected abstract SagaStepMessage getCompensatingStepMessage(String id);

    public final List<String> getStepTopics() {
        return Arrays.asList(getClass().getAnnotation(Saga.class).stepTopics());
    }

    public final JsonNode getPayload() {
        return state.getPayload();
    }

    protected String getCurrentStep() {
        return state.getCurrentStep();
    }

    public void advance() {

        // 1. 다음 step 토픽 찾기
        String nextStepTopic = getNextStepTopic();

        if (nextStepTopic == null) {
            state.updateCurrentStep(null);
            return;
        }

        // 2. 해당 step 메세지 발행
       SagaStepMessage stepMessage = getStepMessage(nextStepTopic);

        // 이벤트 발생
        // event.fire(new SagaEvent(state.getId(), stepEvent.type, stepEvent.eventType, stepEvent.payload));

        // 3. 이벤트 발생 대신 outbox 테이블 저장
        Outbox outbox = new Outbox(state.getId(), stepMessage.type, stepMessage.eventType, stepMessage.payload);
        entityManager.persist(outbox);

        // 4. SagaState Update
        state.updateStepStatus(nextStepTopic, SagaStepStatus.STARTED);
        state.updateCurrentStep(nextStepTopic);
    }

    public void goBack() {
        String previousStepTopic = getPreviousStep();

        // 1. 이전 step 토픽 찾기
        if (previousStepTopic == null) {
            state.updateCurrentStep(null);
            return;
        }

        SagaStepMessage stepMessage = getCompensatingStepMessage(previousStepTopic);

        // 이벤트 발생
        // event.fire(new SagaEvent(getId(), stepEvent.type, stepEvent.eventType, stepEvent.payload));

        // 3. 이벤트 발생 대신 outbox 테이블 저장
        Outbox outbox = new Outbox(state.getId(), stepMessage.type, stepMessage.eventType, stepMessage.payload);
        entityManager.persist(outbox);

        // 4. SagaState Update
        state.updateStepStatus(previousStepTopic, SagaStepStatus.COMPENSATING);
        state.updateCurrentStep(previousStepTopic);
    }

    private String getNextStepTopic() {
        if (getCurrentStep() == null) {

            // Saga 클래스에 구현된 @Saga의 tstepIds를 List<String>로 반환
            return getStepTopics().get(0);
        }

        int idx = getStepTopics().indexOf(getCurrentStep());

        // 다음 단계가 없을 경우
        if (idx == getStepTopics().size() - 1) {
            return null;
        }

        // 다음 단계가 있을 경우
        return getStepTopics().get(idx + 1);
    }

    private String getPreviousStep() {
        int idx = getStepTopics().indexOf(getCurrentStep());

        if (idx == 0) {
            return null;
        }

        return getStepTopics().get(idx - 1);
    }
}
