package com.grupouno.iot.minero.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupouno.iot.minero.dto.MeasurementDTO;
import com.grupouno.iot.minero.dto.SensorMessageDTO;
import com.grupouno.iot.minero.services.SensorDataService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "iot.mode", havingValue = "kafka")
public class SensorKafkaConsumer {

    private final SensorDataService sensorDataService;
    private final ObjectMapper mapper = new ObjectMapper();

    public SensorKafkaConsumer(SensorDataService sensorDataService) {
        this.sensorDataService = sensorDataService;
    }

    @KafkaListener(topics = "iot-sensor-data", groupId = "iot-minero-group")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            SensorMessageDTO dto = mapper.readValue(record.value(), SensorMessageDTO.class);

            if (dto.getJson_data() != null) {
                for (MeasurementDTO measurement : dto.getJson_data()) {
                    sensorDataService.processMeasurement(dto.getApi_key(), measurement);
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error processing Kafka message:");
            e.printStackTrace();
        }
    }
}
