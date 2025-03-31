package com.grupouno.iot.minero.dao;

import com.grupouno.iot.minero.models.SensorData;
import com.grupouno.iot.minero.repository.SensorDataRepository;
import org.springframework.stereotype.Repository;

@Repository
public class SensorDataDao {

    private final SensorDataRepository repository;

    public SensorDataDao(SensorDataRepository repository) {
        this.repository = repository;
    }

    public void save(SensorData data) {
        repository.save(data);
    }
}
