package com.grupouno.iot.minero.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
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
        request.setApiKey("sensorApiKey-ABC123");
        Map<String, Object> measurement = new HashMap<>();
        measurement.put("timestamp", 1712665800);
        measurement.put("temperature", 24.5);
        measurement.put("humidity", 40.2);
        request.setJsonData(Collections.singletonList(measurement));

        mockMvc.perform(post("/api/v1/sensor_data")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Datos insertados correctamente"));

        verify(sensorDataService, 
            org.mockito.Mockito.times(1)).saveSensorData(eq("sensorApiKey-ABC123"), anyList());
    }

    @Test
    void testGetSensorData() throws Exception {
        // Creamos un SensorDataDTO de ejemplo
        SensorDataDTO dto = new SensorDataDTO();
        dto.setId(1L);
        dto.setSensorId(2L);
        dto.setTimestamp(1712665800);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("temperature", 24.5);
        dataMap.put("humidity", 40.2);
        dto.setData(dataMap);
        dto.setCreatedAt(LocalDateTime.now());

        // Simular que el servicio devuelve una lista con ese DTO
        when(sensorDataService.getSensorData(eq("comp-12345678"), anyList(), eq(1712665800L), eq(1712665900L)))
                .thenReturn(Collections.singletonList(dto));

        // Realizamos la petición GET (usando sensor_id múltiples veces)
        mockMvc.perform(get("/api/v1/sensor_data")
                .param("company_api_key", "comp-12345678")
                .param("from", "1712665800")
                .param("to", "1712665900")
                .param("sensor_id", "2", "3", "4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].sensorId", is(2)))
                .andExpect(jsonPath("$[0].timestamp", is(1712665800)))
                .andExpect(jsonPath("$[0].data.temperature", is(24.5)))
                .andExpect(jsonPath("$[0].data.humidity", is(40.2)));

        verify(sensorDataService, org.mockito.Mockito.times(1))
                .getSensorData(eq("comp-12345678"), anyList(), eq(1712665800L), eq(1712665900L));
    }
}
