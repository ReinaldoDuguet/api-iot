package com.grupouno.tcp.controller;

import com.grupouno.tcp.simulator.Esp32SimulatorManager;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/simulator/tcp")
@RequiredArgsConstructor
public class Esp32SimulatorController {

    private final Esp32SimulatorManager manager;

    @PostMapping("/start")
    public String start() {
        manager.start();
        return "✅ ESP32 TCP Simulator started";
    }

    @PostMapping("/stop")
    public String stop() {
        manager.stop();
        return "🛑 ESP32 TCP Simulator stopped";
    }

    @PostMapping("/pause")
    public String pause() {
        manager.pause();
        return "⏸️ ESP32 TCP Simulator paused";
    }

    @PostMapping("/resume")
    public String resume() {
        manager.resume();
        return "▶️ ESP32 TCP Simulator resumed";
    }

    @GetMapping("/status")
    public String status() {
        return "🚦 Status: running=" + manager.isRunning() + ", paused=" + manager.isPaused();
    }
}
