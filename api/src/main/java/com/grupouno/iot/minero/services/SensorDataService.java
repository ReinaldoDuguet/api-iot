package com.grupouno.iot.minero.services;

<<<<<<< HEAD:api/src/main/java/com/grupouno/iot/minero/services/SensorDataService.java
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupouno.iot.minero.dao.SensorDao;
import com.grupouno.iot.minero.dao.SensorDataDao;
import com.grupouno.iot.minero.simulator.dto.MeasurementDTO;
import com.grupouno.iot.minero.models.Sensor;
import com.grupouno.iot.minero.models.SensorData;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
=======
import java.util.List;
>>>>>>> alex/company-location-sensors-api:src/main/java/com/grupouno/iot/minero/services/SensorDataService.java
import java.util.Map;

import com.grupouno.iot.minero.dto.MeasurementDTO;
import com.grupouno.iot.minero.dto.SensorDataDTO;

public interface SensorDataService {
    void saveSensorData(String apiKey, List<Map<String, Object>> jsonData);
	void processMeasurement(String api_key, MeasurementDTO measurement);
	List<SensorDataDTO> getSensorData(String companyApiKey, List<Long> sensorIds, Long from, Long to);
}