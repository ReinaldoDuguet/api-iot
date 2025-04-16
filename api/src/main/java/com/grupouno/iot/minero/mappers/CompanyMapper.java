package com.grupouno.iot.minero.mappers;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.grupouno.iot.minero.dto.CompanyDTO;
import com.grupouno.iot.minero.models.Company;

@Component
public class CompanyMapper {
	 public static CompanyDTO toDto(Company entity) {
	        CompanyDTO dto = new CompanyDTO();
	        dto.setId(entity.getId());
	        dto.setName(entity.getName());
	        dto.setApiKey(entity.getApiKey());
	        dto.setActive(entity.isActive());
	        dto.setCreatedAt(entity.getCreatedAt());
	        dto.setUpdatedAt(entity.getUpdatedAt());
	        if (entity.getLocations() != null) {
	            dto.setLocationIds(entity.getLocations().stream()
	                .map(location -> location.getId())
	                .collect(Collectors.toList()));
	        }
	        
	        return dto;
	    }

	    public static Company toEntity(CompanyDTO dto) {
	        Company entity = new Company();
	        entity.setId(dto.getId());
	        entity.setName(dto.getName());
	        entity.setActive(dto.isActive());
	        entity.setCreatedAt(dto.getCreatedAt());
	        entity.setUpdatedAt(dto.getUpdatedAt());
	        return entity;
	    }
}
