package com.grupouno.iot.minero.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LocationDTO {
    private Long id;
    private String name;
    private Map<String, Object> metadata;
    private Long companyId;
    private Long cityId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Long> sensorIds;
    
    public LocationDTO() {}

    public LocationDTO(Long id, String name, Map<String, Object> metadata, Long companyId, Long cityId,
                       LocalDateTime createdAt, LocalDateTime updatedAt, List<Long> sensorIds) {
        this.id = id;
        this.name = name;
        this.metadata = metadata;
        this.companyId = companyId;
        this.cityId = cityId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.sensorIds = sensorIds;
    }
}
