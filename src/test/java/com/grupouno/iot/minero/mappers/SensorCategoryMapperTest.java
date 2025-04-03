package com.grupouno.iot.minero.mappers;

import com.grupouno.iot.minero.dto.SensorCategoryDTO;
import com.grupouno.iot.minero.models.SensorCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class SensorCategoryMapperTest {

    private SensorCategoryMapper sensorCategoryMapper;

    @BeforeEach
    void setUp() {
        sensorCategoryMapper = new SensorCategoryMapper();
    }

    @Test
    void testToDTO() {
        // Arrange
        SensorCategory category = new SensorCategory();
        category.setId(1L);
        category.setName("Temperature");
        category.setDescription("Temperature sensors");
        category.setCreatedAt(LocalDateTime.of(2025, 4, 2, 10, 0));

        // Act
        SensorCategoryDTO dto = sensorCategoryMapper.toDTO(category);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(category.getId());
        assertThat(dto.getName()).isEqualTo(category.getName());
        assertThat(dto.getDescription()).isEqualTo(category.getDescription());
        assertThat(dto.getCreatedAt()).isEqualTo(category.getCreatedAt());
    }

    @Test
    void testToEntity() {
        // Arrange
        SensorCategoryDTO dto = new SensorCategoryDTO();
        dto.setId(1L);
        dto.setName("Temperature");
        dto.setDescription("Temperature sensors");
        dto.setCreatedAt(LocalDateTime.of(2025, 4, 2, 10, 0));

        // Act
        SensorCategory category = sensorCategoryMapper.toEntity(dto);

        // Assert
        assertThat(category).isNotNull();
        assertThat(category.getId()).isEqualTo(dto.getId());
        assertThat(category.getName()).isEqualTo(dto.getName());
        assertThat(category.getDescription()).isEqualTo(dto.getDescription());
        assertThat(category.getCreatedAt()).isEqualTo(dto.getCreatedAt());
    }
}
