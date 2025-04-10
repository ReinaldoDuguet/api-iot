package com.grupouno.iot.minero.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.grupouno.iot.minero.dto.SensorCategoryDTO;
import com.grupouno.iot.minero.services.SensorCategoryService;

class SensorCategoryControllerTest {

    private SensorCategoryService service;
    private SensorCategoryController controller;

    @BeforeEach
    void setUp() {
        service = mock(SensorCategoryService.class);
        controller = new SensorCategoryController(service);
    }

    @Test
    void testGetAll() {
        // Arrange
        SensorCategoryDTO dto = new SensorCategoryDTO();
        dto.setId(1L);
        dto.setName("Temperature");
        dto.setDescription("Temperature sensors");
        List<SensorCategoryDTO> expectedList = Collections.singletonList(dto);
        when(service.findAll()).thenReturn(expectedList);

        // Act
        ResponseEntity<List<SensorCategoryDTO>> response = controller.getAll();

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(expectedList);
        verify(service, times(1)).findAll();
    }

    @Test
    void testGetById() {
        // Arrange
        Long id = 1L;
        SensorCategoryDTO dto = new SensorCategoryDTO();
        dto.setId(id);
        dto.setName("Temperature");
        dto.setDescription("Temperature sensors");
        when(service.findById(id)).thenReturn(dto);

        // Act
        ResponseEntity<SensorCategoryDTO> response = controller.getById(id);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(dto);
        verify(service, times(1)).findById(id);
    }

    @Test
    void testCreate() {
        // Arrange
        SensorCategoryDTO inputDto = new SensorCategoryDTO();
        inputDto.setName("Temperature");
        inputDto.setDescription("Temperature sensors");
        SensorCategoryDTO savedDto = new SensorCategoryDTO();
        savedDto.setId(1L);
        savedDto.setName("Temperature");
        savedDto.setDescription("Temperature sensors");
        when(service.create(inputDto)).thenReturn(savedDto);

        // Act
        ResponseEntity<SensorCategoryDTO> response = controller.create(inputDto);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(savedDto);
        verify(service, times(1)).create(inputDto);
    }

    @Test
    void testUpdate() {
        // Arrange
        Long id = 1L;
        SensorCategoryDTO inputDto = new SensorCategoryDTO();
        inputDto.setName("Updated Name");
        inputDto.setDescription("Updated Description");
        SensorCategoryDTO updatedDto = new SensorCategoryDTO();
        updatedDto.setId(id);
        updatedDto.setName("Updated Name");
        updatedDto.setDescription("Updated Description");
        when(service.update(id, inputDto)).thenReturn(updatedDto);

        // Act
        ResponseEntity<SensorCategoryDTO> response = controller.update(id, inputDto);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(updatedDto);
        verify(service, times(1)).update(id, inputDto);
    }

    @Test
    void testDelete() {
        // Arrange
        Long id = 1L;
        String expectedMessage = "Se ha eliminado el elemento [nombre] con id 1";
        when(service.delete(id)).thenReturn(expectedMessage);

        // Act
        ResponseEntity<String> response = controller.delete(id);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(expectedMessage);
        verify(service, times(1)).delete(id);
    }

}
