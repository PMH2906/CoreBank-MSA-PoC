package com.tmax.orchestrator.event;

import java.util.UUID;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AccountApprovalAndUpdateAmountEvent {

    public UUID sagaId;
    public UUID messageId;
    public AccountApprovalAndUpdateAmountStatus status;
}
