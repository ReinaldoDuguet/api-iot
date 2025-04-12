package com.grupouno.iot.minero.controller;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import org.apache.kafka.common.errors.InvalidRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grupouno.iot.minero.dto.LocationDTO;
import com.grupouno.iot.minero.models.City;
import com.grupouno.iot.minero.services.CityService;
import com.grupouno.iot.minero.services.LocationService;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationController {

    private final LocationService locationService;
    private final CityService cityService;

    @Autowired
    public LocationController(LocationService locationService, CityService cityService) {
        this.locationService = locationService;
        this.cityService = cityService;
    }
    
    @GetMapping
    public List<LocationDTO> getAllLocations() {
        return locationService.getAllLocations();
    }

    @GetMapping("/{id}")
    public LocationDTO getLocationById(@PathVariable Long id) {
        return locationService.getLocationById(id);
    }
    
    @PostMapping
    public LocationDTO createLocation(@RequestBody LocationDTO locationDTO) {
        if (locationDTO.getCityId() != null) {
            City city = cityService.getCityById(locationDTO.getCityId());
            if (city != null) {
                locationDTO.setCityId(city.getId());
            } else {
                throw new EntityNotFoundException("City not found with id: " + locationDTO.getCityId());
            }
        } else {
            throw new InvalidRequestException("City ID is required for creating location.");
        }

        return locationService.createLocation(locationDTO);
    }

    @PutMapping("/{id}")
    public LocationDTO updateLocation(@PathVariable Long id, @RequestBody LocationDTO locationDTO) {
        if (locationDTO.getCityId() != null) {
            City city = cityService.getCityById(locationDTO.getCityId());
            if (city != null) {
                locationDTO.setCityId(city.getId());
            } else {
                throw new EntityNotFoundException("City not found with id: " + locationDTO.getCityId());
            }
        }

        return locationService.updateLocation(id, locationDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
    }
}

