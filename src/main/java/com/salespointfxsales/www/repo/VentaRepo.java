package com.salespointfxsales.www.repo;

import com.salespointfxsales.www.model.Venta;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaRepo extends JpaRepository<Venta, Integer>{
    List<Venta> findBySucursalEstatusSucursalTrueAndCreatedAtBetween(LocalDateTime fechaapertura, LocalDateTime fechaVenta);
}
