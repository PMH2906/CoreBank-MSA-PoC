package com.tmax.orchestrator.service;

import com.tmax.orchestrator.dto.request.TransferRequest;
import com.tmax.orchestrator.framework.SagaManager;
import com.tmax.orchestrator.saga.TransferSaga;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService{

    private final SagaManager sagaManager;

    @Override
    public void transfer(TransferRequest request) {
        sagaManager.begin(TransferSaga.class, request.toSagaPayload());
    }
}
