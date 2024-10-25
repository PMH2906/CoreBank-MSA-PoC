package com.tmax.orchestrator.kafka;

import com.tmax.orchestrator.event.AccountApprovalAndUpdateAmountEvent;
import com.tmax.orchestrator.event.TransferTransactionInsertEvent;
import com.tmax.orchestrator.framework.SagaManager;
import com.tmax.orchestrator.saga.TransferSaga;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrchestratorEventHandler {

    private final SagaManager sagaManager;

    @Transactional
    public void onAccountApprovalAndAmountUpdateEvent(AccountApprovalAndUpdateAmountEvent event) {
        TransferSaga saga = sagaManager.find(TransferSaga.class, event.sagaId);

        if (saga == null) {
            return;
        }

        saga.onAccountApprovalAndAmountUpdate(event);
    }

    public void onTransferTransactionInsertEvent(TransferTransactionInsertEvent event) {

        TransferSaga saga = sagaManager.find(TransferSaga.class, event.sagaId);

        if(saga == null) {
            return;
        }

        saga.onTransferTransactionInsertEvent(event);
    }
}
