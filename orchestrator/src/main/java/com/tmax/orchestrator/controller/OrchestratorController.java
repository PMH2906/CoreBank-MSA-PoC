package com.tmax.orchestrator.controller;

import com.tmax.orchestrator.dto.request.TransferRequest;
import com.tmax.orchestrator.service.TransferService;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@ApplicationScoped
public class OrchestratorController {

    private final TransferService transferService;

    @PostMapping("/deposit")
    ResponseEntity<?> deposit() {
        return ResponseEntity.accepted()
            .build();
    }

    @PostMapping("/withdraw")
    ResponseEntity<?> withdraw() {
        return ResponseEntity.accepted()
            .build();
    }

    /**
     * 계좌 이체를 실행하는 메서드입니다.
     * 1. 계좌 상태 확인 -> account service
     * 1-1. 입금인(고객) 계좌 확인(계좌 존재 여부, 잔액 여부, 이체한도 확인)
     * 1-2. 수신인(고객) 계좌 확인(외부 은행 제외, 계좌 존재 여부)
     *
     * 2. 잔액 update > account service
     * 2-1. 입금인 잔액 update
     * 2-2. 수신인 잔액 update
     *
     * 3. 이체 트랜잭션 insert -> banking service.
     * 3-1. 입급, 지급 트랜잭션 insert
     * 3-2. 이체 트랜잭션 insert
     *
     * @params : TransferRequest : 입금자명, 입금자 계좌번호, 수신자명, 수신자 계좌번호, 금액을 입력합니다.
     * **/
    @PostMapping("/banking/transfer")
    ResponseEntity<?> transfer(@RequestBody TransferRequest request) {

        transferService.transfer(request);
        return ResponseEntity.ok().build();
    }
}
