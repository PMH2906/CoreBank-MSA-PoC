package com.tmax.orchestrator.framework;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "sagastate")
@NoArgsConstructor
@Getter
public class SagaState {

    @Id
    private UUID id;

    @Version
    private int version;

    private String type;

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private ObjectNode payload;

    private String currentStep;

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private ObjectNode stepStatus;

    @Enumerated(EnumType.STRING)
    private SagaStatus sagaStatus;

    public SagaState(String sagaType, ObjectNode payload) {
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
        this.stepStatus.put(nextStep, started.name());
    }
}
