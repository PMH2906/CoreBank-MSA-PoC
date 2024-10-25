package com.tmax.orchestrator.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tmax.orchestrator.framework.SagaStatus;
import com.tmax.orchestrator.framework.SagaStepStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "sagastate")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SagaState {

    @Id
    private UUID id;

    @Version
    private int version;

    private String type;

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode payload;

    private String currentStep;

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode stepStatus;

    @Enumerated(EnumType.STRING)
    private SagaStatus sagaStatus;

    public SagaState(String sagaType, JsonNode payload) {
        this.id = UUID.randomUUID();
        this.type = sagaType;
        this.payload = payload;
        this.sagaStatus = SagaStatus.STARTED;
        this.stepStatus = JsonNodeFactory.instance.objectNode();
    }

    public void updateCurrentStep(String currentStep) {
        this.currentStep = currentStep;
    }

    public void updateStepStatus(String nextStep, SagaStepStatus started) {
        ObjectNode stepStatus = (ObjectNode) this.stepStatus;
        stepStatus.put(nextStep, started.name());
    }

    public void updateSagaStatus(SagaStatus sagaStatus) {
        this.sagaStatus = sagaStatus;
    }
}
