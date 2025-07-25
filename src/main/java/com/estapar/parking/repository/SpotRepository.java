package com.estapar.parking.repository;

import com.estapar.parking.model.entity.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpotRepository extends JpaRepository<Spot, Long> {

    Spot findByLatAndLng(double lat, double lng);

}
