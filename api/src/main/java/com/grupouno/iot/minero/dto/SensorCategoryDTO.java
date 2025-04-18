package com.grupouno.iot.minero.dto;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SensorCategoryDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
}
