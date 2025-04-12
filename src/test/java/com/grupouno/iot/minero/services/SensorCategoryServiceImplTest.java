package com.grupouno.iot.minero.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.grupouno.iot.minero.dto.SensorCategoryDTO;
import com.grupouno.iot.minero.mappers.SensorCategoryMapper;
import com.grupouno.iot.minero.models.SensorCategory;
import com.grupouno.iot.minero.repository.SensorCategoryRepository;
import com.grupouno.iot.minero.services.impl.SensorCategoryServiceImpl;

class SensorCategoryServiceImplTest {

    @Mock
    private SensorCategoryRepository repository;

    @Mock
    private SensorCategoryMapper mapper;

    @InjectMocks
    private SensorCategoryServiceImpl sensorCategoryService;

    private SensorCategory category;
    private SensorCategoryDTO categoryDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        category = new SensorCategory();
        category.setId(1L);
        category.setName("Temperature");
        category.setDescription("Temperature sensors");
        category.setCreatedAt(LocalDateTime.of(2025, 4, 2, 10, 0));

        categoryDTO = new SensorCategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setName("Temperature");
        categoryDTO.setDescription("Temperature sensors");
        categoryDTO.setCreatedAt(LocalDateTime.of(2025, 4, 2, 10, 0));
    }

    @Test
    void testFindAll() {
        when(repository.findAll()).thenReturn(Collections.singletonList(category));
        when(mapper.toDTO(category)).thenReturn(categoryDTO);

        // Act
        var result = sensorCategoryService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(categoryDTO, result.get(0));

        verify(repository, times(1)).findAll();
        verify(mapper, times(1)).toDTO(category);
    }

    @Test
    void testFindById_WhenFound() {
        when(repository.findById(1L)).thenReturn(Optional.of(category));
        when(mapper.toDTO(category)).thenReturn(categoryDTO);

        SensorCategoryDTO result = sensorCategoryService.findById(1L);

        assertNotNull(result);
        assertEquals(categoryDTO, result);

        verify(repository, times(1)).findById(1L);
        verify(mapper, times(1)).toDTO(category);
    }

    @Test
    void testFindById_WhenNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> sensorCategoryService.findById(1L));
        assertTrue(exception.getMessage().contains("SensorCategory no encontrado con id"));
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testCreate() {
        when(mapper.toEntity(categoryDTO)).thenReturn(category);
        when(repository.save(category)).thenReturn(category);
        when(mapper.toDTO(category)).thenReturn(categoryDTO);

        SensorCategoryDTO result = sensorCategoryService.create(categoryDTO);

        assertNotNull(result);
        assertEquals(categoryDTO, result);

        verify(mapper, times(1)).toEntity(categoryDTO);
        verify(repository, times(1)).save(category);
        verify(mapper, times(1)).toDTO(category);
    }

    @Test
    void testUpdate_WhenFound() {
        SensorCategoryDTO updatedDTO = new SensorCategoryDTO();
        updatedDTO.setName("Updated Name");
        updatedDTO.setDescription("Updated Description");
        updatedDTO.setId(1L);
        updatedDTO.setCreatedAt(categoryDTO.getCreatedAt());

        when(repository.findById(1L)).thenReturn(Optional.of(category));
        // Actualizamos la entidad y se espera que se guarde
        category.setName(updatedDTO.getName());
        category.setDescription(updatedDTO.getDescription());
        when(repository.save(category)).thenReturn(category);
        when(mapper.toDTO(category)).thenReturn(updatedDTO);

        SensorCategoryDTO result = sensorCategoryService.update(1L, updatedDTO);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("Updated Description", result.getDescription());

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(category);
        verify(mapper, times(1)).toDTO(category);
    }

    @Test
    void testUpdate_WhenNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        SensorCategoryDTO updatedDTO = new SensorCategoryDTO();
        updatedDTO.setName("Updated Name");
        updatedDTO.setDescription("Updated Description");
        Exception exception = assertThrows(RuntimeException.class, () -> sensorCategoryService.update(1L, updatedDTO));
        assertTrue(exception.getMessage().contains("SensorCategory no encontrado con id"));
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testDelete() {
        when(repository.findById(1L)).thenReturn(Optional.of(category));
        doNothing().when(repository).deleteById(1L);

        sensorCategoryService.delete(1L);

        verify(repository, times(1)).deleteById(1L);
    }
}
