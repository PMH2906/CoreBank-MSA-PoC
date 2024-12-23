package org.tmax.account.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TransferResultEvent {

    private static final ObjectMapper mapper = new ObjectMapper();

    private UUID sagaId;
    private JsonNode payload;
    private  LocalDateTime timestamp;

    private TransferResultEvent(UUID sagaId, JsonNode payload) {
        this.sagaId = sagaId;
        this.payload = payload;
        this.timestamp = LocalDateTime.now();
    }

    public static TransferResultEvent of(UUID sagaId, TransferStatus status) {
        ObjectNode asJson = mapper.createObjectNode()
            .put("status", status.name());

        return new TransferResultEvent(sagaId, asJson);
    }

    public UUID sagaId() { return sagaId;}

    public String aggregateType() {
        return "account-approval-and-account-amount-update";
    }

    public JsonNode payload() {
        return payload;
    }

    public String type() {
        return "account-approval-and-account-amount-update";
    }

    public LocalDateTime timestamp() {
        return timestamp;
    }
}
