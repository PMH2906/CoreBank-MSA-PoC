package com.tmax.orchestrator.event;

import java.util.UUID;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TransferTXInsertEvent {

    public UUID sagaId;
    public UUID eventId;
    public TransferTXInsertStatus status;
}
