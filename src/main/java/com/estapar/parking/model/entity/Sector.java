package com.estapar.parking.model.entity;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
public class Sector {

    @Id
    private String name;

    private double basePrice;
    private int maxCapacity;

    private LocalTime openHour;
    private LocalTime closeHour;

    private int durationLimitMinutes;

    // Getters e Setters
}
