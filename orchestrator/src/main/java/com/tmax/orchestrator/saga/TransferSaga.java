package com.tmax.orchestrator.saga;

import static com.tmax.orchestrator.saga.TransferSaga.ACCOUNT_AMOUNT_UPDATE;
import static com.tmax.orchestrator.saga.TransferSaga.ACCOUNT_APPROVAL;
import static com.tmax.orchestrator.saga.TransferSaga.TRANSFER_TX_INSERT;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tmax.orchestrator.event.AccountApprovalEvent;
import com.tmax.orchestrator.framework.Saga;
import com.tmax.orchestrator.framework.SagaBase;
import com.tmax.orchestrator.domain.SagaState;
import com.tmax.orchestrator.framework.SagaStatus;
import com.tmax.orchestrator.framework.SagaStepMessage;
import jakarta.persistence.EntityManager;

@Saga(type="transfer", step = {ACCOUNT_APPROVAL, TRANSFER_TX_INSERT, ACCOUNT_AMOUNT_UPDATE})
public class TransferSaga extends SagaBase {

    static final String ACCOUNT_APPROVAL = "account-approval";
    static final String TRANSFER_TX_INSERT = "transfer-tx-insert";
    static final String ACCOUNT_AMOUNT_UPDATE = "account-amount-update";
    static final String REQUEST = "REQUEST";
    static final String CANCEL = "CANCEL";

    public TransferSaga(EntityManager entityManager, SagaState state) {
        super(entityManager, state);
    }

    @Override
    public SagaStepMessage getStepMessage(String topic) {

        return getStepMessage(topic, REQUEST, getPayload());
    }

    @Override
    protected SagaStepMessage getCompensatingStepMessage(String topic) {

        ObjectNode payload = getPayload().deepCopy();
        payload.put("type", CANCEL);

        return getStepMessage(topic, CANCEL, payload);
    }

    private SagaStepMessage getStepMessage(String topic, String eventType, JsonNode payload) {

        if (topic.equals(ACCOUNT_APPROVAL)) {
            return new SagaStepMessage(ACCOUNT_APPROVAL, eventType, payload);
        }
        else if (topic.equals(TRANSFER_TX_INSERT)) {
            return new SagaStepMessage(TRANSFER_TX_INSERT, eventType, payload);
        }
        else if (topic.equals(ACCOUNT_AMOUNT_UPDATE)){
            return new SagaStepMessage(ACCOUNT_AMOUNT_UPDATE, eventType, payload);
        }

        // Todo : Error 처리 추가 예정
        return null;
    }

    public void onAccountApproval(AccountApprovalEvent event) {
        if (alreadyProcessed(event.messageId)) {
            return;
        }

        onStepEvent(ACCOUNT_APPROVAL, event.status.toStepStatus());
        updateOrderStatus();

        processed(event.messageId);
    }

    private void updateOrderStatus() {
        if (getStatus() == SagaStatus.COMPLETED) {
            PurchaseOrder order = PurchaseOrder.findById(getOrderId());
            order.status = PurchaseOrderStatus.PROCESSING;
        }
        else if (getStatus() == SagaStatus.ABORTED) {
            PurchaseOrder order = PurchaseOrder.findById(getOrderId());
            order.status = PurchaseOrderStatus.CANCELLED;
        }
    }
}
