package com.estapar.parking.controller;

import com.estapar.parking.model.dto.WebhookEventDTO;
import com.estapar.parking.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<Void> receberEvento(@RequestBody WebhookEventDTO event) {
        System.out.println("===> Webhook recebido: " + event.getEvent_type() + " | " + event.getLicense_plate());
        vehicleService.processEvent(event);
        return ResponseEntity.ok().build();
    }
}

