package com.grupouno.iot.minero.controller;

import com.grupouno.iot.minero.dto.SensorDTO;
import com.grupouno.iot.minero.services.SensorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sensors")
public class SensorController {

	private final SensorService sensorService;

	public SensorController(SensorService sensorService) {
		this.sensorService = sensorService;
	}

	@GetMapping
	public ResponseEntity<List<SensorDTO>> getAll() {
		return ResponseEntity.ok(sensorService.getAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<SensorDTO> getById(@PathVariable Long id) {
		return ResponseEntity.ok(sensorService.getById(id));
	}

	@PostMapping
	public ResponseEntity<SensorDTO> create(@RequestBody SensorDTO dto) {
		dto.setApiKey(null);
		SensorDTO created = sensorService.create(dto);
		return new ResponseEntity<>(created, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<SensorDTO> update(@PathVariable Long id, @RequestBody SensorDTO dto) {
		SensorDTO updated = sensorService.update(id, dto);
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		sensorService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
