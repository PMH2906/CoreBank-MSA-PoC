package org.tmax.account.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.tmax.account.domain.Outbox;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountConsumer {

    private final EntityManager entityManager;
    private final ObjectMapper objectMapper;

    public static final String EVENT_ID = "id";
    public static final String EVENT_TYPE = "eventType";

    /**
     * @param sagaId : sagaId를 입력합니다.
     * @param eventId : eventId를 입력합니다.
     * @param eventType : eventType을 입력합니다.
     * @param payload : payload를 입력합니다.
     */
    @KafkaListener(topics = "account-approval-and-account-amount-update.outbox.events")
    @Transactional
    void listen(
        @Header(KafkaHeaders.RECEIVED_KEY) UUID sagaId,
        @Header(EVENT_ID) String eventId,
        @Header(EVENT_TYPE) String eventType,
        @Payload String payload) throws JsonProcessingException {

        log.info("Kafka message with key = {}, messageId = {}, eventType = {} payload = {}", sagaId, eventId, eventType, payload);

        TransferPayload transferPayload = objectMapper.readValue(payload, TransferPayload.class);

        // Todo : 계좌 서비스  비즈니스 로직 작성
        // 현재는 임시로 모두 허용
        // orchestrator로 보내는 메시지 생성
        TransferResultEvent transferEvent = TransferResultEvent.of(sagaId, transferPayload.transferStatus());
        Outbox outbox = new Outbox(transferEvent.sagaId(), transferEvent.aggregateType(), transferEvent.type(), transferEvent.payload());
        entityManager.persist(outbox);
    }
}
