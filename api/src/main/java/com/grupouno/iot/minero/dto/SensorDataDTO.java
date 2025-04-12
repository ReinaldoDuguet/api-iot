package com.grupouno.iot.minero.dto;
import java.time.LocalDateTime;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SensorDataDTO {
    private Long id;
    private String apiKey;
    private Long sensorId;
    private long timestamp;
    private Map<String, Object> data;
    private LocalDateTime createdAt;
}
