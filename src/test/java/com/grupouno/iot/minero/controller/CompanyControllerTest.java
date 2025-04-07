package com.grupouno.iot.minero.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupouno.iot.minero.dto.CompanyDTO;
import com.grupouno.iot.minero.exceptions.EntityNotFoundException;
import com.grupouno.iot.minero.services.CompanyService;


@ExtendWith(MockitoExtension.class)
class CompanyControllerTest {

	private MockMvc mockMvc;

	@Mock
	private CompanyService companyService;

	@InjectMocks
	private CompanyController companyController;

    private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(companyController).build();
	}
	
	@Test
	void createCompany_ShouldReturnCreatedCompany() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setId(1L);
		companyDTO.setName("Test Company");

		when(companyService.createCompany(any())).thenReturn(companyDTO);

		mockMvc.perform(post("/api/v1/companies").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(companyDTO))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.name").value("Test Company"));
	}

	@Test
	void getCompanyById_ShouldReturnCompany() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setId(1L);
		companyDTO.setName("Test Company");

		when(companyService.getCompanyById(1L)).thenReturn(companyDTO);

		mockMvc.perform(get("/api/v1/companies/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L))
				.andExpect(jsonPath("$.name").value("Test Company"));
	}

	@Test
	void getAllCompanies_ShouldReturnListOfCompanies() throws Exception {
		CompanyDTO company1 = new CompanyDTO();
		company1.setId(1L);
		company1.setName("Company 1");

		CompanyDTO company2 = new CompanyDTO();
		company2.setId(2L);
		company2.setName("Company 2");

		List<CompanyDTO> companies = Arrays.asList(company1, company2);
		when(companyService.getAllCompanies()).thenReturn(companies);

		mockMvc.perform(get("/api/v1/companies")).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(2));
	}

	@Test
	void updateCompany_ShouldReturnUpdatedCompany() throws Exception {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setId(1L);
		companyDTO.setName("Updated Company");

		when(companyService.updateCompany(eq(1L), any())).thenReturn(companyDTO);

		mockMvc.perform(put("/api/v1/companies/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(companyDTO))).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.name").value("Updated Company"));
	}

	@Test
	void deleteCompany_ShouldReturnSuccessMessage_WhenCompanyExists() throws Exception {
		doNothing().when(companyService).deleteCompany(1L);

		mockMvc.perform(delete("/api/v1/companies/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("Se ha eliminado la entidad con id 1"));
	}

	@Test
	void deleteCompany_ShouldReturnNotFound_WhenCompanyDoesNotExist() throws Exception {
		doThrow(new EntityNotFoundException("La compañía con id 1 no fue encontrada."))
				.when(companyService).deleteCompany(1L);

		mockMvc.perform(delete("/api/v1/companies/1"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("La compañía con id 1 no fue encontrada."));
	}
}
