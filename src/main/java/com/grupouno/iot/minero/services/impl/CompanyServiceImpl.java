package com.grupouno.iot.minero.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grupouno.iot.minero.dto.CompanyDTO;
import com.grupouno.iot.minero.exceptions.EntityNotFoundException;
import com.grupouno.iot.minero.mappers.CompanyMapper;
import com.grupouno.iot.minero.models.Company;
import com.grupouno.iot.minero.repository.CompanyRepository;
import com.grupouno.iot.minero.services.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public CompanyDTO createCompany(CompanyDTO companyDTO) {
        Company company = CompanyMapper.toEntity(companyDTO);
        company = companyRepository.save(company);
        return CompanyMapper.toDto(company);
    }

    @Override
    public CompanyDTO getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));
        return CompanyMapper.toDto(company);
    }

    @Override
    public List<CompanyDTO> getAllCompanies() {
        return companyRepository.findAll().stream()
                .map(CompanyMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompanyDTO updateCompany(Long id, CompanyDTO companyDTO) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));
        company.setName(companyDTO.getName());
        company.setApiKey(companyDTO.getApiKey());
        company.setActive(companyDTO.isActive());
        company.setUpdatedAt(LocalDateTime.now());
        company = companyRepository.save(company);
        return CompanyMapper.toDto(company);
    }

    @Override
    public void deleteCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));
        companyRepository.delete(company);
    }
}
