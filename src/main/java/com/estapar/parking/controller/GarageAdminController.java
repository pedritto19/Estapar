package com.estapar.parking.controller;

import com.estapar.parking.service.GarageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class GarageAdminController {

    @Autowired
    private GarageService garageService;

    @PostMapping("/import-garage")
    public String importar() {
        garageService.importarConfiguracaoDaGaragem();
        return "Importação realizada com sucesso.";
    }
}
