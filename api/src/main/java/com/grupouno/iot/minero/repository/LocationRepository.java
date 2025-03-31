package com.grupouno.iot.minero.repository;

import com.grupouno.iot.minero.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}

