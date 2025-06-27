package com.estapar.parking.model.dto;

import java.time.LocalDateTime;

public class SpotStatusResponse {
    private boolean occupied;
    private String licensePlate;
    private double priceUntilNow;
    private LocalDateTime entryTime;
    private LocalDateTime timeParked;

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public double getPriceUntilNow() {
        return priceUntilNow;
    }

    public void setPriceUntilNow(double priceUntilNow) {
        this.priceUntilNow = priceUntilNow;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public LocalDateTime getTimeParked() {
        return timeParked;
    }

    public void setTimeParked(LocalDateTime timeParked) {
        this.timeParked = timeParked;
    }
}
