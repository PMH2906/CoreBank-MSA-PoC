package com.tmax.orchestrator.framework;

import com.fasterxml.jackson.databind.JsonNode;
import com.tmax.orchestrator.saga.SagaEvent;
import io.debezium.outbox.quarkus.ExportedEvent;
import jakarta.enterprise.event.Event;
import jakarta.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;

public abstract class SagaBase {

    private final Event<ExportedEvent<?,?>> event;
    private final SagaState state;

    protected SagaBase(Event<ExportedEvent<?,?>> event, SagaState state) {
        this.event = event;
        this.state = state;
    }

    protected abstract SagaStepMessage getStepMessage(String topic);

    public final List<String> getStepTopics() {
        return Arrays.asList(getClass().getAnnotation(Saga.class).stepTopics());
    }

    public final JsonNode getPayload() {
        return state.getPayload();
    }

    public void advance() {
        String nextStep = getNextStep();

        if (nextStep == null) {
            state.updateCurrentStep(null);
            return;
        }

       SagaStepMessage stepEvent = getStepMessage(nextStep);

        // 이벤트를 왜 보내주는지 모르겠음..
        event.fire(new SagaEvent(state.getId(), stepEvent.type, stepEvent.eventType, stepEvent.payload));

        state.updateStepStatus(nextStep, SagaStepStatus.STARTED);
        state.updateCurrentStep(nextStep);
    }

    private String getNextStep() {
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

    protected String getCurrentStep() {
        return state.getCurrentStep();
    }
}
