package com.salespointfxsales.www.service;

import com.salespointfxadmin.www.model.enums.Naturaleza;
import com.salespointfxsales.www.model.Sucursal;
import com.salespointfxsales.www.model.Venta;
import com.salespointfxsales.www.model.dto.ResultadoCobro;
import com.salespointfxsales.www.repo.SucursalRepo;
import com.salespointfxsales.www.repo.VentaDetalleRepo;
import com.salespointfxsales.www.repo.VentaRepo;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepo vr;
    private final VentaDetalleRepo vdr;
    private final SucursalRepo sr;

    @Transactional
    public Venta save(Venta v, ResultadoCobro rc) {
        try {
            v.setStatus(true);
            v.setCreatedAt(LocalDateTime.now());
            v.setNaturalezaVenta(Naturaleza.S);
            Sucursal sucursal = sr.findByEstatusSucursalTrue()
                    .orElseThrow(() -> new IllegalStateException("No hay sucursal activa"));

            v.setSucursal(sucursal);
            return vr.save(v);

        } catch (Exception e) {
            // Aquí puedes registrar el error o lanzarlo para que el controller lo capture
            System.err.println("Error al guardar la venta: " + e.getMessage());
            throw e;
        }
    }

}
