package com.tmax.orchestrator.event;

import java.util.UUID;
import lombok.AllArgsConstructor;
import org.apache.kafka.common.header.Headers;

@AllArgsConstructor
public class AccountApprovalEvent {

    public UUID sagaId;
    public UUID messageId;
    public AccountApprovalStatus status;
    // public Headers headers;
}
