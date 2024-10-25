package com.tmax.orchestrator.framework;

public enum SagaStepStatus {
    STARTED, FAILED, SUCCEEDED, COMPENSATING, COMPENSATED;
}
