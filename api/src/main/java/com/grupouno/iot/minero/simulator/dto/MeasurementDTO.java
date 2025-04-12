package com.grupouno.iot.minero.simulator.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MeasurementDTO {

	private Long timestamp;
	private Map<String, Object> values;
}