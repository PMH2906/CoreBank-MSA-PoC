package com.tmax.orchestrator.dto.request;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Builder;

@Builder
public class TransferRequest {

    String depositorName;
    String depositorAccountNum;
    String recipientName;
    String recipientAccountNum;
    Long amount;

    public JsonNode toSagaPayload() {

        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode payload = objectMapper.createObjectNode();

        payload.put("depositor-name", this.depositorName);
        payload.put("depositor-accountNum", this.depositorAccountNum);
        payload.put("recipient-name", this.recipientName);
        payload.put("recipient-accountNum", this.recipientAccountNum);
        payload.put("amount", this.amount.toString());

        return payload;
    }
}
