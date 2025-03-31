package com.grupouno.iot.minero.controller;

import com.grupouno.iot.minero.tcp.simulator.Esp32SimulatorManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/simulator/esp32")
@RequiredArgsConstructor
public class Esp32SimulatorController {

    private final Esp32SimulatorManager simulatorManager;

    @PostMapping("/start")
    public ResponseEntity<String> start() {
        simulatorManager.start();
        return ResponseEntity.ok("✅ ESP32 Simulator started");
    }

    @PostMapping("/stop")
    public ResponseEntity<String> stop() {
        simulatorManager.stop();
        return ResponseEntity.ok("🛑 ESP32 Simulator stopped");
    }

    @PostMapping("/pause")
    public ResponseEntity<String> pause() {
        simulatorManager.pause();
        return ResponseEntity.ok("⏸️ ESP32 Simulator paused");
    }

    @PostMapping("/resume")
    public ResponseEntity<String> resume() {
        simulatorManager.resume();
        return ResponseEntity.ok("▶️ ESP32 Simulator resumed");
    }

    @GetMapping("/status")
    public ResponseEntity<String> status() {
        boolean running = simulatorManager.isRunning();
        boolean paused = simulatorManager.isPaused();
        String status = running ? (paused ? "⏸️ Paused" : "▶️ Running") : "🛑 Stopped";
        return ResponseEntity.ok("ESP32 Simulator status: " + status);
    }

    @PostMapping("/toggle")
    public ResponseEntity<String> toggle() {
        if (simulatorManager.isRunning()) {
            simulatorManager.stop();
            return ResponseEntity.ok("🛑 Simulator toggled OFF");
        } else {
            simulatorManager.start();
            return ResponseEntity.ok("✅ Simulator toggled ON");
        }
    }
}
