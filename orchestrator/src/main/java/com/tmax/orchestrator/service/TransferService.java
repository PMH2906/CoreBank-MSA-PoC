package com.tmax.orchestrator.service;

import com.tmax.orchestrator.dto.request.TransferRequest;

public interface TransferService {

    void transfer(TransferRequest request);
}
