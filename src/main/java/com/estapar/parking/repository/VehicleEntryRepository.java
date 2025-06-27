package com.estapar.parking.repository;

import com.estapar.parking.model.entity.VehicleEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleEntryRepository extends JpaRepository<VehicleEntry, Long> {

    // Para o endpoint /plate-status
    Optional<VehicleEntry> findFirstByLicensePlateAndExitTimeIsNullOrderByEntryTimeDesc(String licensePlate);

    // Para o endpoint /spot-status (usando JOIN com spot)
    @Query("SELECT ve FROM VehicleEntry ve WHERE ve.spot.lat = :lat AND ve.spot.lng = :lng AND ve.exitTime IS NULL ORDER BY ve.entryTime DESC")
    Optional<VehicleEntry> findActiveBySpotLatLng(@Param("lat") Double lat, @Param("lng") Double lng);

    @Query("SELECT SUM(ve.pricePaid) FROM VehicleEntry ve " +
       "WHERE FUNCTION('DATE', ve.exitTime) = :exitDate " +
       "AND ve.spot.sector.name = :sector")
    Double calculateTotalRevenueByDateAndSector(@Param("exitDate") String exitDate,
                                                @Param("sector") String sector);

}
