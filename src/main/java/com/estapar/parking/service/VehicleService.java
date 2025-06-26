package com.estapar.parking.service;

import com.estapar.parking.model.dto.WebhookEventDTO;
import com.estapar.parking.model.entity.Sector;
import com.estapar.parking.model.entity.Spot;
import com.estapar.parking.model.entity.VehicleEntry;
import com.estapar.parking.repository.SectorRepository;
import com.estapar.parking.repository.SpotRepository;
import com.estapar.parking.repository.VehicleEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class VehicleService {

    @Autowired private VehicleEntryRepository vehicleEntryRepo;
    @Autowired private SpotRepository spotRepo;
    @Autowired private SectorRepository sectorRepo;

    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    public void processEvent(WebhookEventDTO event) {
        switch (event.getEvent_type()) {
            case "ENTRY" -> handleEntry(event);
            case "PARKED" -> handleParked(event);
            case "EXIT" -> handleExit(event);
        }
    }

    private void handleEntry(WebhookEventDTO event) {
        String plate = event.getLicense_plate();
        LocalDateTime entryTime = LocalDateTime.parse(event.getEntry_time(), formatter);

        // Encontrar o setor com vagas disponíveis
        Sector setorEscolhido = sectorRepo.findAll().stream()
                .filter(this::temVagasDisponiveis)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Nenhum setor disponível"));

        // Encontrar um spot livre dentro do setor
        Optional<Spot> optionalSpot = spotRepo.findAll().stream()
                .filter(s -> s.getSector().getName().equals(setorEscolhido.getName()) && !s.isOccupied())
                .findFirst();

        if (optionalSpot.isEmpty()) {
            throw new RuntimeException("Nenhum spot livre encontrado no setor selecionado");
        }

        Spot spot = optionalSpot.get();
        spot.setOccupied(true);
        spotRepo.save(spot);

        double precoBase = setorEscolhido.getBasePrice();
        double ocupacao = calcularOcupacao(setorEscolhido);
        double precoFinal = calcularPrecoDinamico(precoBase, ocupacao);

        VehicleEntry ve = new VehicleEntry();
        ve.setLicensePlate(plate);
        ve.setEntryTime(entryTime);
        ve.setPricePaid(precoFinal);
        ve.setSpot(spot);

        vehicleEntryRepo.save(ve);
    }


    private void handleParked(WebhookEventDTO event) {
        String plate = event.getLicense_plate();
        Double lat = event.getLat();
        Double lng = event.getLng();

        Spot spot = spotRepo.findByLatAndLng(lat, lng);
        spot.setOccupied(true);
        spotRepo.save(spot);

        Optional<VehicleEntry> optional = vehicleEntryRepo.findFirstByLicensePlateAndExitTimeIsNullOrderByEntryTimeDesc(plate);
        if (optional.isPresent()) {
            VehicleEntry ve = optional.get();
            ve.setSpot(spot);
            ve.setParkedTime(LocalDateTime.now());
            vehicleEntryRepo.save(ve);
        }
    }

    private void handleExit(WebhookEventDTO event) {
        String plate = event.getLicense_plate();
        LocalDateTime exitTime = LocalDateTime.parse(event.getExit_time(), formatter);

        Optional<VehicleEntry> optional = vehicleEntryRepo.findFirstByLicensePlateAndExitTimeIsNullOrderByEntryTimeDesc(plate);
        if (optional.isPresent()) {
            VehicleEntry ve = optional.get();
            ve.setExitTime(exitTime);
            vehicleEntryRepo.save(ve);

            Spot spot = ve.getSpot();
            if (spot != null) {
                spot.setOccupied(false);
                spotRepo.save(spot);
            }
        }
    }

    private boolean temVagasDisponiveis(Sector sector) {
        long ocupadas = spotRepo.findAll().stream()
                .filter(s -> s.getSector().getName().equals(sector.getName()) && s.isOccupied())
                .count();
        return ocupadas < sector.getMaxCapacity();
    }

    private double calcularOcupacao(Sector sector) {
        long total = sector.getMaxCapacity();
        long ocupadas = spotRepo.findAll().stream()
                .filter(s -> s.getSector().getName().equals(sector.getName()) && s.isOccupied())
                .count();
        return (double) ocupadas / total;
    }

    private double calcularPrecoDinamico(double base, double ocupacao) {
        if (ocupacao < 0.25) return base * 0.9;
        if (ocupacao <= 0.5) return base;
        if (ocupacao <= 0.75) return base * 1.1;
        return base * 1.25;
    }
}
