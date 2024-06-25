package com.tmax.orchestrator.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrchestratorConsumer {

    @KafkaListener(topics = "ORCHESTRATOR_TOPIC", groupId = "orchestator-group")
    public void consume(String message) {
        log.info(message);
    }
}
