package com.tmax.orchestrator.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmax.orchestrator.event.AccountApprovalAndUpdateAmountEvent;
import com.tmax.orchestrator.event.AccountApprovalAndUpdateAmountPayload;
import com.tmax.orchestrator.event.TransferTXInsertEvent;
import com.tmax.orchestrator.event.TransferTXInsertPayload;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
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
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "ORCHESTRATOR_TOPIC", groupId = "orchestator-group")
    public void consume(String message) {
        log.info(message);
    }


    /**
     * @param sagaId : sagaId를 입력합니다.
     * @param eventId : eventId를 입력합니다.
     * @param eventType : eventType을 입력합니다.
     * @param payload : payload를 입력합니다.
     */
    @KafkaListener(topics = "account-approval-and-account-amount-update.inbox.events")
    void listenAccountApprovalAndAmountUpdateEvent(
        @Header(KafkaHeaders.RECEIVED_KEY) UUID sagaId,
        @Header(EVENT_ID) String eventId,
        @Header(EVENT_TYPE) String eventType,
        @Payload String payload) throws JsonProcessingException {

        log.info("Kafka message with key = {}, messageId = {}, eventType = {} payload = {}", sagaId, eventId, eventType, payload);

        AccountApprovalAndUpdateAmountPayload accountApprovalAndUpdateAmountPayload = objectMapper.readValue(payload, AccountApprovalAndUpdateAmountPayload.class);

        // Base64 디코딩 후 UUID 변환
        String decodedEventId = new String(Base64.getDecoder().decode(eventId));
        UUID eventUUID = UUID.fromString(decodedEventId);

        AccountApprovalAndUpdateAmountEvent event = new AccountApprovalAndUpdateAmountEvent(sagaId, eventUUID, accountApprovalAndUpdateAmountPayload.status);
        eventHandler.onAccountApprovalAndAmountUpdateEvent(event);
    }

    /**
     * @param sagaId : sagaId를 입력합니다.
     * @param eventId : eventId를 입력합니다.
     * @param eventType : eventType을 입력합니다.
     * @param payload : payload를 입력합니다.
     */
    @KafkaListener(topics = "transfer-tx-insert.inbox.events")
    void listenTransferTxInsertEvent(
        @Header(KafkaHeaders.RECEIVED_KEY) UUID sagaId,
        @Header(EVENT_ID) String eventId,
        @Header(EVENT_TYPE) String eventType,
        @Payload String payload) throws JsonProcessingException {

        log.info("Kafka message with key = {}, messageId = {}, eventType = {} payload = {}", sagaId, eventId, eventType, payload);

        TransferTXInsertPayload transferTXInsertPayload = objectMapper.readValue(payload, TransferTXInsertPayload.class);

        // Base64 디코딩 후 UUID 변환
        String decodedEventId = new String(Base64.getDecoder().decode(eventId));
        UUID eventUUID = UUID.fromString(decodedEventId);

        TransferTXInsertEvent event = new TransferTXInsertEvent(sagaId, eventUUID, transferTXInsertPayload.status);
        eventHandler.onTransferTransactionInsertEvent(event);
    }
}
