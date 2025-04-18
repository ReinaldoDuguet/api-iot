package com.grupouno.iot.minero.services;

import com.grupouno.iot.minero.dto.SensorDTO;

import java.util.List;

public interface SensorService {
    List<SensorDTO> getAll();
    SensorDTO getById(Long id);
    SensorDTO create(SensorDTO dto);
    SensorDTO update(Long id, SensorDTO dto);
    void delete(Long id);
}
