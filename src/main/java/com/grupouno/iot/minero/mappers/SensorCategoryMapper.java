package com.grupouno.iot.minero.mappers;

import com.grupouno.iot.minero.dto.SensorCategoryDTO;
import com.grupouno.iot.minero.models.SensorCategory;
import org.springframework.stereotype.Component;

@Component
public class SensorCategoryMapper {

    public SensorCategoryDTO toDTO(SensorCategory category) {
        SensorCategoryDTO dto = new SensorCategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setCreatedAt(category.getCreatedAt());
        return dto;
    }

    public SensorCategory toEntity(SensorCategoryDTO dto) {
        SensorCategory category = new SensorCategory();
        category.setId(dto.getId());
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : category.getCreatedAt());
        return category;
    }
}
