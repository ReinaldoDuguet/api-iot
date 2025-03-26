package com.grupouno.iot.minero.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupouno.iot.minero.dao.SensorDao;
import com.grupouno.iot.minero.dao.SensorDataDao;
import com.grupouno.iot.minero.dto.MeasurementDTO;
import com.grupouno.iot.minero.models.Sensor;
import com.grupouno.iot.minero.models.SensorData;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class SensorDataService {

    private final SensorDao sensorDao;
    private final SensorDataDao sensorDataDao;
    private final ObjectMapper mapper;

    public SensorDataService(SensorDao sensorDao, SensorDataDao sensorDataDao) {
        this.sensorDao = sensorDao;
        this.sensorDataDao = sensorDataDao;
        this.mapper = new ObjectMapper();
    }

    public void processMeasurement(String apiKey, MeasurementDTO dto) {
        Optional<Sensor> sensorOpt = sensorDao.findByApiKey(apiKey);

        if (sensorOpt.isEmpty()) {
            System.err.println("⚠️ Sensor with api_key [" + apiKey + "] not found or not registered.");
            return;
        }

        Sensor sensor = sensorOpt.get();

        try {
            Map<String, Object> dataMap = Map.of(
                    "temp", dto.getTemp(),
                    "humidity", dto.getHumidity()
            );

            SensorData data = new SensorData();
            data.setSensor(sensor);
            data.setTimestamp(dto.getDatetime());
            data.setData(dataMap);
            data.setCreatedAt(LocalDateTime.now());

            sensorDataDao.save(data);

            System.out.println("✅ Saved SensorData for sensor [" + sensor.getName() + "]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
