package com.grupouno.iot.minero.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CompanyDTO {

    private Long id;
    private String name;
    private String apiKey;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Long> locationIds;
}
