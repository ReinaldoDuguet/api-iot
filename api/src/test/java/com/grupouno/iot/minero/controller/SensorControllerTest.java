package com.grupouno.iot.minero.controller;

import com.grupouno.iot.minero.dto.SensorDTO;
import com.grupouno.iot.minero.services.SensorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SensorControllerTest {

    @Mock
    private SensorService sensorService;

    @InjectMocks
    private SensorController sensorController;

    private SensorDTO sensorDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sensorDTO = new SensorDTO();
        sensorDTO.setId(1L);
        sensorDTO.setName("Test Sensor");
    }

    @Test
    void testGetAll() {
        when(sensorService.getAll()).thenReturn(Arrays.asList(sensorDTO));

        ResponseEntity<List<SensorDTO>> response = sensorController.getAll();
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Test Sensor", response.getBody().get(0).getName());
    }

    @Test
    void testGetById() {
        when(sensorService.getById(1L)).thenReturn(sensorDTO);

        ResponseEntity<SensorDTO> response = sensorController.getById(1L);
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Test Sensor", response.getBody().getName());
    }

    @Test
    void testCreate() {
        when(sensorService.create(any(SensorDTO.class))).thenReturn(sensorDTO);

        ResponseEntity<SensorDTO> response = sensorController.create(sensorDTO);
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testUpdate() {
        when(sensorService.update(eq(1L), any(SensorDTO.class))).thenReturn(sensorDTO);

        ResponseEntity<SensorDTO> response = sensorController.update(1L, sensorDTO);
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testDelete() {
        doNothing().when(sensorService).delete(1L);

        ResponseEntity<Void> response = sensorController.delete(1L);
        assertEquals(204, response.getStatusCode().value());
    }
}
