package com.grupouno.tcp.simulator;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.OutputStream;
import java.net.Socket;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

// Simulates an ESP32 device sending TCP data to a Java server
// Uses a configurable interval, host, port and API key
// Builds dynamic JSON messages with randomized temp/humidity values
// Can be paused, resumed or stopped using control methods
// Implements Runnable to be executed in a separate thread

public class Esp32Simulator implements Runnable {

    private final String host;
    private final int port;
    private final String apiKey;
    private final int intervalMillis;
    private volatile boolean running = true;
    private volatile boolean paused = false;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Random random = new Random();

    public Esp32Simulator(String host, int port, String apiKey, int intervalMillis) {
        this.host = host;
        this.port = port;
        this.apiKey = apiKey;
        this.intervalMillis = intervalMillis;
    }

    @Override
    public void run() {
        System.out.println("📡 ESP32 Simulator started");

        try (Socket socket = new Socket(host, port);
             OutputStream out = socket.getOutputStream()) {

            while (running) {
                if (paused) {
                    Thread.sleep(500);
                    continue;
                }

                Map<String, Object> jsonMessage = new HashMap<>();
                jsonMessage.put("api_key", apiKey);

                Map<String, Object> dataEntry = new HashMap<>();
                dataEntry.put("datetime", Instant.now().getEpochSecond());
                dataEntry.put("temp", 15 + random.nextDouble() * 10); // 15-25
                dataEntry.put("humidity", 0.4 + random.nextDouble() * 0.2); // 0.4 - 0.6

                jsonMessage.put("json_data", new Map[]{dataEntry});

                String json = objectMapper.writeValueAsString(jsonMessage);
                out.write((json + "\n").getBytes());
                out.flush();

                System.out.println("📤 Sent: " + json);

                Thread.sleep(intervalMillis);
            }

        } catch (Exception e) {
            System.err.println("❌ Simulator error: " + e.getMessage());
        }

        System.out.println("🛑 ESP32 Simulator stopped");
    }

    // Controls
    public void stop() {
        this.running = false;
    }

    public void pause() {
        this.paused = true;
    }

    public void resume() {
        this.paused = false;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isPaused() {
        return paused;
    }
}
