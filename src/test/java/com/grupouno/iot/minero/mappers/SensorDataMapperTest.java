package com.grupouno.iot.minero.mappers;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.grupouno.iot.minero.dto.SensorDataDTO;
import com.grupouno.iot.minero.models.Sensor;
import com.grupouno.iot.minero.models.SensorData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SensorDataMapperTest {

    private SensorDataMapper sensorDataMapper;
    private Sensor sensor;

    @BeforeEach
    void setUp() {
        sensorDataMapper = new SensorDataMapper();
        sensor = new Sensor();
        sensor.setId(1L);
    }

    @Test
    void testToEntity_ValidData() {
        Map<String, Object> json = new HashMap<>();
        json.put("timestamp", 1625078400000L);
        json.put("value", 23.5);

        SensorData sensorData = sensorDataMapper.toEntity(sensor, json);
        assertNotNull(sensorData);
        assertEquals(1L, sensorData.getSensor().getId());
        assertEquals(1625078400000L, sensorData.getTimestamp());
        assertEquals(json, sensorData.getData());
        assertNotNull(sensorData.getCreatedAt());
    }

    @Test
    void testToEntity_InvalidTimestamp() {
        Map<String, Object> json = new HashMap<>();
        json.put("timestamp", "not a number");
        json.put("value", 23.5);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> sensorDataMapper.toEntity(sensor, json));
        assertTrue(exception.getMessage().contains("El campo 'timestamp' debe estar presente y ser numérico."));
    }

    @Test
    void testToDTO() {
        SensorData sensorData = new SensorData();
        sensorData.setId(1L);
        sensorData.setSensor(sensor);
        sensorData.setTimestamp(1625078400000L);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("value", 23.5);
        sensorData.setData(dataMap);
        sensorData.setCreatedAt(LocalDateTime.now());

        SensorDataDTO dto = sensorDataMapper.toDTO(sensorData);
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(sensor.getId(), dto.getSensorId());
        assertEquals(1625078400000L, dto.getTimestamp());
        assertEquals(dataMap, dto.getData());
        assertNotNull(dto.getCreatedAt());
    }
}
