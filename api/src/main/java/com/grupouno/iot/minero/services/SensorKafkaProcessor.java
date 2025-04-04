package com.grupouno.iot.minero.services; // Está en la capa de servicios

import com.fasterxml.jackson.databind.ObjectMapper; // Jackson para convertir JSON a objetos Java
import com.grupouno.iot.minero.simulator.dto.MeasurementDTO; // DTO de medición
import com.grupouno.iot.minero.simulator.dto.SensorMessageDTO; // DTO del mensaje completo de Kafka
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service // Componente de Spring que contiene lógica de negocio
public class SensorKafkaProcessor {

    private static final Logger log = LoggerFactory.getLogger(SensorKafkaProcessor.class); // Logger para logs
    private final ObjectMapper mapper = new ObjectMapper(); // Jackson ObjectMapper
    private final SensorDataService sensorDataService; // Servicio que guarda las mediciones

    public SensorKafkaProcessor(SensorDataService sensorDataService) {
        this.sensorDataService = sensorDataService;
    }

    public void processKafkaMessage(String messageJson) {
        try {
            SensorMessageDTO dto = mapper.readValue(messageJson, SensorMessageDTO.class); // Convierte JSON a DTO

            if (dto.getApi_key() == null || dto.getJson_data() == null) {
                log.warn("❌ Mensaje inválido: api_key o json_data nulo");
                return;
            }

            for (MeasurementDTO measurement : dto.getJson_data()) {
                sensorDataService.processMeasurement(dto.getApi_key(), measurement); // Delegamos a la capa de servicio
            }

            log.info("✅ Procesado mensaje de sensor con API key: {}", dto.getApi_key());

        } catch (Exception e) {
            log.error("❌ Error deserializando mensaje Kafka: {}", messageJson);
            log.debug("Detalles del error:", e); // Stacktrace solo si activas DEBUG
        }
    }
}
