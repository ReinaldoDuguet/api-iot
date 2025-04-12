package com.grupouno.iot.minero.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grupouno.iot.minero.models.Sensor;

public interface SensorRepository extends JpaRepository<Sensor, Long> {
    Optional<Sensor> findByApiKey(String apiKey);
    boolean existsByApiKey(String apiKey);
}
