package com.tmax.orchestrator.saga;

import static com.tmax.orchestrator.saga.TransferSaga.DEPOSITOR_ACCOUNT_APPROVAL;
import static com.tmax.orchestrator.saga.TransferSaga.DEPOSITOR_ACCOUNT_UPDATE;
import static com.tmax.orchestrator.saga.TransferSaga.DEPOSIT_WITHDRAWAL_TX_INSERT;
import static com.tmax.orchestrator.saga.TransferSaga.RECIPIENT_ACCOUNT_APPROVAL;
import static com.tmax.orchestrator.saga.TransferSaga.RECIPIENT_ACCOUNT_UPDATE;
import static com.tmax.orchestrator.saga.TransferSaga.TRANSFER_TX_INSERT;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tmax.orchestrator.framework.Saga;
import com.tmax.orchestrator.framework.SagaBase;
import com.tmax.orchestrator.domain.SagaState;
import com.tmax.orchestrator.framework.SagaStepMessage;
import jakarta.persistence.EntityManager;

@Saga(type="transfer", stepTopics = {DEPOSITOR_ACCOUNT_APPROVAL, RECIPIENT_ACCOUNT_APPROVAL, TRANSFER_TX_INSERT, DEPOSIT_WITHDRAWAL_TX_INSERT, DEPOSITOR_ACCOUNT_UPDATE, RECIPIENT_ACCOUNT_UPDATE})
public class TransferSaga extends SagaBase {

    static final String DEPOSITOR_ACCOUNT_APPROVAL = "depositor-account-approval";
    static final String RECIPIENT_ACCOUNT_APPROVAL = "recipient-account-approval";
    static final String TRANSFER_TX_INSERT = "transfer-tx-insert";
    static final String DEPOSIT_WITHDRAWAL_TX_INSERT = "deposit-withdrawal-tx-insert";
    static final String DEPOSITOR_ACCOUNT_UPDATE = "depositor-account-update";
    static final String RECIPIENT_ACCOUNT_UPDATE = "recipient-account-update";
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

        if (topic.equals(RECIPIENT_ACCOUNT_UPDATE)) {
            return new SagaStepMessage(RECIPIENT_ACCOUNT_UPDATE, eventType, payload);
        }
        else if (topic.equals(DEPOSITOR_ACCOUNT_UPDATE)) {
            return new SagaStepMessage(DEPOSITOR_ACCOUNT_UPDATE, eventType, payload);
        }
        else if (topic.equals(DEPOSIT_WITHDRAWAL_TX_INSERT)){
            return new SagaStepMessage(DEPOSIT_WITHDRAWAL_TX_INSERT, eventType, payload);
        }
        else if (topic.equals(TRANSFER_TX_INSERT)){
            return new SagaStepMessage(TRANSFER_TX_INSERT, eventType, payload);
        }
        else if (topic.equals(RECIPIENT_ACCOUNT_APPROVAL)){
            return new SagaStepMessage(RECIPIENT_ACCOUNT_APPROVAL, eventType, payload);
        }
        else if (topic.equals(DEPOSITOR_ACCOUNT_APPROVAL)){
            return new SagaStepMessage(DEPOSITOR_ACCOUNT_APPROVAL, eventType, payload);
        }

        // Todo : Error 처리 추가 예정
        return null;
    }
}
