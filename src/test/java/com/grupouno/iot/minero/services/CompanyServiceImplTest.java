package com.grupouno.iot.minero.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.grupouno.iot.minero.dto.CompanyDTO;
import com.grupouno.iot.minero.exceptions.EntityNotFoundException;
import com.grupouno.iot.minero.mappers.CompanyMapper;
import com.grupouno.iot.minero.models.Company;
import com.grupouno.iot.minero.repository.CompanyRepository;
import com.grupouno.iot.minero.services.impl.CompanyServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyServiceImpl companyService;

    private Company company;
    private CompanyDTO companyDTO;

    @BeforeEach
    void setUp() {
        company = new Company();
        company.setId(1L);
        company.setName("Test Company");
        company.setApiKey("123456");
        company.setActive(true);
        company.setCreatedAt(LocalDateTime.now());
        company.setUpdatedAt(LocalDateTime.now());

        companyDTO = CompanyMapper.toDto(company);
    }

    @Test
    void createCompany_ShouldReturnCreatedCompany() {
        when(companyRepository.save(any(Company.class))).thenReturn(company);
        
        CompanyDTO result = companyService.createCompany(companyDTO);
        
        assertNotNull(result);
        assertEquals(companyDTO.getId(), result.getId());
        assertEquals(companyDTO.getName(), result.getName());
        verify(companyRepository, times(1)).save(any(Company.class));
    }

    @Test
    void getCompanyById_ShouldReturnCompany_WhenFound() {
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        
        CompanyDTO result = companyService.getCompanyById(1L);
        
        assertNotNull(result);
        assertEquals(companyDTO.getId(), result.getId());
        verify(companyRepository, times(1)).findById(1L);
    }

    @Test
    void getCompanyById_ShouldThrowException_WhenNotFound() {
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(EntityNotFoundException.class, () -> companyService.getCompanyById(1L));
        verify(companyRepository, times(1)).findById(1L);
    }

    @Test
    void getAllCompanies_ShouldReturnListOfCompanies() {
        when(companyRepository.findAll()).thenReturn(List.of(company));
        
        List<CompanyDTO> result = companyService.getAllCompanies();
        
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(companyRepository, times(1)).findAll();
    }

    @Test
    void updateCompany_ShouldReturnUpdatedCompany_WhenFound() {
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(companyRepository.save(any(Company.class))).thenReturn(company);
        
        CompanyDTO updatedDTO = new CompanyDTO();
        updatedDTO.setName("Updated Name");
        updatedDTO.setApiKey("654321");
        updatedDTO.setActive(false);
        
        CompanyDTO result = companyService.updateCompany(1L, updatedDTO);
        
        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        verify(companyRepository, times(1)).findById(1L);
        verify(companyRepository, times(1)).save(any(Company.class));
    }

    @Test
    void updateCompany_ShouldThrowException_WhenNotFound() {
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(EntityNotFoundException.class, () -> companyService.updateCompany(1L, companyDTO));
        verify(companyRepository, times(1)).findById(1L);
    }

    @Test
    void deleteCompany_ShouldDelete_WhenFound() {
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        doNothing().when(companyRepository).delete(company);
        
        companyService.deleteCompany(1L);
        
        verify(companyRepository, times(1)).findById(1L);
        verify(companyRepository, times(1)).delete(company);
    }

    @Test
    void deleteCompany_ShouldThrowException_WhenNotFound() {
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(EntityNotFoundException.class, () -> companyService.deleteCompany(1L));
        verify(companyRepository, times(1)).findById(1L);
    }
}
