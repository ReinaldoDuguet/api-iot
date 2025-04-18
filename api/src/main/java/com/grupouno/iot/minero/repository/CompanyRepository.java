package com.grupouno.iot.minero.repository;

import com.grupouno.iot.minero.dto.CompanyDTO;
import com.grupouno.iot.minero.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByApiKey(String apiKey);
    boolean existsByApiKey(String apiKey);
	Object save(CompanyDTO company);
}
