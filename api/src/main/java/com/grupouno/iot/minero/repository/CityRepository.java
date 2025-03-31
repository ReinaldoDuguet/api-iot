package com.grupouno.iot.minero.repository;

import com.grupouno.iot.minero.models.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findByName(String name);
}
