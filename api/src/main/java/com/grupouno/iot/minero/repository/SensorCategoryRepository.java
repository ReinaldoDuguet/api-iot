package com.grupouno.iot.minero.repository;

import com.grupouno.iot.minero.models.SensorCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SensorCategoryRepository extends JpaRepository<SensorCategory, Long> {
    Optional<SensorCategory> findByName(String name);
    boolean existsByName(String name);

}
