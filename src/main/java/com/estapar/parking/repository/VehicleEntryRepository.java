package com.estapar.parking.repository;

import com.estapar.parking.model.entity.VehicleEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleEntryRepository extends JpaRepository<VehicleEntry, Long> {

    Optional<VehicleEntry> findFirstByLicensePlateAndExitTimeIsNullOrderByEntryTimeDesc(String licensePlate);

}
