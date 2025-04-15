package com.salespointfxsales.www.repo;

import com.salespointfxsales.www.model.SucursalRecoleccion;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SucursalRecoleccionRepo extends JpaRepository<SucursalRecoleccion, Integer>{
    List<SucursalRecoleccion> findBySucursalEstatusSucursalTrueAndCreatedAtBetween(LocalDateTime inicio, LocalDateTime fin);
}
