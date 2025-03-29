package com.grupouno.iot.minero.config;

import org.springframework.stereotype.Component;

/**
 * Simple config class to hold a flag that can enable or disable the simulator in runtime.
 */
@Component
public class ProducerConfig {
    private volatile boolean enabled = true; // Ensures visibility across threads

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}