package com.grupouno.iot.minero.services;

import java.util.List;

import com.grupouno.iot.minero.dto.CompanyDTO;

public interface CompanyService {

    List<CompanyDTO> getAllCompanies();
    CompanyDTO getCompanyById(Long id);
    CompanyDTO createCompany(CompanyDTO companyDTO);
    CompanyDTO updateCompany(Long id, CompanyDTO companyDTO);
    void deleteCompany(Long id);
}
