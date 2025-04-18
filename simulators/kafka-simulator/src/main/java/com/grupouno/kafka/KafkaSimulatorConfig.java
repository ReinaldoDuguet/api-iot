package com.grupouno.kafka;

import org.springframework.stereotype.Component;

/**
 * This config class allows enabling or disabling the Kafka simulator in real time.
 */
@Component
public class KafkaSimulatorConfig {

    private boolean enabled = false; // Simulator is enabled by default

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
