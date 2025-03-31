package com.grupouno.tcp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

// TCP server host to connect the simulated ESP32
// TCP port the simulator sends messages to
// Unique API key to identify the sensor from this simulator
// Interval between messages in milliseconds

@Configuration
@ConfigurationProperties(prefix = "esp32.simulator")
public class Esp32SimulatorConfig {

    private String host;
    private int port;
    private String apiKey;
    private int intervalMillis;

    // Getters and Setters
    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }

    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }

    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }

    public int getIntervalMillis() { return intervalMillis; }
    public void setIntervalMillis(int intervalMillis) { this.intervalMillis = intervalMillis; }
}
