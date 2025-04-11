package com.grupouno.iot.minero.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grupouno.iot.minero.dto.SensorDataDTO;
import com.grupouno.iot.minero.dto.SensorDataRequestDTO;
import com.grupouno.iot.minero.services.SensorDataService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/sensor_data")
@RequiredArgsConstructor
public class SensorDataController {

	private final SensorDataService sensorDataService;

    @PostMapping
    public ResponseEntity<String> insertSensorData(@RequestBody SensorDataRequestDTO request) {
        sensorDataService.saveSensorData(request.getApiKey(), request.getJsonData());
        return ResponseEntity.status(HttpStatus.CREATED).body("Datos insertados correctamente");
    }

    @GetMapping
    public ResponseEntity<List<SensorDataDTO>> getSensorData(
        @RequestParam("company_api_key") String companyApiKey,
        @RequestParam("from") Long from,
        @RequestParam("to") Long to,
        @RequestParam("sensor_id") List<Long> sensorIds) {

        List<SensorDataDTO> data = sensorDataService.getSensorData(companyApiKey, sensorIds, from, to);
        return ResponseEntity.ok(data);
    }

}
