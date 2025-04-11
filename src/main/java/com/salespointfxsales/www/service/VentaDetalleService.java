package com.salespointfxsales.www.service;

import com.salespointfxsales.www.model.MovimientoCaja;
import com.salespointfxsales.www.model.VentaDetalle;
import com.salespointfxsales.www.model.enums.TipoMovimiento;
import com.salespointfxsales.www.repo.MovimientoCajaRepo;
import com.salespointfxsales.www.repo.VentaDetalleRepo;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VentaDetalleService {
    private final VentaDetalleRepo vdr;
private final MovimientoCajaRepo mcr;    
    public List<VentaDetalle> findResumenVentasPorSucursalProducto(){
        try {
            MovimientoCaja mc = mcr.findFirstBySucursalEstatusSucursalTrueOrderByIdMovimientoCajaDesc();
            if (mc.getTipoMovimientoCaja().equals(TipoMovimiento.APERTURA)) {
                return vdr.findResumenVentasPorSucursalProducto(mc.getCreatedAt(), LocalDateTime.now());
            } else {
                throw new IllegalStateException("La caja no está abierta.");
            }
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
