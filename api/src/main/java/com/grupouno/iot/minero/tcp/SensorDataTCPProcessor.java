package com.grupouno.iot.minero.tcp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupouno.iot.minero.simulator.dto.MeasurementDTO;
import com.grupouno.iot.minero.services.SensorDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SensorDataTCPProcessor {

    private static final Logger log = LoggerFactory.getLogger(SensorDataTCPProcessor.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SensorDataService sensorDataService;

    public SensorDataTCPProcessor(SensorDataService sensorDataService) {
        this.sensorDataService = sensorDataService;
    }

    public void processJson(String jsonMessage) {
        try {
            JsonNode root = objectMapper.readTree(jsonMessage);

            String apiKey = root.path("api_key").asText(null);
            if (apiKey == null) {
                log.warn("⚠️ TCP data missing api_key");
                return;
            }

            JsonNode jsonData = root.path("json_data");
            if (!jsonData.isArray()) {
                log.warn("⚠️ TCP data json_data is not array");
                return;
            }

            for (JsonNode measurementNode : jsonData) {
                long datetime = measurementNode.path("datetime").asLong();
                double temp = measurementNode.path("temp").asDouble();
                double humidity = measurementNode.path("humidity").asDouble();

                MeasurementDTO dto = new MeasurementDTO();
                dto.setDatetime(datetime);
                dto.setTemp(temp);
                dto.setHumidity(humidity);

                sensorDataService.processMeasurement(apiKey, dto);
            }

            log.info("✅ Processed TCP message from API key: {}", apiKey);
        } catch (Exception e) {
            log.error("❌ Failed to process TCP message: " + jsonMessage, e);
        }
    }
}
