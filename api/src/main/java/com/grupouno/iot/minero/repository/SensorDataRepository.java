package com.grupouno.iot.minero.repository;

import com.grupouno.iot.minero.models.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorDataRepository extends JpaRepository<SensorData, Long> {
}
