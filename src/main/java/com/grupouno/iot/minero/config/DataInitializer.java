package com.grupouno.iot.minero.config;

import com.grupouno.iot.minero.models.*;
import com.grupouno.iot.minero.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class DataInitializer {

    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final CompanyRepository companyRepository;
    private final LocationRepository locationRepository;
    private final SensorCategoryRepository categoryRepository;
    private final SensorRepository sensorRepository;

    public DataInitializer(
            CountryRepository countryRepository,
            CityRepository cityRepository,
            CompanyRepository companyRepository,
            LocationRepository locationRepository,
            SensorCategoryRepository categoryRepository,
            SensorRepository sensorRepository
    ) {
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
        this.companyRepository = companyRepository;
        this.locationRepository = locationRepository;
        this.categoryRepository = categoryRepository;
        this.sensorRepository = sensorRepository;
    }

    @PostConstruct
    public void initData() {

        if (sensorRepository.count() > 0) return;

        // País
        Country country = countryRepository.findByName("Chile")
                .orElseGet(() -> {
                    Country c = new Country();
                    c.setName("Chile");
                    c.setCreatedAt(LocalDateTime.now());
                    return countryRepository.save(c);
                });

        // Ciudad
        City city = cityRepository.findByName("Antofagasta")
                .orElseGet(() -> {
                    City ci = new City();
                    ci.setName("Antofagasta");
                    ci.setCountry(country);
                    ci.setCreatedAt(LocalDateTime.now());
                    return cityRepository.save(ci);
                });

        // Compañía
        Company company = companyRepository.findByApiKey("comp-12345678")
                .orElseGet(() -> {
                    Company co = new Company();
                    co.setName("Minera Prueba S.A.");
                    co.setApiKey("comp-12345678");
                    co.setActive(true);
                    co.setCreatedAt(LocalDateTime.now());
                    co.setUpdatedAt(LocalDateTime.now());
                    return companyRepository.save(co);
                });

        // Ubicación
        Location location = new Location();
        location.setName("Planta Norte");
        location.setCity(city);
        location.setCompany(company);
        location.setCreatedAt(LocalDateTime.now());
        location.setUpdatedAt(LocalDateTime.now());
        location.setMetadata(Map.of("area", "sur"));
        locationRepository.save(location);

        // Categoría de Sensor
        SensorCategory category = categoryRepository.findByName("Temperatura / Humedad")
                .orElseGet(() -> {
                    SensorCategory cat = new SensorCategory();
                    cat.setName("Temperatura / Humedad");
                    cat.setDescription("Sensores ambientales");
                    cat.setCreatedAt(LocalDateTime.now());
                    return categoryRepository.save(cat);
                });

        // Sensor
        Sensor sensor = new Sensor();
        sensor.setName("Sensor de Prueba");
        sensor.setApiKey("sensor-api-key-123");
        sensor.setCategory(category);
        sensor.setLocation(location);
        sensor.setMetadata(Map.of("modelo", "ESP32"));
        sensor.setActive(true);
        sensor.setCreatedAt(LocalDateTime.now());
        sensor.setUpdatedAt(LocalDateTime.now());
        sensorRepository.save(sensor);

        System.out.println("✅ Datos de prueba creados correctamente.");
    }
}
