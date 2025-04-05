package com.salespointfxsales.www.repo;

import com.salespointfxsales.www.model.MovimientoCaja;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoCajaRepo extends JpaRepository<MovimientoCaja, Integer> {
    MovimientoCaja findFirstBySucursalEstatusSucursalTrueOrderByIdMovimientoCajaDesc();
}
