package com.grupouno.iot.minero.mappers;

import com.grupouno.iot.minero.dto.SensorDTO;
import com.grupouno.iot.minero.models.Location;
import com.grupouno.iot.minero.models.Sensor;
import com.grupouno.iot.minero.models.SensorCategory;
import org.springframework.stereotype.Component;

@Component
public class SensorMapper {

    public SensorDTO toDTO(Sensor sensor) {
        SensorDTO dto = new SensorDTO();
        dto.setId(sensor.getId());
        dto.setName(sensor.getName());
        dto.setApiKey(sensor.getApiKey());
        dto.setMetadata(sensor.getMetadata());
        dto.setActive(sensor.isActive());
        dto.setCreatedAt(sensor.getCreatedAt());
        dto.setUpdatedAt(sensor.getUpdatedAt());

        if (sensor.getLocation() != null) {
            dto.setLocationId(sensor.getLocation().getId());
        }

        if (sensor.getCategory() != null) {
            dto.setCategoryId(sensor.getCategory().getId());
        }

        return dto;
    }

    public Sensor toEntity(SensorDTO dto, Location location, SensorCategory category) {
        Sensor sensor = new Sensor();
        sensor.setName(dto.getName());
        sensor.setMetadata(dto.getMetadata());
        sensor.setActive(dto.isActive());
        sensor.setLocation(location);
        sensor.setCategory(category);
        return sensor;
    }
}

