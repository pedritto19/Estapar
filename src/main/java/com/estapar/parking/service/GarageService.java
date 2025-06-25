package com.estapar.parking.service;

import com.estapar.parking.model.entity.Sector;
import com.estapar.parking.model.entity.Spot;
import com.estapar.parking.repository.SectorRepository;
import com.estapar.parking.repository.SpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GarageService {

   private final String GARAGE_URL = "http://localhost:3000/garage";

    @Autowired
    private SectorRepository sectorRepository;

    @Autowired
    private SpotRepository spotRepository;

    public void importarConfiguracaoDaGaragem() {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> resposta = restTemplate.getForObject(GARAGE_URL, Map.class);

        List<Map<String, Object>> setores = (List<Map<String, Object>>) resposta.get("garage");
        List<Map<String, Object>> vagas = (List<Map<String, Object>>) resposta.get("spots");

        List<Sector> setoresSalvos = new ArrayList<>();
        for (Map<String, Object> setorJson : setores) {
            Sector s = new Sector();
            s.setName((String) setorJson.get("sector"));
            s.setBasePrice(Double.parseDouble(setorJson.get("base_price").toString()));
            s.setMaxCapacity((int) setorJson.get("max_capacity"));
            s.setOpenHour(LocalTime.parse((String) setorJson.get("open_hour")));
            s.setCloseHour(LocalTime.parse((String) setorJson.get("close_hour")));
            s.setDurationLimitMinutes((int) setorJson.get("duration_limit_minutes"));
            setoresSalvos.add(s);
        }

        sectorRepository.saveAll(setoresSalvos);

        List<Spot> spotsSalvos = new ArrayList<>();
        for (Map<String, Object> vagaJson : vagas) {
            Spot sp = new Spot();
            sp.setLat((double) vagaJson.get("lat"));
            sp.setLng((double) vagaJson.get("lng"));
            String setorNome = (String) vagaJson.get("sector");

            // Relaciona o setor correspondente
            setoresSalvos.stream()
                    .filter(s -> s.getName().equals(setorNome))
                    .findFirst()
                    .ifPresent(sp::setSector);

            spotsSalvos.add(sp);
        }

        spotRepository.saveAll(spotsSalvos);
    }
}
