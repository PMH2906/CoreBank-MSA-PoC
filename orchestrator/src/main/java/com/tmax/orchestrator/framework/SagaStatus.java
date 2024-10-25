package com.tmax.orchestrator.framework;

public enum SagaStatus {
    STARTED, ABORTING, ABORTED, COMPLETED;

    public boolean isCompleted() {
        return this == COMPLETED;
    }

    public boolean isAborted() {
        return this == ABORTED;
    }
}