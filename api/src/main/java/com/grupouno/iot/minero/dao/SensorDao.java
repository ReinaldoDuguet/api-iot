package com.grupouno.iot.minero.dao;

import com.grupouno.iot.minero.models.Sensor;
import com.grupouno.iot.minero.repository.SensorRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SensorDao {

    private final SensorRepository sensorRepository;

    public SensorDao(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public Optional<Sensor> findByApiKey(String apiKey) {
        return sensorRepository.findByApiKey(apiKey);
    }
}
