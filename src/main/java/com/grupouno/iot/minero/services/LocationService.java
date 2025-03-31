package com.grupouno.iot.minero.services;

import com.grupouno.iot.minero.dto.LocationDTO;
import java.util.List;

public interface LocationService {
    List<LocationDTO> getAllLocations();
    LocationDTO getLocationById(Long id);
    LocationDTO createLocation(LocationDTO locationDTO);
    LocationDTO updateLocation(Long id, LocationDTO locationDTO);
    void deleteLocation(Long id);
}
