package com.grupouno.iot.minero.dto;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SensorDTO {
    private Long id;
    private String name;
    private String apiKey;
    private Map<String, Object> metadata;
    private boolean isActive;
    private Long locationId;
    private Long categoryId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
