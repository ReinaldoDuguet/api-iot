package com.grupouno.iot.minero.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grupouno.iot.minero.dto.SensorDataDTO;
import com.grupouno.iot.minero.dto.SensorDataRequestDTO;
import com.grupouno.iot.minero.services.SensorDataService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/sensor-data")
@RequiredArgsConstructor
public class SensorDataController {

	private final SensorDataService sensorDataService;

	@PostMapping
	public ResponseEntity<String> insertSensorData(@RequestBody SensorDataRequestDTO request) {
		sensorDataService.saveSensorData(request.getApiKey(), request.getJsonData());
		return ResponseEntity.status(HttpStatus.CREATED).body("Datos insertados correctamente");
	}

	@GetMapping("/{sensorId}")
	public ResponseEntity<List<SensorDataDTO>> getSensorDataBySensor(@PathVariable Long sensorId) {
		List<SensorDataDTO> data = sensorDataService.getSensorDataBySensorId(sensorId);
		return ResponseEntity.ok(data);
	}
}
