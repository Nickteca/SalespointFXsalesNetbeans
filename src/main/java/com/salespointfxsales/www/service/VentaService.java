package com.salespointfxsales.www.service;

import com.salespointfxadmin.www.model.enums.Naturaleza;
import com.salespointfxsales.www.model.Folio;
import com.salespointfxsales.www.model.ProductoPaquete;
import com.salespointfxsales.www.model.Sucursal;
import com.salespointfxsales.www.model.SucursalProducto;
import com.salespointfxsales.www.model.Venta;
import com.salespointfxsales.www.model.VentaDetalle;
import com.salespointfxsales.www.model.dto.ResultadoCobro;
import com.salespointfxsales.www.repo.FolioRepo;
import com.salespointfxsales.www.repo.ProductoPaqueteRepo;
import com.salespointfxsales.www.repo.SucursalProductoRepo;
import com.salespointfxsales.www.repo.SucursalRepo;
import com.salespointfxsales.www.repo.VentaDetalleRepo;
import com.salespointfxsales.www.repo.VentaRepo;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepo vr;
    private final VentaDetalleRepo vdr;
    private final SucursalRepo sr;
    private final FolioRepo fr;
    private final ProductoPaqueteRepo ppr;
    private final SucursalProductoRepo spr;

    @Transactional
    public Venta save(Venta v, ResultadoCobro rc, Folio f) {
        try {
            v.setStatus(true);
            v.setCreatedAt(LocalDateTime.now());
            v.setNaturalezaVenta(Naturaleza.Salida);
            Sucursal sucursal = sr.findByEstatusSucursalTrue()
                    .orElseThrow(() -> new IllegalStateException("No hay sucursal activa"));

            v.setSucursal(sucursal);

            fr.incrementarNumeroFolio(f.getIdFolio());
            actualizarInventario(v);
            return vr.save(v);

        } catch (Exception e) {
            // Aquí puedes registrar el error o lanzarlo para que el controller lo capture
            System.err.println("Error al guardar la venta: " + e.getMessage());
            throw e;
        }
    }

    private void actualizarInventario(Venta v) {
        try {
            List<VentaDetalle> lvd = v.getListVentaDetalle();
            for (VentaDetalle vd : lvd) {
                SucursalProducto sp = vd.getSucursalProducto();
                if (sp.getProducto().isEsPaquete()) {
                    List<ProductoPaquete> lpp = ppr.findByPaquete(sp.getProducto());
                    for (ProductoPaquete pp : lpp) {
                        SucursalProducto spa = spr.findByIdSucursalProducto(pp.getProductoPaquete().getIdProducto()); 
                        spa.setInventario(spa.getInventario()-(pp.getCantidad()*vd.getCantidad()));
                        spr.save(spa);
                    }
                }else{
                    sp.setInventario(sp.getInventario()- vd.getCantidad());
                    spr.save(sp);
                }
            }
        } catch (Exception e) {
            System.err.println("Error Actuañlizar el inventario: " + e.getMessage());
            throw e;
        }
    }

}
