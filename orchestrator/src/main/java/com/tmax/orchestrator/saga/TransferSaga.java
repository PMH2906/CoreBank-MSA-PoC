package com.tmax.orchestrator.saga;

import static com.tmax.orchestrator.saga.TransferSaga.ACCOUNT_APPROVAL_AND_AMOUNT_UPDATE;
import static com.tmax.orchestrator.saga.TransferSaga.TRANSFER_TX_INSERT;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tmax.orchestrator.event.AccountApprovalAndUpdateAmountEvent;
import com.tmax.orchestrator.event.TransferTXInsertEvent;
import com.tmax.orchestrator.framework.Saga;
import com.tmax.orchestrator.framework.SagaBase;
import com.tmax.orchestrator.domain.SagaState;
import com.tmax.orchestrator.framework.SagaStepMessage;
import jakarta.persistence.EntityManager;

/**
 * 계좌 이체 Saga 클래스
 * 사용하는 서비스 Micro Service 및 각 service의 비즈니스 실행 로직
 * 1. account service -> ACCOUNT_APPROVAL_AND_AMOUNT_UPDATE(계좌 상태 확인 & 잔액)
 * 1-1. 입금인(고객) 계좌 확인(계좌 존재 여부, 잔액 여부, 이체한도 확인)
 * 1-2. 수신인(고객) 계좌 확인(외부 은행 제외, 계좌 존재 여부)
 * 1-3. 입금인 잔액 update
 * 1-4. 수신인 잔액 update
 *
 * 2. banking service -> TRANSFER_TX_INSERT(이체 트랜잭션 insert)
 * 2-1. 입금, 지급 트랜잭션 insert
 * 2-2. 이체 트랜잭션 insert
 * **/
@Saga(type="transfer", step = {ACCOUNT_APPROVAL_AND_AMOUNT_UPDATE, TRANSFER_TX_INSERT})
public class TransferSaga extends SagaBase {

    static final String ACCOUNT_APPROVAL_AND_AMOUNT_UPDATE = "account-approval-and-account-amount-update";
    static final String TRANSFER_TX_INSERT = "transfer-tx-insert";
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

        if (topic.equals(ACCOUNT_APPROVAL_AND_AMOUNT_UPDATE)) {
            return new SagaStepMessage(ACCOUNT_APPROVAL_AND_AMOUNT_UPDATE, eventType, payload);
        }
        else if (topic.equals(TRANSFER_TX_INSERT)) {
            return new SagaStepMessage(TRANSFER_TX_INSERT, eventType, payload);
        }

        // Todo : Error 처리 추가 예정
        return null;
    }

    public void onAccountApprovalAndAmountUpdate(AccountApprovalAndUpdateAmountEvent event) {
        if (alreadyProcessed(event.eventId)) {
            return;
        }

        onStepEvent(ACCOUNT_APPROVAL_AND_AMOUNT_UPDATE, event.status.toStepStatus());

        processed(event.eventId);
    }

    public void onTransferTransactionInsertEvent(TransferTXInsertEvent event) {

        if (alreadyProcessed(event.eventId)) {
            return;
        }

        onStepEvent(ACCOUNT_APPROVAL_AND_AMOUNT_UPDATE, event.status.toStepStatus());

        processed(event.eventId);
    }
}
