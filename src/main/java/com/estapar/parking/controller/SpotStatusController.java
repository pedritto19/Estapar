package com.estapar.parking.controller;

import com.estapar.parking.model.dto.SpotStatusRequest;
import com.estapar.parking.model.dto.SpotStatusResponse;
import com.estapar.parking.service.SpotStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/spot-status")
public class SpotStatusController {

    @Autowired
    private SpotStatusService spotStatusService;

    @PostMapping
    public SpotStatusResponse checkSpotStatus(@RequestBody SpotStatusRequest request) {
        return spotStatusService.getSpotStatus(request);
    }
}
