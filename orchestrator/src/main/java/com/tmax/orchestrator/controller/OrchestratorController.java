package com.tmax.orchestrator.controller;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import com.tmax.orchestrator.dto.request.TransferRequest;
import com.tmax.orchestrator.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrchestratorController {

    private final TransferService transferService;

    @PostMapping()
    ResponseEntity<?> deposit() {
        return ResponseEntity.accepted()
            .build();
    }

    @PostMapping()
    ResponseEntity<?> withdraw() {
        return ResponseEntity.accepted()
            .build();
    }

    @PostMapping("/banking/transfer")
    ResponseEntity<?> transfer(@RequestBody TransferRequest request) {

        transferService.transfer(request);
//        return ResponseEntity.accepted()
//            .location(fromCurrentRequest().path("/{id}").build(reservation.id().toString()))
//            .header(HttpHeaders.RETRY_AFTER, "0.5") // seconds
//            .build();
        return ResponseEntity.accepted()
            .build();
    }
}
