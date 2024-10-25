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

    /**
     * Saga 인스턴스를 생성하고 비즈니스전 로직을 실행하는 서비스입니다.
     *
     * @params TransferRequest : 입금자명, 입금자 계좌번호, 수신자명, 수신자 계좌번호, 금액을 입력합니다.
     * **/
    @Override
    public void  transfer(TransferRequest request) {
        sagaManager.begin(TransferSaga.class, request.toSagaPayload());
    }
}
