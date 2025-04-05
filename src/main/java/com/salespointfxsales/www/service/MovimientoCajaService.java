package com.salespointfxsales.www.service;

import com.salespointfxsales.www.model.MovimientoCaja;
import com.salespointfxsales.www.repo.MovimientoCajaRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovimientoCajaService {

    private final MovimientoCajaRepo mcr;

    public MovimientoCaja findlastmovimientoCajasucursalActiva() {
        return mcr.findFirstBySucursalEstatusSucursalTrueOrderByIdMovimientoCajaDesc();
    }

}
