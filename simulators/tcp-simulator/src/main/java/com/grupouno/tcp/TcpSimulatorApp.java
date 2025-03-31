package com.grupouno.tcp;

public class TcpSimulatorApp {
    public static void main(String[] args) {
        String host = System.getenv().getOrDefault("ESP32_SIMULATOR_HOST", "localhost");
        int port = Integer.parseInt(System.getenv().getOrDefault("ESP32_SIMULATOR_PORT", "6000"));
        String apiKey = System.getenv().getOrDefault("ESP32_SIMULATOR_API_KEY", "sensor-esp32-tcp-key-519494H1494948T");
        int interval = Integer.parseInt(System.getenv().getOrDefault("ESP32_SIMULATOR_INTERVAL", "2500"));

        Esp32Simulator simulator = new Esp32Simulator(host, port, apiKey, interval);
        new Thread(simulator).start();
    }
}
