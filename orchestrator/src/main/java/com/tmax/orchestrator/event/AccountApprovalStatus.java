package com.tmax.orchestrator.event;

import com.tmax.orchestrator.framework.SagaStepStatus;

public enum AccountApprovalStatus {

    APPROVED, REJECTED, CANCELLED;

    public SagaStepStatus toStepStatus() {
        return this == CANCELLED ? SagaStepStatus.COMPENSATED : this == REJECTED ? SagaStepStatus.FAILED : SagaStepStatus.SUCCEEDED;
    }

//    public SagaStepStatus toStepStatus() {
//        switch(this) {
//            case CANCELLED:
//                return SagaStepStatus.COMPENSATED;
//            case REJECTED:
//                return SagaStepStatus.FAILED;
//            default:
//                throw new IllegalArgumentException("Unexpected state: " + this);
//        }
//    }
}
