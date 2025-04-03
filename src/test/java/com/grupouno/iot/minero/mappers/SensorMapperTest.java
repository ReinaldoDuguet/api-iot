package com.grupouno.iot.minero.mappers;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.grupouno.iot.minero.dto.SensorDTO;
import com.grupouno.iot.minero.models.Location;
import com.grupouno.iot.minero.models.Sensor;
import com.grupouno.iot.minero.models.SensorCategory;

class SensorMapperTest {

	private SensorMapper sensorMapper;

	@BeforeEach
	void setUp() {
		sensorMapper = new SensorMapper();
	}

	@Test
	void testToDTO() {
		Location location = new Location();
		location.setId(1L);

		SensorCategory category = new SensorCategory();
		category.setId(2L);

		Sensor sensor = new Sensor();
		sensor.setId(3L);
		sensor.setName("Temperature Sensor");
		sensor.setApiKey("API123");

		Map<String, Object> metadata = new HashMap<>();
		metadata.put("key", "value");
		sensor.setMetadata(metadata);

		sensor.setActive(true);
		sensor.setLocation(location);
		sensor.setCategory(category);
		sensor.setCreatedAt(LocalDateTime.now());
		sensor.setUpdatedAt(LocalDateTime.now());

		SensorDTO dto = sensorMapper.toDTO(sensor);

		assertNotNull(dto);
		assertEquals(3L, dto.getId());
		assertEquals("Temperature Sensor", dto.getName());
		assertEquals("API123", dto.getApiKey());
		assertEquals("{\"unit\": \"Celsius\"}", dto.getMetadata());
		assertTrue(dto.isActive());
		assertEquals(1L, dto.getLocationId());
		assertEquals(2L, dto.getCategoryId());
		assertNotNull(dto.getCreatedAt());
		assertNotNull(dto.getUpdatedAt());
	}

	@Test
    void testToEntity() {
        SensorDTO dto = new SensorDTO();
        dto.setId(3L);
        dto.setName("Temperature Sensor");
        dto.setApiKey("API123");
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("key", "value");
        dto.setMetadata(metadata);
        dto.setActive(true);
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());
        
        Location location = new Location();
        location.setId(1L);
        
        SensorCategory category = new SensorCategory();
        category.setId(2L);
        
        Sensor sensor = sensorMapper.toEntity(dto, location, category);
        
        assertNotNull(sensor);
        assertEquals(3L, sensor.getId());
        assertEquals("Temperature Sensor", sensor.getName());
        assertEquals("API123", sensor.getApiKey());
        assertEquals("{\"unit\": \"Celsius\"}", sensor.getMetadata());
        assertTrue(sensor.isActive());
        assertEquals(1L, sensor.getLocation().getId());
        assertEquals(2L, sensor.getCategory().getId());
        assertNotNull(sensor.getCreatedAt());
        assertNotNull(sensor.getUpdatedAt());
    }
}
