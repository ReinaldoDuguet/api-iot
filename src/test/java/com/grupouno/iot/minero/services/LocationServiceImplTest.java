package com.grupouno.iot.minero.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.grupouno.iot.minero.dto.LocationDTO;
import com.grupouno.iot.minero.exceptions.EntityNotFoundException;
import com.grupouno.iot.minero.mappers.LocationMapper;
import com.grupouno.iot.minero.models.City;
import com.grupouno.iot.minero.models.Company;
import com.grupouno.iot.minero.models.Location;
import com.grupouno.iot.minero.repository.CityRepository;
import com.grupouno.iot.minero.repository.CompanyRepository;
import com.grupouno.iot.minero.repository.LocationRepository;
import com.grupouno.iot.minero.services.impl.LocationServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class LocationServiceImplTest {

    @Mock
    private LocationRepository locationRepository;
    
    @Mock
    private CompanyRepository companyRepository;
    
    @Mock
    private CityRepository cityRepository;
    
    @InjectMocks
    private LocationServiceImpl locationService;

    private Location location;
    private LocationDTO locationDTO;
    private Company company;
    private City city;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        company = new Company();
        company.setId(1L);
        
        city = new City();
        city.setId(2L);
        
        location = new Location();
        location.setId(3L);
        location.setName("Test Location");
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("key", "value");
        location.setMetadata(metadata);
        location.setCompany(company);
        location.setCity(city);
        location.setCreatedAt(LocalDateTime.now());
        location.setUpdatedAt(LocalDateTime.now());
        
        locationDTO = LocationMapper.toDto(location);
    }

    @Test
    void testGetAllLocations() {
        when(locationRepository.findAll()).thenReturn(List.of(location));
        List<LocationDTO> locations = locationService.getAllLocations();
        assertFalse(locations.isEmpty());
        assertEquals(1, locations.size());
    }

    @Test
    void testGetLocationById() {
        when(locationRepository.findById(3L)).thenReturn(Optional.of(location));
        LocationDTO dto = locationService.getLocationById(3L);
        assertNotNull(dto);
        assertEquals("Test Location", dto.getName());
    }

    @Test
    void testGetLocationById_NotFound() {
        when(locationRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> locationService.getLocationById(3L));
    }

    @Test
    void testCreateLocation() {
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(cityRepository.findById(2L)).thenReturn(Optional.of(city));
        when(locationRepository.save(any(Location.class))).thenReturn(location);
        LocationDTO savedLocation = locationService.createLocation(locationDTO);
        assertNotNull(savedLocation);
        assertEquals("Test Location", savedLocation.getName());
    }

    @Test
    void testCreateLocation_CityNotFound() {
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(cityRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> locationService.createLocation(locationDTO));
    }

    @Test
    void testUpdateLocation() {
        when(locationRepository.findById(3L)).thenReturn(Optional.of(location));
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(cityRepository.findById(2L)).thenReturn(Optional.of(city));
        when(locationRepository.save(any(Location.class))).thenReturn(location);
        
        LocationDTO updatedLocation = locationService.updateLocation(3L, locationDTO);
        assertNotNull(updatedLocation);
        assertEquals("Test Location", updatedLocation.getName());
    }

    @Test
    void testUpdateLocation_NotFound() {
        when(locationRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> locationService.updateLocation(3L, locationDTO));
    }

    @Test
    void testDeleteLocation() {
        when(locationRepository.findById(3L)).thenReturn(Optional.of(location));
        locationService.deleteLocation(3L);
        verify(locationRepository, times(1)).delete(location);
    }

    @Test
    void testDeleteLocation_NotFound() {
        when(locationRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> locationService.deleteLocation(3L));
    }
}
