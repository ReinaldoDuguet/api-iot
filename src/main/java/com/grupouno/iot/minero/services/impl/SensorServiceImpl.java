package com.grupouno.iot.minero.services.impl;

import com.grupouno.iot.minero.dto.SensorDTO;
import com.grupouno.iot.minero.mappers.SensorMapper;
import com.grupouno.iot.minero.models.Location;
import com.grupouno.iot.minero.models.Sensor;
import com.grupouno.iot.minero.models.SensorCategory;
import com.grupouno.iot.minero.repository.LocationRepository;
import com.grupouno.iot.minero.repository.SensorCategoryRepository;
import com.grupouno.iot.minero.repository.SensorRepository;
import com.grupouno.iot.minero.services.SensorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SensorServiceImpl implements SensorService {

	private final SensorRepository sensorRepository;
	private final LocationRepository locationRepository;
	private final SensorCategoryRepository categoryRepository;
	private final SensorMapper sensorMapper;

	public SensorServiceImpl(SensorRepository sensorRepository, LocationRepository locationRepository,
			SensorCategoryRepository categoryRepository, SensorMapper sensorMapper) {
		this.sensorRepository = sensorRepository;
		this.locationRepository = locationRepository;
		this.categoryRepository = categoryRepository;
		this.sensorMapper = sensorMapper;
	}

	@Override
	public List<SensorDTO> getAll() {
		return sensorRepository.findAll().stream().map(sensorMapper::toDTO).collect(Collectors.toList());
	}

	@Override
	public SensorDTO getById(Long id) {
		Sensor sensor = sensorRepository.findById(id).orElseThrow();
		return sensorMapper.toDTO(sensor);
	}

	@Override
	public SensorDTO create(SensorDTO dto) {
		Location location = locationRepository.findById(dto.getLocationId()).orElseThrow();
		SensorCategory category = categoryRepository.findById(dto.getCategoryId()).orElseThrow();

		Sensor sensor = sensorMapper.toEntity(dto, location, category);
		sensor = sensorRepository.save(sensor);
		return sensorMapper.toDTO(sensor);
	}

	@Override
	public SensorDTO update(Long id, SensorDTO dto) {
		Sensor existing = sensorRepository.findById(id).orElseThrow();
		Location location = locationRepository.findById(dto.getLocationId()).orElseThrow();
		SensorCategory category = categoryRepository.findById(dto.getCategoryId()).orElseThrow();

		existing.setName(dto.getName());
		existing.setApiKey(dto.getApiKey());
		existing.setMetadata(dto.getMetadata());
		existing.setActive(dto.isActive());
		existing.setLocation(location);
		existing.setCategory(category);
		existing.setUpdatedAt(dto.getUpdatedAt());

		existing = sensorRepository.save(existing);
		return sensorMapper.toDTO(existing);
	}

	@Override
	public void delete(Long id) {
		Sensor sensor = sensorRepository.findById(id).orElseThrow();
		sensorRepository.delete(sensor);
	}
}
