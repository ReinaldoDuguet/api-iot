package com.grupouno.iot.minero.mappers;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.grupouno.iot.minero.dto.LocationDTO;
import com.grupouno.iot.minero.models.City;
import com.grupouno.iot.minero.models.Company;
import com.grupouno.iot.minero.models.Location;

class LocationMapperTest {

    @Test
    void testToDto() {
        Company company = new Company();
        company.setId(1L);
        
        City city = new City();
        city.setId(2L);
        
        Location location = new Location();
        location.setId(3L);
        location.setName("Test Location");
        
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("key", "value");
        location.setMetadata(metadata);
        
        location.setCompany(company);
        location.setCity(city);
        location.setCreatedAt(LocalDateTime.now());
        location.setUpdatedAt(LocalDateTime.now());

        LocationDTO dto = LocationMapper.toDto(location);

        assertNotNull(dto);
        assertEquals(3L, dto.getId());
        assertEquals("Test Location", dto.getName());
        
        assertEquals(metadata, dto.getMetadata());
        
        assertEquals(1L, dto.getCompanyId());
        assertEquals(2L, dto.getCityId());
        assertNotNull(dto.getCreatedAt());
        assertNotNull(dto.getUpdatedAt());
    }

    @Test
    void testToEntity() {
        LocationDTO dto = new LocationDTO();
        dto.setId(3L);
        dto.setName("Test Location");
        
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("key", "value");
        dto.setMetadata(metadata);
        
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());

        Company company = new Company();
        company.setId(1L);

        City city = new City();
        city.setId(2L);

        Location location = LocationMapper.toEntity(dto, company, city);

        assertNotNull(location);
        assertEquals(3L, location.getId());
        assertEquals("Test Location", location.getName());
        
        assertEquals(metadata, location.getMetadata());
        
        assertEquals(1L, location.getCompany().getId());
        assertEquals(2L, location.getCity().getId());
        assertNotNull(location.getCreatedAt());
        assertNotNull(location.getUpdatedAt());
    }
}
