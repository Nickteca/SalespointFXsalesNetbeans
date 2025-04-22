package com.salespointfxsales.www.repo;

import com.salespointfxsales.www.model.enums.NombreFolio;
import com.salespointfxsales.www.model.MovimientoInventario;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoInventarioRepo extends JpaRepository<MovimientoInventario, Integer>{
    List<MovimientoInventario> findBySucursalEstatusSucursalTrueAndCreatedAtBetweenAndNombreFolio(LocalDateTime startDate, LocalDateTime endDate, NombreFolio nombreFolio);
    List<MovimientoInventario> findBySucursalEstatusSucursalTrueAndCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}
