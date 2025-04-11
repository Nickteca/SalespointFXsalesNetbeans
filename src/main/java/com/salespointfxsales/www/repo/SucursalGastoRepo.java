package com.salespointfxsales.www.repo;

import com.salespointfxsales.www.model.SucursalGasto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SucursalGastoRepo extends JpaRepository<SucursalGasto, Integer>{
   List<SucursalGasto> findBySucursalEstatusSucursalTrueAndCreatedAtBetween(LocalDateTime inicio, LocalDateTime fin);
}
