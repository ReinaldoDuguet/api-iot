package com.grupouno.iot.minero.simulator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupouno.iot.minero.simulator.dto.MeasurementDTO;
import com.grupouno.iot.minero.simulator.dto.SensorMessageDTO;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.concurrent.*;

@Component
@ConditionalOnProperty(name = "simulator.kafka.enabled", havingValue = "true")
public class KafkaSimulatorProducer {

    private static final Logger log = LoggerFactory.getLogger(KafkaSimulatorProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaSimulatorConfig config;
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${kafka.simulator.api-key}")
    private String apiKey;

    @Value("${kafka.topic.iot-sensor-data}")
    private String topic;

    @Value("${kafka.simulator.delay.ms}")
    private long delayMs;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> taskHandle;
    private int t = 0;

    public KafkaSimulatorProducer(KafkaTemplate<String, String> kafkaTemplate, KafkaSimulatorConfig config) {
        this.kafkaTemplate = kafkaTemplate;
        this.config = config;
    }

    @PostConstruct
    public void init() {
        taskHandle = scheduler.scheduleAtFixedRate(this::sendIfEnabled, 0, delayMs, TimeUnit.MILLISECONDS);
    }

    private void sendIfEnabled() {
        if (!config.isEnabled()) {
            return;
        }

        try {
            double temp = 20 + 5 * Math.sin(t / 10.0);
            double humidity = 50 + 10 * Math.cos(t / 15.0);
            t++;

            MeasurementDTO measurement = new MeasurementDTO();
            measurement.setTemp(temp);
            measurement.setHumidity(humidity);
            long epochMillis = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            measurement.setDatetime(epochMillis);

            SensorMessageDTO msg = new SensorMessageDTO();
            msg.setApi_key(apiKey);
            msg.setJson_data(Collections.singletonList(measurement));

            String json = mapper.writeValueAsString(msg);
            kafkaTemplate.send(topic, json);
            log.info("🚀 Mensaje simulado enviado a Kafka: {}", json);
        } catch (Exception e) {
            log.error("❌ Error simulando mensaje Kafka", e);
        }
    }

    @PreDestroy
    public void shutdown() {
        if (taskHandle != null) taskHandle.cancel(true);
        scheduler.shutdownNow();
    }
}
