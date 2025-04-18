package com.grupouno.iot.minero.config;

import com.grupouno.iot.minero.models.*;
import com.grupouno.iot.minero.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

// Inicializa data para testing de consumidor kafka

@Component
public class DataInitializer {

    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final CompanyRepository companyRepository;
    private final LocationRepository locationRepository;
    private final SensorCategoryRepository categoryRepository;
    private final SensorRepository sensorRepository;
    private final RoleRepository roleRepository;


    public DataInitializer(
            CountryRepository countryRepository,
            CityRepository cityRepository,
            CompanyRepository companyRepository,
            LocationRepository locationRepository,
            SensorCategoryRepository categoryRepository,
            SensorRepository sensorRepository,
            RoleRepository roleRepository
    ) {
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
        this.companyRepository = companyRepository;
        this.locationRepository = locationRepository;
        this.categoryRepository = categoryRepository;
        this.sensorRepository = sensorRepository;
        this.roleRepository = roleRepository;

    }

    private void createRoleIfNotExists(String roleName) {
        roleRepository.findByName(roleName).orElseGet(() -> {
            Role role = new Role();
            role.setName(roleName);
            role.setCreatedAt(LocalDateTime.now());
            return roleRepository.save(role);
        });
    }

    @PostConstruct
    public void initData() {

        // Crear roles base si no existen
        createRoleIfNotExists("ADMIN");
        createRoleIfNotExists("USER");

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


        // Sensor zigbee mqtt Simulado para test
        Sensor sensorMqtt = new Sensor();
        sensorMqtt.setName("Sensor zigbee mqtt test");
        sensorMqtt.setApiKey("sensor-zigbee-mqtt-key-15464813J57L");
        sensorMqtt.setCategory(category);
        sensorMqtt.setLocation(location);
        sensorMqtt.setMetadata(Map.of("modelo", "Zigbee"));
        sensorMqtt.setActive(true);
        sensorMqtt.setCreatedAt(LocalDateTime.now());
        sensorMqtt.setUpdatedAt(LocalDateTime.now());
        sensorRepository.save(sensorMqtt);

        // Sensor ESP32 tcp Simulado para test
        Sensor sensorTcp = new Sensor();
        sensorTcp.setName("Sensor esp32 tcp test");
        sensorTcp.setApiKey("sensor-esp32-tcp-key-519494H1494948T");
        sensorTcp.setCategory(category);
        sensorTcp.setLocation(location);
        sensorTcp.setMetadata(Map.of("modelo", "ESP32"));
        sensorTcp.setActive(true);
        sensorTcp.setCreatedAt(LocalDateTime.now());
        sensorTcp.setUpdatedAt(LocalDateTime.now());
        sensorRepository.save(sensorTcp);

        // Sensor zigbee mqtt Simulado para test
        Sensor sensorKafka = new Sensor();
        sensorKafka.setName("Sensor kafka test prod");
        sensorKafka.setApiKey("82ba1908-96c7-4a7b-854c-969a5e389909");
        sensorKafka.setCategory(category);
        sensorKafka.setLocation(location);
        sensorKafka.setMetadata(Map.of("modelo", "4.0_ind"));
        sensorKafka.setActive(true);
        sensorKafka.setCreatedAt(LocalDateTime.now());
        sensorKafka.setUpdatedAt(LocalDateTime.now());
        sensorRepository.save(sensorKafka);

        System.out.println("✅ Datos de prueba creados correctamente.");
    }
}
