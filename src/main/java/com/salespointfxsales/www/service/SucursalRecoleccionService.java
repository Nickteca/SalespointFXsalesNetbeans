package com.salespointfxsales.www.service;

import com.salespointfxsales.www.model.SucursalRecoleccion;
import com.salespointfxsales.www.model.enums.TipoMovimiento;
import com.salespointfxsales.www.repo.MovimientoCajaRepo;
import com.salespointfxsales.www.repo.SucursalRecoleccionRepo;
import com.salespointfxsales.www.repo.SucursalRepo;
import com.salespointfxsales.www.service.printer.PrinterRecoleccion;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SucursalRecoleccionService {

    private final SucursalRecoleccionRepo srr;
    private final SucursalRepo srepo;
    private final MovimientoCajaRepo mcr;
    private final PrinterRecoleccion pr;

    @Transactional
    public SucursalRecoleccion save(SucursalRecoleccion sr) throws Exception {
        try {
            if (mcr.findFirstBySucursalEstatusSucursalTrueOrderByIdMovimientoCajaDesc().getTipoMovimientoCaja().equals(TipoMovimiento.CIERRE)) {
                throw new IllegalStateException("La caja no está abierta.");
            }
            sr.setCreatedAt(LocalDateTime.now());
            sr.setSucursal(srepo.findByEstatusSucursalTrue().get());
            sr = srr.save(sr);
            pr.imprimirRecoleccion(sr);
            return sr;
        } catch (IllegalStateException e) {
            // Aquí capturamos el caso cuando no hay sucursal activa o la caja no está abierta
            System.err.println("Error: " + e.getMessage());
            throw e;  // Lanzamos la excepción para que el controller lo maneje
        } catch (Exception e) {
            // Captura cualquier otra excepción
            System.err.println("Error desconocido al guardar la venta: " + e.getMessage());
            throw e;
        }
    }
}
