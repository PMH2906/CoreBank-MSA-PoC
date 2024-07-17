package com.tmax.orchestrator.dto.request;

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

    public ObjectNode toSagaPayload() {

        return new ObjectMapper().createObjectNode()
            .put("depositor-name", this.depositorName)
            .put("depositor-accountNum", this.depositorAccountNum)
            .put("recipient-name", this.recipientName)
            .put("recipient-accountNum", this.recipientAccountNum)
            .put("amount", this.amount.toString());
    }
}
