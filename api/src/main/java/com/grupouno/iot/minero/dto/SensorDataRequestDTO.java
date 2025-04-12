package com.grupouno.iot.minero.dto;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SensorDataRequestDTO {
    private String apiKey;
    private List<Map<String, Object>> jsonData;
}