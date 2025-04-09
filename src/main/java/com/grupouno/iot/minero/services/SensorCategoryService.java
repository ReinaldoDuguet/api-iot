package com.grupouno.iot.minero.services;

import com.grupouno.iot.minero.dto.SensorCategoryDTO;
import java.util.List;

public interface SensorCategoryService {
    List<SensorCategoryDTO> findAll();
    SensorCategoryDTO findById(Long id);
    SensorCategoryDTO create(SensorCategoryDTO dto);
    SensorCategoryDTO update(Long id, SensorCategoryDTO dto);
    String delete(Long id);
}
