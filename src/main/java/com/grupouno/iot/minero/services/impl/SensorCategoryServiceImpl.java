package com.grupouno.iot.minero.services.impl;

import com.grupouno.iot.minero.dto.SensorCategoryDTO;
import com.grupouno.iot.minero.mappers.SensorCategoryMapper;
import com.grupouno.iot.minero.models.SensorCategory;
import com.grupouno.iot.minero.repository.SensorCategoryRepository;
import com.grupouno.iot.minero.services.SensorCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SensorCategoryDTO findById(Long id) {
        SensorCategory category = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("SensorCategory no encontrado con id " + id));
        return mapper.toDTO(category);
    }

    @Override
    public SensorCategoryDTO create(SensorCategoryDTO dto) {
        SensorCategory category = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(category));
    }

    @Override
    public SensorCategoryDTO update(Long id, SensorCategoryDTO dto) {
        SensorCategory category = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("SensorCategory no encontrado con id " + id));

        category.setName(dto.getName());
        category.setDescription(dto.getDescription());

        return mapper.toDTO(repository.save(category));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
