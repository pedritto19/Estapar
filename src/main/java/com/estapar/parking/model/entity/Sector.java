package com.estapar.parking.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalTime;

@Entity
public class Sector {

    @Id
    private String name; // Ex: "A", "B", "C"

    private double basePrice;
    private int maxCapacity;
    private LocalTime openHour;
    private LocalTime closeHour;
    private int durationLimitMinutes;

    // Getters e Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public LocalTime getOpenHour() {
        return openHour;
    }

    public void setOpenHour(LocalTime openHour) {
        this.openHour = openHour;
    }

    public LocalTime getCloseHour() {
        return closeHour;
    }

    public void setCloseHour(LocalTime closeHour) {
        this.closeHour = closeHour;
    }

    public int getDurationLimitMinutes() {
        return durationLimitMinutes;
    }

    public void setDurationLimitMinutes(int durationLimitMinutes) {
        this.durationLimitMinutes = durationLimitMinutes;
    }
}
