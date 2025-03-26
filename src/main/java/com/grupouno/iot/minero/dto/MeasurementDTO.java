package com.grupouno.iot.minero.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MeasurementDTO {
    private Long datetime;
    private Double temp;
    private Double humidity;
}
