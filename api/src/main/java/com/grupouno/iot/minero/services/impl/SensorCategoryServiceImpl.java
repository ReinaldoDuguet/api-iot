package com.grupouno.iot.minero.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.grupouno.iot.minero.dto.SensorCategoryDTO;
import com.grupouno.iot.minero.exceptions.EntityAlreadyExistsException;
import com.grupouno.iot.minero.exceptions.EntityNotFoundException;
import com.grupouno.iot.minero.mappers.SensorCategoryMapper;
import com.grupouno.iot.minero.models.SensorCategory;
import com.grupouno.iot.minero.repository.SensorCategoryRepository;
import com.grupouno.iot.minero.services.SensorCategoryService;

@Service
public class SensorCategoryServiceImpl implements SensorCategoryService {

	private final SensorCategoryRepository repository;
	private final SensorCategoryMapper mapper;

	public SensorCategoryServiceImpl(SensorCategoryRepository repository, SensorCategoryMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	@Override
	public List<SensorCategoryDTO> findAll() {
		return repository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
	}

	@Override
	public SensorCategoryDTO findById(Long id) {
		SensorCategory category = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("SensorCategory no encontrado con id " + id));
		return mapper.toDTO(category);
	}

	@Override
	public SensorCategoryDTO create(SensorCategoryDTO dto) {
		boolean exists = repository.existsByName(dto.getName());
		if (exists) {
			throw new EntityAlreadyExistsException("SensorCategory con nombre '" + dto.getName() + "' ya existe.");
		}

		SensorCategory category = mapper.toEntity(dto);
		return mapper.toDTO(repository.save(category));
	}

	@Override
	public SensorCategoryDTO update(Long id, SensorCategoryDTO dto) {
	    SensorCategory category = repository.findById(id)
	            .orElseThrow(() -> new EntityNotFoundException("SensorCategory no encontrado con id: " + id));
	    
	    if (dto.getName() != null && !dto.getName().equals(category.getName())) {
	        category.setName(dto.getName());
	    }
	    
	    if (dto.getDescription() != null && !dto.getDescription().equals(category.getDescription())) {
	        category.setDescription(dto.getDescription());
	    }
	    	    
	    return mapper.toDTO(repository.save(category));
	}

	@Override
	public String delete(Long id) {
	    var entidad = repository.findById(id)
	            .orElseThrow(() -> new EntityNotFoundException("SensorCategory no encontrado con id: " + id));
	    
	    String mensaje = "Se ha eliminado el elemento " + entidad.getName() + " con id " + id;
	    repository.deleteById(id);
	    return mensaje;
	}


}
