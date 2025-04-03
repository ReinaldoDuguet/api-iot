package com.grupouno.iot.minero.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.grupouno.iot.minero.dto.SensorDataDTO;
import com.grupouno.iot.minero.mappers.SensorDataMapper;
import com.grupouno.iot.minero.models.Sensor;
import com.grupouno.iot.minero.models.SensorData;
import com.grupouno.iot.minero.repository.SensorDataRepository;
import com.grupouno.iot.minero.repository.SensorRepository;
import com.grupouno.iot.minero.services.impl.SensorDataServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

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
    private SensorDataDTO sensorDataDTO;
    private Map<String, Object> jsonData;

    @BeforeEach
    void setUp() {
        sensor = new Sensor();
        sensor.setId(1L);

        sensorData = new SensorData();
        sensorData.setId(10L);
        sensorData.setSensor(sensor);
        sensorData.setTimestamp(1625078400000L);
        sensorData.setCreatedAt(LocalDateTime.now());
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("value", 25);
        sensorData.setData(dataMap);

        sensorDataDTO = new SensorDataDTO();
        sensorDataDTO.setId(10L);
        sensorDataDTO.setSensorId(1L);
        sensorDataDTO.setTimestamp(1625078400000L);
        sensorDataDTO.setData(dataMap);
        sensorDataDTO.setCreatedAt(sensorData.getCreatedAt());

        jsonData = new HashMap<>();
        jsonData.put("timestamp", 1625078400000L);
        jsonData.put("value", 25);
    }

    @Test
    void testSaveSensorData_Success() {
        String apiKey = "api123";
        List<Map<String, Object>> jsonList = Collections.singletonList(jsonData);

        when(sensorRepository.findByApiKey(apiKey)).thenReturn(Optional.of(sensor));
        when(sensorDataMapper.toEntity(sensor, jsonData)).thenReturn(sensorData);
        when(sensorDataRepository.saveAll(anyList())).thenReturn(Collections.singletonList(sensorData));

        sensorDataService.saveSensorData(apiKey, jsonList);

        verify(sensorRepository, times(1)).findByApiKey(apiKey);
        verify(sensorDataMapper, times(1)).toEntity(sensor, jsonData);
        verify(sensorDataRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testSaveSensorData_InvalidApiKey() {
        String apiKey = "invalid";
        List<Map<String, Object>> jsonList = Collections.singletonList(jsonData);

        when(sensorRepository.findByApiKey(apiKey)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> sensorDataService.saveSensorData(apiKey, jsonList));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());

        verify(sensorRepository, times(1)).findByApiKey(apiKey);
        verify(sensorDataMapper, never()).toEntity(any(), any());
        verify(sensorDataRepository, never()).saveAll(anyList());
    }

    @Test
    void testGetSensorDataBySensorId() {
        Long sensorId = 1L;
        List<SensorData> sensorDataList = Collections.singletonList(sensorData);
        when(sensorDataRepository.findBySensorId(sensorId)).thenReturn(sensorDataList);
        when(sensorDataMapper.toDTO(sensorData)).thenReturn(sensorDataDTO);

        List<SensorDataDTO> result = sensorDataService.getSensorDataBySensorId(sensorId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sensorDataDTO, result.get(0));

        verify(sensorDataRepository, times(1)).findBySensorId(sensorId);
        verify(sensorDataMapper, times(1)).toDTO(sensorData);
    }
}
