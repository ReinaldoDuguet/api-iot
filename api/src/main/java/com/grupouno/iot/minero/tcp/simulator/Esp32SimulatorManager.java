package com.grupouno.iot.minero.tcp.simulator;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

// Manages lifecycle of the ESP32 TCP simulator: start, stop, pause, resume
// Uses configuration injected via Esp32SimulatorConfig
// Runs simulator in its own thread
// Stops simulator gracefully when app is shutting down

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "esp32.simulator.tcp.enabled", havingValue = "true")
public class Esp32SimulatorManager {

    private final Esp32SimulatorConfig config;
    private Thread simulatorThread;
    private Esp32Simulator simulator;

    public synchronized void start() {
        if (simulatorThread != null && simulatorThread.isAlive()) return;

        simulator = new Esp32Simulator(
                config.getHost(),
                config.getPort(),
                config.getApiKey(),
                config.getIntervalMillis()
        );

        simulatorThread = new Thread(simulator);
        simulatorThread.start();
    }

    public synchronized void stop() {
        if (simulator != null) simulator.stop();
    }

    public synchronized void pause() {
        if (simulator != null) simulator.pause();
    }

    public synchronized void resume() {
        if (simulator != null) simulator.resume();
    }

    public boolean isRunning() {
        return simulator != null && simulator.isRunning();
    }

    public boolean isPaused() {
        return simulator != null && simulator.isPaused();
    }

    @PreDestroy
    public void shutdown() {
        stop();
    }
}
