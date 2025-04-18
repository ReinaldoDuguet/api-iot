package com.grupouno.iot.minero.tcp.simulator;

import com.grupouno.iot.minero.controller.Esp32SimulatorController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "esp32.simulator.tcp.enabled", havingValue = "true", matchIfMissing = false)
public class Esp32SimulatorAutoConfiguration {

    @Bean
    public Esp32SimulatorManager esp32SimulatorManager(Esp32SimulatorConfig config) {
        return new Esp32SimulatorManager(config);
    }

    @Bean
    public Esp32SimulatorController esp32SimulatorController(Esp32SimulatorManager manager) {
        return new Esp32SimulatorController(manager);
    }
}
