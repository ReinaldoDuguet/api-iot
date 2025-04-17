package com.grupouno.iot.minero.mappers;

import com.grupouno.iot.minero.dto.LocationDTO;
import com.grupouno.iot.minero.models.City;
import com.grupouno.iot.minero.models.Company;
import com.grupouno.iot.minero.models.Location;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LocationMapper {

    public static LocationDTO toDto(Location location) {
        LocationDTO dto = new LocationDTO();
        dto.setId(location.getId());
        dto.setName(location.getName());
        dto.setMetadata(location.getMetadata());
        dto.setCompanyId(location.getCompany().getId());
        dto.setCityId(location.getCity().getId());
        dto.setCreatedAt(location.getCreatedAt());
        dto.setUpdatedAt(location.getUpdatedAt());

        if (location.getSensors() != null) {
            List<Long> sensorIds = location.getSensors()
                    .stream()
                    .map(sensor -> sensor.getId())
                    .collect(Collectors.toList());
            dto.setSensorIds(sensorIds);
        }

        return dto;
    }

    public static Location toEntity(LocationDTO dto, Company company, City city) {
        Location location = new Location();
        location.setId(dto.getId());
        location.setName(dto.getName());
        location.setMetadata(dto.getMetadata());
        location.setCompany(company);
        location.setCity(city);
        location.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : java.time.LocalDateTime.now());
        location.setUpdatedAt(java.time.LocalDateTime.now());
        return location;
    }
}
