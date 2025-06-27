package com.estapar.parking.controller;

import com.estapar.parking.model.dto.RevenueRequest;
import com.estapar.parking.model.dto.RevenueResponse;
import com.estapar.parking.service.RevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/revenue")
public class RevenueController {

    @Autowired
    private RevenueService revenueService;

    @GetMapping
    public RevenueResponse getRevenue(@RequestBody RevenueRequest request) {
        return revenueService.calculateRevenue(request);
    }
}
