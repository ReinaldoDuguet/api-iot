package com.grupouno.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MeasurementDTO {
    private Long datetime;
    private Double temp;
    private Double humidity;
}
