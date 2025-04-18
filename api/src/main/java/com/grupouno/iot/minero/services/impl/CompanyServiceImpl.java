package com.grupouno.iot.minero.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
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
import com.grupouno.iot.minero.exceptions.BadRequestException;
import org.springframework.http.HttpStatus;


@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final LocationRepository locationRepository;
    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository, LocationRepository locationRepository) {
		this.locationRepository = locationRepository;
        this.companyRepository = companyRepository;
    }

    private String generateCompanyApiKey() {
        return "company_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    
    @Override
    public CompanyDTO createCompany(CompanyDTO companyDTO) {
        Company company = CompanyMapper.toEntity(companyDTO);
        
        company.setApiKey(generateCompanyApiKey());
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

        if (companyDTO.getApiKey() != null && !companyDTO.getApiKey().equals(existingCompany.getApiKey())) {
            throw new BadRequestException("No se permite modificar el API Key de una compañía.");
        }

        if (companyDTO.getName() != null && !companyDTO.getName().equals(existingCompany.getName())) {
            existingCompany.setName(companyDTO.getName());
        }

        existingCompany.setUpdatedAt(LocalDateTime.now());

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
