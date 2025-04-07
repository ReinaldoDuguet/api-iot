package com.grupouno.iot.minero.services;

import com.grupouno.iot.minero.dto.SensorDTO;
import com.grupouno.iot.minero.exceptions.ApiKeyAlreadyExistsException;
import com.grupouno.iot.minero.mappers.SensorMapper;
import com.grupouno.iot.minero.models.Location;
import com.grupouno.iot.minero.models.Sensor;
import com.grupouno.iot.minero.models.SensorCategory;
import com.grupouno.iot.minero.repository.LocationRepository;
import com.grupouno.iot.minero.repository.SensorCategoryRepository;
import com.grupouno.iot.minero.repository.SensorRepository;
import com.grupouno.iot.minero.services.impl.SensorServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SensorServiceImplTest {

    @Mock
    private SensorRepository sensorRepository;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private SensorCategoryRepository categoryRepository;

    @Mock
    private SensorMapper sensorMapper;

    @InjectMocks
    private SensorServiceImpl sensorService;

    private Sensor sensor;
    private SensorDTO sensorDTO;
    private Location location;
    private SensorCategory category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        location = new Location();
        location.setId(1L);

        category = new SensorCategory();
        category.setId(1L);

        sensor = new Sensor();
        sensor.setId(1L);
        sensor.setName("Sensor Test");
        sensor.setLocation(location);
        sensor.setCategory(category);

        sensorDTO = new SensorDTO();
        sensorDTO.setId(1L);
        sensorDTO.setName("Sensor Test");
        sensorDTO.setLocationId(1L);
        sensorDTO.setCategoryId(1L);
    }

    @Test
    void testGetAll() {
        when(sensorRepository.findAll()).thenReturn(Collections.singletonList(sensor));
        when(sensorMapper.toDTO(sensor)).thenReturn(sensorDTO);

        List<SensorDTO> result = sensorService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sensorDTO, result.get(0));

        verify(sensorRepository, times(1)).findAll();
        verify(sensorMapper, times(1)).toDTO(sensor);
    }

    @Test
    void testGetById() {
        when(sensorRepository.findById(1L)).thenReturn(Optional.of(sensor));
        when(sensorMapper.toDTO(sensor)).thenReturn(sensorDTO);

        SensorDTO result = sensorService.getById(1L);

        assertNotNull(result);
        assertEquals(sensorDTO, result);

        verify(sensorRepository, times(1)).findById(1L);
        verify(sensorMapper, times(1)).toDTO(sensor);
    }

    @Test
    void testCreate() {
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(sensorMapper.toEntity(sensorDTO, location, category)).thenReturn(sensor);
        when(sensorRepository.save(sensor)).thenReturn(sensor);
        when(sensorMapper.toDTO(sensor)).thenReturn(sensorDTO);

        SensorDTO result = sensorService.create(sensorDTO);

        assertNotNull(result);
        assertEquals(sensorDTO, result);

        verify(locationRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).findById(1L);
        verify(sensorMapper, times(1)).toEntity(sensorDTO, location, category);
        verify(sensorRepository, times(1)).save(sensor);
        verify(sensorMapper, times(1)).toDTO(sensor);
    }

    @Test
    void testUpdate_NoApiKeyChange() {
        // Caso en el que el DTO no modifica la apiKey
        // Aseguramos que el apiKey en sensorDTO es igual al existente
        sensorDTO.setApiKey(sensor.getApiKey()); // No se cambia la apiKey
        sensorDTO.setName("Updated Sensor Name");

        when(sensorRepository.findById(1L)).thenReturn(Optional.of(sensor));
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(sensorRepository.save(sensor)).thenReturn(sensor);
        when(sensorMapper.toDTO(sensor)).thenReturn(sensorDTO);

        SensorDTO result = sensorService.update(1L, sensorDTO);

        assertNotNull(result);
        assertEquals("Updated Sensor Name", result.getName());

        verify(sensorRepository, times(1)).findById(1L);
        verify(sensorRepository, times(1)).save(sensor);
        verify(sensorMapper, times(1)).toDTO(sensor);
    }

    @Test
    void testUpdate_ApiKeyChangedAndUnique() {
        // Caso en el que se modifica la apiKey y es única
        String newApiKey = "NEW_API_KEY";
        sensorDTO.setApiKey(newApiKey); // Cambia la apiKey (y es diferente de la existente)
        sensorDTO.setName("Updated Sensor Name");

        when(sensorRepository.findById(1L)).thenReturn(Optional.of(sensor));
        when(sensorRepository.existsByApiKey(newApiKey)).thenReturn(false);
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(sensorRepository.save(sensor)).thenReturn(sensor);
        when(sensorMapper.toDTO(sensor)).thenReturn(sensorDTO);

        SensorDTO result = sensorService.update(1L, sensorDTO);

        assertNotNull(result);
        assertEquals(newApiKey, result.getApiKey());
        assertEquals("Updated Sensor Name", result.getName());

        verify(sensorRepository, times(1)).findById(1L);
        verify(sensorRepository, times(1)).existsByApiKey(newApiKey);
        verify(sensorRepository, times(1)).save(sensor);
        verify(sensorMapper, times(1)).toDTO(sensor);
    }

    @Test
    void testUpdate_ApiKeyChangedAndAlreadyExists() {
        // Caso en el que se modifica la apiKey pero ya está en uso
        String newApiKey = "DUPLICATE_API_KEY";
        sensorDTO.setApiKey(newApiKey); // Se cambia la apiKey
        sensorDTO.setName("Updated Sensor Name");

        when(sensorRepository.findById(1L)).thenReturn(Optional.of(sensor));
        when(sensorRepository.existsByApiKey(newApiKey)).thenReturn(true);

        Exception exception = assertThrows(ApiKeyAlreadyExistsException.class,
                () -> sensorService.update(1L, sensorDTO));

        String expectedMessage = "The API Key is already in use by another sensor.";
        assertTrue(exception.getMessage().contains(expectedMessage));

        verify(sensorRepository, times(1)).findById(1L);
        verify(sensorRepository, times(1)).existsByApiKey(newApiKey);
        verify(sensorRepository, never()).save(any(Sensor.class));
        verify(sensorMapper, never()).toDTO(any(Sensor.class));
    }

    @Test
    void testDelete() {
        when(sensorRepository.findById(1L)).thenReturn(Optional.of(sensor));
        doNothing().when(sensorRepository).delete(sensor);

        sensorService.delete(1L);

        verify(sensorRepository, times(1)).findById(1L);
        verify(sensorRepository, times(1)).delete(sensor);
    }
}
