package com.grupouno.iot.minero.repository;

import com.grupouno.iot.minero.models.SensorData;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorDataRepository extends JpaRepository<SensorData, Long> {
	List<SensorData> findBySensorId(Long sensorId);
	List<SensorData> findBySensorIdInAndTimestampBetween(List<Long> sensorIds, long from, long to);

}
