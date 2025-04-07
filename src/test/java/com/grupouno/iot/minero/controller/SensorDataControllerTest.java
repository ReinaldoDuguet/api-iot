package com.grupouno.iot.minero.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupouno.iot.minero.dto.SensorDataDTO;
import com.grupouno.iot.minero.dto.SensorDataRequestDTO;
import com.grupouno.iot.minero.services.SensorDataService;

class SensorDataControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SensorDataService sensorDataService;

    @InjectMocks
    private SensorDataController sensorDataController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(sensorDataController).build();
    }

    @Test
    void testInsertSensorData() throws Exception {
        SensorDataRequestDTO request = new SensorDataRequestDTO();
        request.setApiKey("api123");
        Map<String, Object> measurement = new HashMap<>();
        measurement.put("timestamp", 1625078400000L);
        measurement.put("value", 25);
        request.setJsonData(Collections.singletonList(measurement));

        mockMvc.perform(post("/api/v1/sensor-data")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Datos insertados correctamente"));

        verify(sensorDataService, times(1)).saveSensorData(eq("api123"), anyList());
    }

    @Test
    void testGetSensorDataBySensor() throws Exception {
        SensorDataDTO dto = new SensorDataDTO();
        dto.setId(1L);
        dto.setSensorId(1L);
        dto.setTimestamp(1625078400000L);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("value", 25);
        dto.setData(dataMap);
        dto.setCreatedAt(LocalDateTime.now());

        when(sensorDataService.getSensorDataBySensorId(1L)).thenReturn(Collections.singletonList(dto));

        mockMvc.perform(get("/api/v1/sensor-data/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].sensorId", is(1)))
                .andExpect(jsonPath("$[0].timestamp", is(1625078400000L)))
                .andExpect(jsonPath("$[0].data.value", is(25)));

        verify(sensorDataService, times(1)).getSensorDataBySensorId(1L);
    }
}
