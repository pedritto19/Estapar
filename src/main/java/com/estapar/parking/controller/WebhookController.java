package com.estapar.parking.controller;

import com.estapar.parking.model.dto.WebhookEventDTO;
import com.estapar.parking.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping
    public void receberEvento(@RequestBody WebhookEventDTO event) {
        vehicleService.processEvent(event);
    }
}
