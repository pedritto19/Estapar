package com.estapar.parking.service;

import com.estapar.parking.model.dto.SpotStatusRequest;
import com.estapar.parking.model.dto.SpotStatusResponse;
import com.estapar.parking.model.entity.VehicleEntry;
import com.estapar.parking.repository.VehicleEntryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SpotStatusService {

    private final VehicleEntryRepository vehicleEntryRepository;

    public SpotStatusService(VehicleEntryRepository vehicleEntryRepository) {
        this.vehicleEntryRepository = vehicleEntryRepository;
    }

        public SpotStatusResponse getSpotStatus(SpotStatusRequest request) {
        Optional<VehicleEntry> veiculo = vehicleEntryRepository
            .findActiveBySpotLatLng(request.getLat(), request.getLng());

        SpotStatusResponse response = new SpotStatusResponse();

        if (veiculo.isPresent()) {
            VehicleEntry entry = veiculo.get();
            response.setOccupied(true);
            response.setLicensePlate(entry.getLicensePlate());
            response.setEntryTime(entry.getEntryTime());
            response.setTimeParked(entry.getParkedTime() != null ? entry.getParkedTime() : LocalDateTime.now());
            response.setPriceUntilNow(entry.getPricePaid() != null ? entry.getPricePaid() : 0.0);
        } else {
            response.setOccupied(false);
            response.setLicensePlate("");
            response.setEntryTime(null);
            response.setTimeParked(null);
            response.setPriceUntilNow(0.0);
        }

        return response;
    }


}
