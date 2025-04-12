package com.grupouno.iot.minero.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.grupouno.iot.minero.dto.MeasurementDTO;
import com.grupouno.iot.minero.dto.SensorDataDTO;
import com.grupouno.iot.minero.exceptions.BadRequestException;
import com.grupouno.iot.minero.mappers.SensorDataMapper;
import com.grupouno.iot.minero.models.Company;
import com.grupouno.iot.minero.models.Sensor;
import com.grupouno.iot.minero.models.SensorData;
import com.grupouno.iot.minero.repository.CompanyRepository;
import com.grupouno.iot.minero.repository.SensorDataRepository;
import com.grupouno.iot.minero.repository.SensorRepository;
import com.grupouno.iot.minero.services.SensorDataService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SensorDataServiceImpl implements SensorDataService {

	private final SensorRepository sensorRepository;
	private final SensorDataRepository sensorDataRepository;
	private final SensorDataMapper sensorDataMapper;
	private final CompanyRepository companyRepository;

	@Override
	@Transactional
	public void saveSensorData(String apiKey, List<Map<String, Object>> jsonData) {
		// Validar que existe un sensor con el sensor_api_key dado
		Sensor sensor = sensorRepository.findByApiKey(apiKey)
				.orElseThrow(() -> new BadRequestException("API key inválida o no asociada a un sensor registrado"));

		List<SensorData> sensorDataList = new ArrayList<>();

		// Para cada medición (representada como Map) en el arreglo jsonData,
		// se utiliza el mapper para convertirla en una entidad SensorData.
		for (Map<String, Object> rawMeasurement : jsonData) {
			SensorData sensorData = sensorDataMapper.toEntity(sensor, rawMeasurement);
			sensorDataList.add(sensorData);
		}

		sensorDataRepository.saveAll(sensorDataList);
	}

	@Override
	public List<SensorDataDTO> getSensorData(String companyApiKey, List<Long> sensorIds, Long from, Long to) {
	    // Validamos que existe una compañía con ese company_api_key
	    Company company = companyRepository.findByApiKey(companyApiKey)
	        .orElseThrow(() -> new BadRequestException("Company not found with api key: " + companyApiKey));
	    
	    List<SensorData> dataList = sensorDataRepository
	        .findBySensorIdInAndTimestampBetween(sensorIds, from, to)
	        .stream()
	        .filter(sd -> sd.getSensor().getLocation().getCompany().getId().equals(company.getId()))
	        .toList();
	    
	    return dataList.stream().map(sensorDataMapper::toDTO).collect(Collectors.toList());
	}

	@Override
	public void processMeasurement(String api_key, MeasurementDTO measurement) {
		// TODO Auto-generated en caso de necesitar procesamiento previo a guardar
		// modificar

	}

}
