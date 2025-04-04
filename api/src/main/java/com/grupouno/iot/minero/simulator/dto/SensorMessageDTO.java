package com.grupouno.iot.minero.simulator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorMessageDTO {
    private String api_key;
    private List<MeasurementDTO> json_data;
}
