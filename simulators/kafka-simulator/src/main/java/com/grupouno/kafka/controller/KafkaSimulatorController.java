package com.grupouno.kafka.controller;

import com.grupouno.kafka.KafkaSimulatorConfig;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/simulator/kafka")
public class KafkaSimulatorController {

    private final KafkaSimulatorConfig config;

    public KafkaSimulatorController(KafkaSimulatorConfig config) {
        this.config = config;
    }

    @PostMapping("/start")
    public String start() {
        config.setEnabled(true);
        return "✅ Kafka simulator started";
    }

    @PostMapping("/stop")
    public String stop() {
        config.setEnabled(false);
        return "⛔ Kafka simulator stopped";
    }

    @GetMapping("/status")
    public String status() {
        return config.isEnabled() ? "✅ Simulator is running" : "⛔ Simulator is stopped";
    }
}