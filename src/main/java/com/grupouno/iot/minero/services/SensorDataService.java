package com.grupouno.iot.minero.services;

import java.util.List;
import java.util.Map;

import com.grupouno.iot.minero.dto.SensorDataDTO;

public interface SensorDataService {
    void saveSensorData(String apiKey, List<Map<String, Object>> jsonData);
    List<SensorDataDTO> getSensorDataBySensorId(Long sensorId);
}