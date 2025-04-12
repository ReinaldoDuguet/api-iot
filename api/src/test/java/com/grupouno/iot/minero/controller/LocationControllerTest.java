package com.grupouno.iot.minero.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import com.grupouno.iot.minero.exceptions.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupouno.iot.minero.dto.LocationDTO;
import com.grupouno.iot.minero.models.City;
import com.grupouno.iot.minero.services.CityService;
import com.grupouno.iot.minero.services.LocationService;

class LocationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LocationService locationService;
    
    @Mock
    private CityService cityService;
    
    @InjectMocks
    private LocationController locationController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(locationController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testGetAllLocations() throws Exception {
        when(locationService.getAllLocations()).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/api/v1/locations"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testGetLocationById() throws Exception {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setId(1L);
        locationDTO.setName("Test Location");
        
        when(locationService.getLocationById(1L)).thenReturn(locationDTO);
        
        mockMvc.perform(get("/api/v1/locations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Location"));
    }

    @Test
    void testCreateLocation() throws Exception {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setName("New Location");
        locationDTO.setCityId(1L);
        
        City city = new City();
        city.setId(1L);
        
        when(cityService.getCityById(1L)).thenReturn(city);
        when(locationService.createLocation(any(LocationDTO.class))).thenReturn(locationDTO);
        
        mockMvc.perform(post("/api/v1/locations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(locationDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Location"));
    }

    @Test
    void testUpdateLocation() throws Exception {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setId(1L);
        locationDTO.setName("Updated Location");
        locationDTO.setCityId(1L);
        
        City city = new City();
        city.setId(1L);
        
        when(cityService.getCityById(1L)).thenReturn(city);
        when(locationService.updateLocation(eq(1L), any(LocationDTO.class))).thenReturn(locationDTO);
        
        mockMvc.perform(put("/api/v1/locations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(locationDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Location"));
    }

    @Test
    void testDeleteLocation() throws Exception {
        doNothing().when(locationService).deleteLocation(1L);
        
        mockMvc.perform(delete("/api/v1/locations/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateLocationWithInvalidCity() throws Exception {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setName("Invalid Location");
        locationDTO.setCityId(999L);

        when(cityService.getCityById(999L))
                .thenThrow(new jakarta.persistence.EntityNotFoundException("City not found with id: 999"));

        mockMvc.perform(post("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(locationDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("City not found with id: 999"))
                .andExpect(jsonPath("$.status").value(404));
    }

}
