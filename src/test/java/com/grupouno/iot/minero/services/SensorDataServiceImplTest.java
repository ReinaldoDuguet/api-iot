package com.grupouno.iot.minero.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.grupouno.iot.minero.dto.SensorDataDTO;
import com.grupouno.iot.minero.exceptions.BadRequestException;
import com.grupouno.iot.minero.mappers.SensorDataMapper;
import com.grupouno.iot.minero.models.Company;
import com.grupouno.iot.minero.models.Location;
import com.grupouno.iot.minero.models.Sensor;
import com.grupouno.iot.minero.models.SensorData;
import com.grupouno.iot.minero.repository.CompanyRepository;
import com.grupouno.iot.minero.repository.SensorDataRepository;
import com.grupouno.iot.minero.repository.SensorRepository;
import com.grupouno.iot.minero.services.impl.SensorDataServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

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

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private SensorDataServiceImpl sensorDataService;

    private Sensor sensor;
    private SensorData sensorData;
    private Map<String, Object> rawMeasurement;
    private List<Map<String, Object>> jsonData;
    private Company company;

    @BeforeEach
    void setUp() {
        // Inicializa el company
        company = new Company();
        company.setId(100L);

        // Asocia el company al location
        Location location = new Location();
        location.setId(10L);
        location.setCompany(company);

        // Configura el sensor
        sensor = new Sensor();
        sensor.setId(1L);
        sensor.setApiKey("sensorApiKey-ABC123");
        sensor.setLocation(location);

        // Configura el sensorData
        sensorData = new SensorData();
        sensorData.setId(10L);
        sensorData.setSensor(sensor);
        sensorData.setTimestamp(1712665800L);
        sensorData.setCreatedAt(LocalDateTime.now());

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("temperature", 24.5);
        dataMap.put("humidity", 40.2);
        sensorData.setData(dataMap);

        rawMeasurement = new HashMap<>();
        rawMeasurement.put("timestamp", 1712665800L);
        rawMeasurement.put("temperature", 24.5);
        rawMeasurement.put("humidity", 40.2);

        jsonData = Collections.singletonList(rawMeasurement);
    }

    @Test
    @Transactional
    void testSaveSensorData_Success() {
        String sensorApiKey = "sensorApiKey-ABC123";

        when(sensorRepository.findByApiKey(sensorApiKey)).thenReturn(Optional.of(sensor));
        when(sensorDataMapper.toEntity(sensor, rawMeasurement)).thenReturn(sensorData);
        when(sensorDataRepository.saveAll(anyList())).thenReturn(Collections.singletonList(sensorData));

        assertDoesNotThrow(() -> sensorDataService.saveSensorData(sensorApiKey, jsonData));

        verify(sensorRepository, times(1)).findByApiKey(sensorApiKey);
        verify(sensorDataMapper, times(1)).toEntity(sensor, rawMeasurement);
        verify(sensorDataRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testSaveSensorData_InvalidApiKey() {
        String sensorApiKey = "invalidKey";
        when(sensorRepository.findByApiKey(sensorApiKey)).thenReturn(Optional.empty());

        BadRequestException ex = assertThrows(BadRequestException.class, 
            () -> sensorDataService.saveSensorData(sensorApiKey, jsonData));

        assertEquals("API key inválida o no asociada a un sensor registrado", ex.getMessage());

        verify(sensorRepository, times(1)).findByApiKey(sensorApiKey);
        verify(sensorDataMapper, never()).toEntity(any(), any());
        verify(sensorDataRepository, never()).saveAll(anyList());
    }

    @Test
    void testGetSensorData_Success() {
        String companyApiKey = "comp-12345678";

        when(companyRepository.findByApiKey(companyApiKey)).thenReturn(Optional.of(company));

        List<SensorData> sensorDataList = Collections.singletonList(sensorData);
        when(sensorDataRepository.findBySensorIdInAndTimestampBetween(anyList(), eq(1712665800L), eq(1712665900L)))
                .thenReturn(sensorDataList);

        SensorDataDTO dto = new SensorDataDTO();
        dto.setId(sensorData.getId());
        dto.setSensorId(sensor.getId());
        dto.setTimestamp(sensorData.getTimestamp());
        dto.setData(sensorData.getData());
        dto.setCreatedAt(sensorData.getCreatedAt());
        dto.setApiKey(sensor.getApiKey());

        when(sensorDataMapper.toDTO(sensorData)).thenReturn(dto);

        List<SensorDataDTO> result = sensorDataService.getSensorData(
                companyApiKey, Arrays.asList(2L, 3L, 4L), 1712665800L, 1712665900L);

        assertNotNull(result);
        assertEquals(1, result.size());

        SensorDataDTO resultDTO = result.get(0);
        assertEquals(10L, resultDTO.getId());
        assertEquals(sensor.getId(), resultDTO.getSensorId());
        assertEquals(1712665800L, resultDTO.getTimestamp());
        assertEquals(24.5, ((Map<String, Object>) resultDTO.getData()).get("temperature"));
        assertEquals("sensorApiKey-ABC123", resultDTO.getApiKey());

        verify(companyRepository, times(1)).findByApiKey(companyApiKey);
        verify(sensorDataRepository, times(1))
                .findBySensorIdInAndTimestampBetween(anyList(), eq(1712665800L), eq(1712665900L));
        verify(sensorDataMapper, times(1)).toDTO(sensorData);
    }

    @Test
    void testGetSensorData_CompanyNotFound() {
        String invalidApiKey = "invalid-comp";

        when(companyRepository.findByApiKey(invalidApiKey)).thenReturn(Optional.empty());

        BadRequestException ex = assertThrows(BadRequestException.class, () ->
                sensorDataService.getSensorData(invalidApiKey, Arrays.asList(2L, 3L), 1712665800L, 1712665900L)
        );

        assertEquals("Company not found with api key: invalid-comp", ex.getMessage());

        verify(companyRepository, times(1)).findByApiKey(invalidApiKey);
        verify(sensorDataRepository, never()).findBySensorIdInAndTimestampBetween(anyList(), anyLong(), anyLong());
    }
}
