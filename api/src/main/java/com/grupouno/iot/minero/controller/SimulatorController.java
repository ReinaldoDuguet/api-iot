package com.grupouno.iot.minero.controller;

import com.grupouno.iot.minero.simulator.KafkaSimulatorConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller to manage simulator state via HTTP
 */
@RestController
@RequestMapping("/simulator")
@ConditionalOnProperty(name = "simulator.kafka.enabled", havingValue = "true")
public class SimulatorController {

    private final KafkaSimulatorConfig config;

    public SimulatorController(KafkaSimulatorConfig config) {
        this.config = config;
    }

    /**
     * Endpoint to toggle simulator state
     */
    @PostMapping("/toggle")
    public ResponseEntity<String> toggleSimulator() {
        config.setEnabled(!config.isEnabled());
        return ResponseEntity.ok("✅ Simulator is now " + (config.isEnabled() ? "ENABLED" : "DISABLED"));
    }

    /**
     * Endpoint to explicitly pause the simulator
     */
    @PostMapping("/pause")
    public ResponseEntity<String> pauseSimulator() {
        config.setEnabled(false);
        return ResponseEntity.ok("⏸️ Simulator PAUSED");
    }

    /**
     * Endpoint to explicitly resume the simulator
     */
    @PostMapping("/resume")
    public ResponseEntity<String> resumeSimulator() {
        config.setEnabled(true);
        return ResponseEntity.ok("▶️ Simulator RESUMED");
    }

    /**
     * Endpoint to check simulator state
     */
    @GetMapping("/status")
    public ResponseEntity<String> getSimulatorStatus() {
        return ResponseEntity.ok("🛰️ Simulator is currently " + (config.isEnabled() ? "ENABLED" : "DISABLED"));
    }
}
