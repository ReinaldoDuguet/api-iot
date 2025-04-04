package com.grupouno.iot.minero.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grupouno.iot.minero.dto.CompanyDTO;
import com.grupouno.iot.minero.exceptions.ApiKeyAlreadyExistsException;
import com.grupouno.iot.minero.exceptions.EntityNotFoundException;
import com.grupouno.iot.minero.mappers.CompanyMapper;
import com.grupouno.iot.minero.models.Company;
import com.grupouno.iot.minero.models.Location;
import com.grupouno.iot.minero.repository.CompanyRepository;
import com.grupouno.iot.minero.repository.LocationRepository;
import com.grupouno.iot.minero.services.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final LocationRepository locationRepository;
    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository, LocationRepository locationRepository) {
		this.locationRepository = locationRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public CompanyDTO createCompany(CompanyDTO companyDTO) {
    	
        if (companyRepository.existsByApiKey(companyDTO.getApiKey())) {
            throw new ApiKeyAlreadyExistsException("La api_key ya está en uso.");
        }
        Company company = CompanyMapper.toEntity(companyDTO);
        company.setCreatedAt(LocalDateTime.now());
        company.setUpdatedAt(LocalDateTime.now());

        if (companyDTO.getLocationIds() != null && !companyDTO.getLocationIds().isEmpty()) {
            List<Location> locations = locationRepository.findAllById(companyDTO.getLocationIds());
            company.setLocations(locations);
        }

        Company savedCompany = companyRepository.save(company);
        return CompanyMapper.toDto(savedCompany);
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
        Company existingCompany = companyRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + id));

        if (companyDTO.getName() != null && !companyDTO.getName().equals(existingCompany.getName())) {
            existingCompany.setName(companyDTO.getName());
        }

        if (companyDTO.getApiKey() != null && !companyDTO.getApiKey().equals(existingCompany.getApiKey())) {
            existingCompany.setApiKey(companyDTO.getApiKey());
        }

        Company updatedCompany = companyRepository.save(existingCompany);
        return CompanyMapper.toDto(updatedCompany);
    }

    @Override
    public void deleteCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));
        companyRepository.delete(company);
    }
}
