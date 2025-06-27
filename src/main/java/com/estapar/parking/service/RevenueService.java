package com.estapar.parking.service;

import com.estapar.parking.model.dto.RevenueRequest;
import com.estapar.parking.model.dto.RevenueResponse;
import com.estapar.parking.repository.VehicleEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class RevenueService {

    private final VehicleEntryRepository vehicleEntryRepository;

    @Autowired
    public RevenueService(VehicleEntryRepository vehicleEntryRepository) {
        this.vehicleEntryRepository = vehicleEntryRepository;
    }

    public RevenueResponse calculateRevenue(RevenueRequest request) {
        String date = request.getDate(); // formato "yyyy-MM-dd"
        String sector = request.getSector();

        Double total = vehicleEntryRepository.calculateTotalRevenueByDateAndSector(date, sector);
        if (total == null) total = 0.0;

        RevenueResponse response = new RevenueResponse();
        response.setAmount(Math.round(total * 100.0) / 100.0);
        response.setCurrency("BRL");
        response.setTimestamp(LocalDate.parse(date, DateTimeFormatter.ISO_DATE).atStartOfDay().toString());

        return response;
    }
}
