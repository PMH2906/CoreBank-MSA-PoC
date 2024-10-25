package com.tmax.orchestrator.event;

import java.util.UUID;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TransferTransactionInsertEvent {

    public UUID sagaId;
    public UUID messageId;
    public TransferTransactionInsertStatus status;
}
