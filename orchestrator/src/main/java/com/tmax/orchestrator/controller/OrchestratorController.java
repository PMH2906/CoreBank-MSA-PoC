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
     * @params : TransferRequest : 입금자명, 입금자 계좌번호, 수신자명, 수신자 계좌번호, 금액을 입력합니다.
     * **/
    @PostMapping("/banking/transfer")
    ResponseEntity<?> transfer(@RequestBody TransferRequest request) {

        transferService.transfer(request);
        return ResponseEntity.ok().build();
    }
}
