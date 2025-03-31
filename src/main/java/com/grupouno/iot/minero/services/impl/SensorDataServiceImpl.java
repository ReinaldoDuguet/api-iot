package com.grupouno.iot.minero.services.impl;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.grupouno.iot.minero.dto.SensorDataDTO;
import com.grupouno.iot.minero.mappers.SensorDataMapper;
import com.grupouno.iot.minero.models.Sensor;
import com.grupouno.iot.minero.models.SensorData;
import com.grupouno.iot.minero.repository.SensorDataRepository;
import com.grupouno.iot.minero.repository.SensorRepository;
import com.grupouno.iot.minero.services.SensorDataService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SensorDataServiceImpl implements SensorDataService {

    private final SensorRepository sensorRepository;
    private final SensorDataRepository sensorDataRepository;
    private final SensorDataMapper sensorDataMapper;

    @Override
    public void saveSensorData(String apiKey, List<Map<String, Object>> jsonData) {
        Sensor sensor = sensorRepository.findByApiKey(apiKey)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "API key inválido o no asociado a un sensor"));

        List<SensorData> dataList = jsonData.stream()
                .map(json -> sensorDataMapper.toEntity(sensor, json))
                .toList();

        sensorDataRepository.saveAll(dataList);
    }

    @Override
    public List<SensorDataDTO> getSensorDataBySensorId(Long sensorId) {
        List<SensorData> dataList = sensorDataRepository.findBySensorId(sensorId);
        return dataList.stream().map(sensorDataMapper::toDTO).toList();
    }
}
