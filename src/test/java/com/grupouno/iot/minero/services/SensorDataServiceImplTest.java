package com.grupouno.iot.minero.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.grupouno.iot.minero.exceptions.BadRequestException;
import com.grupouno.iot.minero.mappers.SensorDataMapper;
import com.grupouno.iot.minero.models.Sensor;
import com.grupouno.iot.minero.models.SensorData;
import com.grupouno.iot.minero.repository.SensorDataRepository;
import com.grupouno.iot.minero.repository.SensorRepository;
import com.grupouno.iot.minero.services.impl.SensorDataServiceImpl;

@ExtendWith(MockitoExtension.class)
class SensorDataServiceImplTest {

    @Mock
    private SensorRepository sensorRepository;

    @Mock
    private SensorDataRepository sensorDataRepository;

    @Mock
    private SensorDataMapper sensorDataMapper;

    @InjectMocks
    private SensorDataServiceImpl sensorDataService;

    private Sensor sensor;
    private SensorData sensorData;
    private Map<String, Object> rawMeasurement;
    private List<Map<String, Object>> jsonData;

    @BeforeEach
    void setUp() {
        sensor = new Sensor();
        sensor.setId(1L);

        sensorData = new SensorData();
        sensorData.setId(10L);
        sensorData.setSensor(sensor);
        sensorData.setTimestamp(1625078400000L);
        sensorData.setCreatedAt(LocalDateTime.now());

        // El mapa de medición con timestamp y un valor (puede ser extendido a otros campos)
        rawMeasurement = new HashMap<>();
        rawMeasurement.put("timestamp", 1625078400000L);
        rawMeasurement.put("value", 25);

        jsonData = Collections.singletonList(rawMeasurement);
    }

    @Test
    void testSaveSensorData_Success() {
        String apiKey = "validApiKey";

        when(sensorRepository.findByApiKey(apiKey)).thenReturn(Optional.of(sensor));
        when(sensorDataMapper.toEntity(sensor, rawMeasurement)).thenReturn(sensorData);
        when(sensorDataRepository.saveAll(anyList())).thenReturn(Collections.singletonList(sensorData));

        assertDoesNotThrow(() -> sensorDataService.saveSensorData(apiKey, jsonData));

        verify(sensorRepository, times(1)).findByApiKey(apiKey);
        verify(sensorDataMapper, times(1)).toEntity(sensor, rawMeasurement);
        verify(sensorDataRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testSaveSensorData_InvalidApiKey() {
        String apiKey = "invalidApiKey";
        when(sensorRepository.findByApiKey(apiKey)).thenReturn(Optional.empty());

        BadRequestException ex = assertThrows(BadRequestException.class,
            () -> sensorDataService.saveSensorData(apiKey, jsonData));

        assertEquals("API key inválida o no asociada a un sensor registrado", ex.getMessage());

        verify(sensorRepository, times(1)).findByApiKey(apiKey);
        verify(sensorDataMapper, never()).toEntity(any(), any());
        verify(sensorDataRepository, never()).saveAll(anyList());
    }
}
