package com.grupouno.iot.minero.mappers;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.grupouno.iot.minero.dto.SensorDataDTO;
import com.grupouno.iot.minero.models.Sensor;
import com.grupouno.iot.minero.models.SensorData;

@Component
public class SensorDataMapper {

    public SensorData toEntity(Sensor sensor, Map<String, Object> json) {
        SensorData entity = new SensorData();
        entity.setSensor(sensor);

        Object timestampValue = json.get("timestamp");
        if (timestampValue instanceof Number) {
            entity.setTimestamp(((Number) timestampValue).longValue());
        } else {
            throw new IllegalArgumentException("El campo 'timestamp' debe estar presente y ser numérico.");
        }

        entity.setData(json);
        entity.setCreatedAt(LocalDateTime.now());
        return entity;
    }

    public SensorDataDTO toDTO(SensorData data) {
        SensorDataDTO dto = new SensorDataDTO();
        dto.setId(data.getId());
        dto.setSensorId(data.getSensor().getId());
        dto.setTimestamp(data.getTimestamp());
        dto.setData(data.getData());
        dto.setCreatedAt(data.getCreatedAt());
        return dto;
    }
}
