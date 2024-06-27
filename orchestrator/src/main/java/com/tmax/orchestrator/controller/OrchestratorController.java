package com.tmax.orchestrator.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrchestratorController {

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

    @PostMapping()
    ResponseEntity<?> transfer() {
        return ResponseEntity.accepted()
            .build();
    }
}
