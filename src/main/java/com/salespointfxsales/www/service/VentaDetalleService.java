package com.salespointfxsales.www.service;

import com.salespointfxsales.www.model.MovimientoCaja;
import com.salespointfxsales.www.model.SucursalProducto;
import com.salespointfxsales.www.model.VentaDetalle;
import com.salespointfxsales.www.model.enums.TipoMovimiento;
import com.salespointfxsales.www.repo.MovimientoCajaRepo;
import com.salespointfxsales.www.repo.VentaDetalleRepo;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VentaDetalleService {

    private final VentaDetalleRepo vdr;
    private final MovimientoCajaRepo mcr;

    public List<VentaDetalle> ventasXsucursalXactivasXcorte() {
        try {
            MovimientoCaja mc = mcr.findFirstBySucursalEstatusSucursalTrueOrderByIdMovimientoCajaDesc();
            if (mc.getTipoMovimientoCaja().equals(TipoMovimiento.APERTURA)) {
                List<VentaDetalle> lvd = vdr.ventasXsucursalXactivasXcorte(mc.getCreatedAt(), LocalDateTime.now());
                Map<SucursalProducto, VentaDetalle> resumenMap = new HashMap<>();

                for (VentaDetalle detalle : lvd) {
                    SucursalProducto clave = detalle.getSucursalProducto();

                    if (!resumenMap.containsKey(clave)) {
                        // Creamos un nuevo VentaDetalle resumido
                        VentaDetalle resumen = new VentaDetalle();
                        resumen.setSucursalProducto(clave);
                        resumen.setCantidad(detalle.getCantidad());
                        resumen.setSubTotal(detalle.getSubTotal());
                        resumen.setPrecio(detalle.getPrecio()); // Puedes usar promedio, primero, o recalcular
                        resumen.setVenta(detalle.getVenta()); // o null, si no importa aquí

                        resumenMap.put(clave, resumen);
                    } else {
                        // Ya existe, sumamos
                        VentaDetalle existente = resumenMap.get(clave);
                        existente.setCantidad((short) (existente.getCantidad() + detalle.getCantidad()));
                        existente.setSubTotal(existente.getSubTotal() + detalle.getSubTotal());
                    }
                }
                List<VentaDetalle> resumenList = new ArrayList<>(resumenMap.values());
                return resumenList;
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
            throw e;// Lanzamos la excepción para que el controller lo maneje
        }
    }
}
