package com.grupouno.iot.minero.services.impl;

import com.grupouno.iot.minero.dto.LocationDTO;
import com.grupouno.iot.minero.exceptions.EntityNotFoundException;
import com.grupouno.iot.minero.mappers.LocationMapper;
import com.grupouno.iot.minero.models.City;
import com.grupouno.iot.minero.models.Company;
import com.grupouno.iot.minero.models.Location;
import com.grupouno.iot.minero.repository.CityRepository;
import com.grupouno.iot.minero.repository.CompanyRepository;
import com.grupouno.iot.minero.repository.LocationRepository;
import com.grupouno.iot.minero.services.LocationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationServiceImpl implements LocationService {

        private final LocationRepository locationRepository;
        private final CompanyRepository companyRepository;
        private final CityRepository cityRepository;

        @Autowired
        public LocationServiceImpl(LocationRepository locationRepository,
                        CompanyRepository companyRepository,
                        CityRepository cityRepository) {
                this.locationRepository = locationRepository;
                this.companyRepository = companyRepository;
                this.cityRepository = cityRepository;
        }

        @Override
        public List<LocationDTO> getAllLocations() {
                return locationRepository.findAll().stream()
                                .map(LocationMapper::toDto)
                                .collect(Collectors.toList());
        }

        @Override
        public LocationDTO getLocationById(Long id) {
                Location location = locationRepository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException("Location not found"));
                return LocationMapper.toDto(location);
        }

        @Override
        public LocationDTO createLocation(LocationDTO dto) {
            Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + dto.getCompanyId()));

            City city = cityRepository.findById(dto.getCityId())
                .orElseThrow(() -> new EntityNotFoundException("City not found with id: " + dto.getCityId()));

            Location location = LocationMapper.toEntity(dto, company, city);
            location = locationRepository.save(location);
            return LocationMapper.toDto(location);
        }


        @Override
        public LocationDTO updateLocation(Long id, LocationDTO dto) {
            Location location = locationRepository.findById(id)
                    .orElseThrow(() -> new com.grupouno.iot.minero.exceptions.EntityNotFoundException("Location not found with id: " + id));

            if (dto.getName() != null) {
                location.setName(dto.getName());
            }

            if (dto.getMetadata() != null) {
                location.setMetadata(dto.getMetadata());
            }

            if (dto.getCompanyId() != null) {
                Company company = companyRepository.findById(dto.getCompanyId())
                        .orElseThrow(() -> new com.grupouno.iot.minero.exceptions.EntityNotFoundException("Company not found with id: " + dto.getCompanyId()));
                location.setCompany(company);
            }

            if (dto.getCityId() != null) {
                City city = cityRepository.findById(dto.getCityId())
                        .orElseThrow(() -> new com.grupouno.iot.minero.exceptions.EntityNotFoundException("City not found with id: " + dto.getCityId()));
                location.setCity(city);
            }

            location.setUpdatedAt(LocalDateTime.now());
            location = locationRepository.save(location);

            return LocationMapper.toDto(location);
        }


        @Override
        public void deleteLocation(Long id) {
                Location location = locationRepository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException("Location not found"));
                locationRepository.delete(location);
        }
}
