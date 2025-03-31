package com.grupouno.iot.minero.repository;

import com.grupouno.iot.minero.models.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SensorRepository extends JpaRepository<Sensor, Long> {
    Optional<Sensor> findByApiKey(String apiKey);
}
