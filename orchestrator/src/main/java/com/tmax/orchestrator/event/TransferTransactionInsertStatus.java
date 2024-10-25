package com.tmax.orchestrator.event;

import com.tmax.orchestrator.framework.SagaStepStatus;

public enum TransferTransactionInsertStatus {

    APPROVED, REJECTED, CANCELLED;

    public SagaStepStatus toStepStatus() {
        return this == CANCELLED ? SagaStepStatus.COMPENSATED : this == REJECTED ? SagaStepStatus.FAILED : SagaStepStatus.SUCCEEDED;
    }
}
