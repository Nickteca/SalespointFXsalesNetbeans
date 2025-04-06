package com.salespointfxsales.www.service;

import com.salespointfxsales.www.model.MovimientoCaja;
import com.salespointfxsales.www.model.enums.TipoMovimiento;
import com.salespointfxsales.www.repo.MovimientoCajaRepo;
import com.salespointfxsales.www.repo.SucursalRepo;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovimientoCajaService {

    private final MovimientoCajaRepo mcr;
    private final SucursalRepo sr;

    public MovimientoCaja findlastmovimientoCajasucursalActiva() {
        return mcr.findFirstBySucursalEstatusSucursalTrueOrderByIdMovimientoCajaDesc();
    }

    public MovimientoCaja saveMovimiento(TipoMovimiento tipo, float saldoApertura, float saldoCierre) {
        if (tipo == TipoMovimiento.APERTURA && saldoApertura <= 0) {
            throw new IllegalArgumentException("El saldo de apertura debe ser mayor que 0.");
        }

        if (tipo == TipoMovimiento.CIERRE && saldoCierre < 0) {
            throw new IllegalArgumentException("El saldo de cierre no puede ser negativo.");
        }

        MovimientoCaja mc = new MovimientoCaja(
                null,
                tipo,
                saldoApertura,
                saldoCierre,
                LocalDateTime.now()
        );

        mc.setSucursal(sr.findByEstatusSucursalTrue()
                .orElseThrow(() -> new IllegalStateException("No hay sucursal activa.")));

        return mcr.save(mc);
    }
}
