package com.salespointfxsales.www.service;

import com.salespointfxsales.www.model.SucursalGasto;
import com.salespointfxsales.www.model.enums.TipoMovimiento;
import com.salespointfxsales.www.repo.MovimientoCajaRepo;
import com.salespointfxsales.www.repo.SucursalGastoRepo;
import com.salespointfxsales.www.repo.SucursalRepo;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SucursalGastoService {

    private final SucursalGastoRepo sgr;
    private final MovimientoCajaRepo mcr;
    private final SucursalRepo sr;

    public SucursalGasto save(SucursalGasto sg) {
        try {
            if (mcr.findFirstBySucursalEstatusSucursalTrueOrderByIdMovimientoCajaDesc().getTipoMovimientoCaja().equals(TipoMovimiento.CIERRE)) {
                throw new IllegalStateException("La caja no está abierta.");
            }
            sg.setCreatedAt(LocalDateTime.now());
            sg.setSucursal(sr.findByEstatusSucursalTrue().get());
            return sgr.save(sg);
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    public List<SucursalGasto> findBySucursalEstatusSucursalTrueAndCreatedAtBetween(LocalDate inicio, LocalDate fin) {
        return sgr.findBySucursalEstatusSucursalTrueAndCreatedAtBetween(inicio.atStartOfDay(), fin.atTime(11, 59, 59));
    }
}
