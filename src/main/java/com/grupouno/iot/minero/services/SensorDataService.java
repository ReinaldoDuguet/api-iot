package com.grupouno.iot.minero.services;

import java.util.List;
import java.util.Map;

import com.grupouno.iot.minero.dto.MeasurementDTO;
import com.grupouno.iot.minero.dto.SensorDataDTO;

public interface SensorDataService {
    void saveSensorData(String apiKey, List<Map<String, Object>> jsonData);
	void processMeasurement(String api_key, MeasurementDTO measurement);
	List<SensorDataDTO> getSensorData(String companyApiKey, List<Long> sensorIds, Long from, Long to);
}