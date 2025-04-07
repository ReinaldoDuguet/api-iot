package com.grupouno.iot.minero.services.impl;

import com.grupouno.iot.minero.dto.SensorDTO;
import com.grupouno.iot.minero.exceptions.ApiKeyAlreadyExistsException;
import com.grupouno.iot.minero.exceptions.EntityNotFoundException;
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
		
		if (sensorRepository.existsByApiKey(dto.getApiKey())) {
            throw new ApiKeyAlreadyExistsException("La api_key ya está en uso.");
		}
		
		Location location = locationRepository.findById(dto.getLocationId()).orElseThrow();
		SensorCategory category = categoryRepository.findById(dto.getCategoryId()).orElseThrow();

		Sensor sensor = sensorMapper.toEntity(dto, location, category);
		sensor = sensorRepository.save(sensor);
		return sensorMapper.toDTO(sensor);
	}

	@Override
	public SensorDTO update(Long id, SensorDTO dto) {
	    Sensor existing = sensorRepository.findById(id)
	        .orElseThrow(() -> new EntityNotFoundException("Sensor not found with id: " + id));

	    // Validar y actualizar apiKey solo si es distinta y no nula
	    if (dto.getApiKey() != null && !dto.getApiKey().equals(existing.getApiKey())) {
	        boolean apiKeyExists = sensorRepository.existsByApiKey(dto.getApiKey());
	        if (apiKeyExists) {
	            throw new ApiKeyAlreadyExistsException("The API Key is already in use by another sensor.");
	        }
	        existing.setApiKey(dto.getApiKey());
	    }

	    if (dto.getName() != null && !dto.getName().equals(existing.getName())) {
	        existing.setName(dto.getName());
	    }

	    if (dto.getMetadata() != null && !dto.getMetadata().equals(existing.getMetadata())) {
	        existing.setMetadata(dto.getMetadata());
	    }

	    if (dto.getLocationId() != null && 
	        (existing.getLocation() == null || !dto.getLocationId().equals(existing.getLocation().getId()))) {
	        Location location = locationRepository.findById(dto.getLocationId())
	            .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + dto.getLocationId()));
	        existing.setLocation(location);
	    }

	    if (dto.getCategoryId() != null && 
	        (existing.getCategory() == null || !dto.getCategoryId().equals(existing.getCategory().getId()))) {
	        SensorCategory category = categoryRepository.findById(dto.getCategoryId())
	            .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + dto.getCategoryId()));
	        existing.setCategory(category);
	    }

	    if (dto.getUpdatedAt() != null && !dto.getUpdatedAt().equals(existing.getUpdatedAt())) {
	        existing.setUpdatedAt(dto.getUpdatedAt());
	    }

	    if (dto.isActive() != existing.isActive()) {
	        existing.setActive(dto.isActive());
	    }

	    Sensor updatedSensor = sensorRepository.save(existing);
	    return sensorMapper.toDTO(updatedSensor);
	}


	@Override
	public void delete(Long id) {
		Sensor sensor = sensorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sensor not found"));
		sensorRepository.delete(sensor);
	}
}
