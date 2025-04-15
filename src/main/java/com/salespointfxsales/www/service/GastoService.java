package com.salespointfxsales.www.service;

import com.salespointfxsales.www.model.Gasto;
import com.salespointfxsales.www.model.enums.TipoMovimiento;
import com.salespointfxsales.www.repo.GastoRepo;
import com.salespointfxsales.www.repo.MovimientoCajaRepo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GastoService {

    private final GastoRepo gr;
    private final MovimientoCajaRepo mcr;

    public List<Gasto> findAll() {
        try {
             if (mcr.findFirstBySucursalEstatusSucursalTrueOrderByIdMovimientoCajaDesc().getTipoMovimientoCaja().equals(TipoMovimiento.CIERRE)) {
                throw new IllegalStateException("La caja no está abierta.");
            }
            return gr.findAll();
        } catch (IllegalStateException e) {
            // Aquí capturamos el caso cuando no hay sucursal activa o la caja no está abierta
            System.err.println("Error: " + e.getMessage());
            throw e;  // Lanzamos la excepción para que el controller lo maneje
        } catch (Exception e) {
            // Captura cualquier otra excepción
            System.err.println("Error desconocido al guardar el gasto: " + e.getMessage());
            throw e;
        }

    }
}
