package com.grupouno.iot.minero.mappers;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.grupouno.iot.minero.dto.CompanyDTO;
import com.grupouno.iot.minero.models.Company;

public class CompanyMapperTest {

    @Test
    void shouldMapEntityToDto() {
        Company company = new Company();
        company.setId(1L);
        company.setName("Test Company");
        company.setApiKey("12345");
        company.setActive(true);
        company.setCreatedAt(LocalDateTime.now());
        company.setUpdatedAt(LocalDateTime.now());

        CompanyDTO dto = CompanyMapper.toDto(company);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(company.getId());
        assertThat(dto.getName()).isEqualTo(company.getName());
        assertThat(dto.getApiKey()).isEqualTo(company.getApiKey());
        assertThat(dto.isActive()).isEqualTo(company.isActive());
        assertThat(dto.getCreatedAt()).isEqualTo(company.getCreatedAt());
        assertThat(dto.getUpdatedAt()).isEqualTo(company.getUpdatedAt());
    }

    @Test
    void shouldMapDtoToEntity() {
        CompanyDTO dto = new CompanyDTO();
        dto.setId(1L);
        dto.setName("Test Company");
        dto.setApiKey("12345");
        dto.setActive(true);
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());

        Company entity = CompanyMapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(dto.getId());
        assertThat(entity.getName()).isEqualTo(dto.getName());
        assertThat(entity.getApiKey()).isEqualTo(dto.getApiKey());
        assertThat(entity.isActive()).isEqualTo(dto.isActive());
        assertThat(entity.getCreatedAt()).isEqualTo(dto.getCreatedAt());
        assertThat(entity.getUpdatedAt()).isEqualTo(dto.getUpdatedAt());
    }
}
