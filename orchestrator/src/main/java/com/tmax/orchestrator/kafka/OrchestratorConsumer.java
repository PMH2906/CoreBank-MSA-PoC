package com.tmax.orchestrator.kafka;

import com.tmax.orchestrator.event.AccountApprovalAndUpdateAmountEvent;
import com.tmax.orchestrator.event.AccountApprovalAndUpdateAmountPayload;
import com.tmax.orchestrator.event.TransferTransactionInsertEvent;
import com.tmax.orchestrator.event.TransferTransactionInsertPayload;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrchestratorConsumer {

    private final String EVENT_ID = "id";
    private final String EVENT_TYPE = "eventType";
    private final OrchestratorEventHandler eventHandler;

    @KafkaListener(topics = "ORCHESTRATOR_TOPIC", groupId = "orchestator-group")
    public void consume(String message) {
        log.info(message);
    }


    /**
     * @param sagaId : SagaState id를 입력합니다.
     * @param messageId : messageId id를 입력합니다.
     * @param eventType : Event type을 입력합니다.
     * @param payload : payload를 입력합니다.
     */
    @KafkaListener(topics = "transfer.account-approval-update-amount.outbox.events", groupId = "orchestator-group")
    void listen(
        @Header(KafkaHeaders.RECEIVED_KEY) UUID sagaId,
        @Header(EVENT_ID) String messageId,
        @Header(EVENT_TYPE) String eventType,
        @Payload AccountApprovalAndUpdateAmountPayload payload) {

        log.info("Kafka message with key = {}, messageId = {}, eventType = {} payload = {}", sagaId, messageId, eventType, payload);
        AccountApprovalAndUpdateAmountEvent event = new AccountApprovalAndUpdateAmountEvent(sagaId, UUID.fromString(messageId), payload.status);
        eventHandler.onAccountApprovalAndAmountUpdateEvent(event);
    }

    /**
     * @param sagaId : SagaState id를 입력합니다.
     * @param messageId : messageId id를 입력합니다.
     * @param eventType : Event type을 입력합니다.
     * @param payload : payload를 입력합니다.
     */
    @KafkaListener(topics = "transfer.transfer-tx-insert.outbox.events", groupId = "orchestator-group")
    void listen(
        @Header(KafkaHeaders.RECEIVED_KEY) UUID sagaId,
        @Header(EVENT_ID) String messageId,
        @Header(EVENT_TYPE) String eventType,
        @Payload TransferTransactionInsertPayload payload) {

        log.info("Kafka message with key = {}, messageId = {}, eventType = {} payload = {}", sagaId, messageId, eventType, payload);
        TransferTransactionInsertEvent event = new TransferTransactionInsertEvent(sagaId, UUID.fromString(messageId), payload.status);
        eventHandler.onTransferTransactionInsertEvent(event);
    }
}
