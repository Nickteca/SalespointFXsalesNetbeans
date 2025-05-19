package com.salespointfxsales.www.service;

import com.salespointfxsales.www.model.enums.NombreFolio;
import com.salespointfxsales.www.model.Corte;
import com.salespointfxsales.www.model.CorteDetalle;
import com.salespointfxsales.www.model.MovimientoCaja;
import com.salespointfxsales.www.model.MovimientoInventario;
import com.salespointfxsales.www.model.MovimientoInventarioDetalle;
import com.salespointfxsales.www.model.SucursalGasto;
import com.salespointfxsales.www.model.SucursalProducto;
import com.salespointfxsales.www.model.SucursalRecoleccion;
import com.salespointfxsales.www.model.Venta;
import com.salespointfxsales.www.model.VentaDetalle;
import com.salespointfxsales.www.model.enums.TipoMovimiento;
import com.salespointfxsales.www.repo.CorteRepo;
import com.salespointfxsales.www.repo.MovimientoCajaRepo;
import com.salespointfxsales.www.repo.MovimientoInventarioRepo;
import com.salespointfxsales.www.repo.SucursalGastoRepo;
import com.salespointfxsales.www.repo.SucursalProductoRepo;
import com.salespointfxsales.www.repo.SucursalRecoleccionRepo;
import com.salespointfxsales.www.repo.SucursalRepo;
import com.salespointfxsales.www.repo.VentaDetalleRepo;
import com.salespointfxsales.www.repo.VentaRepo;
import com.salespointfxsales.www.service.printer.PrinterCorteService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MovimientoCajaService {

    private final MovimientoCajaRepo mcr;
    private final SucursalRepo sr;
    private final VentaDetalleRepo vdr;
    private final SucursalGastoRepo sgr;
    private final SucursalRecoleccionRepo srr;
    private final MovimientoInventarioRepo mir;
    private final CorteRepo cr;
    private final SucursalProductoRepo spr;
    private final VentaRepo vr;
    private final CorteService cs;
    private final PrinterCorteService pcs;

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

    @Transactional
    public Corte cerrarCaja() throws Exception {
        try {
            MovimientoCaja mcA = mcr.findFirstBySucursalEstatusSucursalTrueOrderByIdMovimientoCajaDesc();
            if (!mcA.getTipoMovimientoCaja().equals(TipoMovimiento.APERTURA)) {
                throw new IllegalArgumentException("la caja no esta abierta, cierre el sistema y vuelva abrirlo");
            }
            List<VentaDetalle> lvd = vdr.ventasXsucursalXactivasXcorte(mcA.getCreatedAt(), LocalDateTime.now());
            List<Venta> lv = vr.findBySucursalEstatusSucursalTrueAndCreatedAtBetween(mcA.getCreatedAt(), LocalDateTime.now());
            List<SucursalGasto> lsg = sgr.findBySucursalEstatusSucursalTrueAndCreatedAtBetween(mcA.getCreatedAt(), LocalDateTime.now());
            List<SucursalRecoleccion> lsr = srr.findBySucursalEstatusSucursalTrueAndCreatedAtBetween(mcA.getCreatedAt(), LocalDateTime.now());
            List<MovimientoInventario> lmi = mir.findBySucursalEstatusSucursalTrueAndCreatedAtBetween(mcA.getCreatedAt(), LocalDateTime.now());
            List<SucursalProducto> lsp = spr.findBySucursalEstatusSucursalTrueAndVendibleTrueOrInventariableTrue();

            /*HACEMOS ALAGUNAS OPERACIONES*/
            float total = toatalVenta(lvd);
            float saldoanterior = mcA.getSaldoAnterior();
            float gasto = totalGasto(lsg);
            float recoleccion = totalRecoleccion(lsr);

            /*CREAMOS EL MOVIMIENTO CAJA PERO DE CIERRE*/
            MovimientoCaja mcC = new MovimientoCaja(null, TipoMovimiento.CIERRE, mcA.getSaldoAnterior(), total + saldoanterior - gasto - recoleccion, LocalDateTime.now(), sr.findByEstatusSucursalTrue().get());
            mcC = mcr.save(mcC);
            Corte corte = cs.save(mcA, mcC);
            if (corte == null) {
                throw new IllegalArgumentException();
            } /*else {
                if (pcs.imprimirCorte(corte, mcA, mcC)) {*/
                    return corte;
               
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    private float toatalVenta(List<VentaDetalle> lvd) {
        try {
            if (lvd.isEmpty()) {
                throw new IllegalArgumentException("al prece no hay ventas");
            }
            float total = 0f;
            for (VentaDetalle detalle : lvd) {
                total += detalle.getSubTotal(); // Asumiendo que tienes un método getSubtotal()
            }
            return total;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    private float totalGasto(List<SucursalGasto> lsg) {
        try {
            if (lsg.isEmpty()) {
                return 0;
            }
            float total = 0f;
            for (SucursalGasto sg : lsg) {
                total += sg.getMontoGasto(); // Asumiendo que tienes un método getSubtotal()
            }
            return total;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    private float totalRecoleccion(List<SucursalRecoleccion> lsr) {
        try {
            if (lsr.isEmpty()) {
                return 0;
            }
            float total = 0f;
            for (SucursalRecoleccion sr : lsr) {
                total += sr.getTotalRecoleccion(); // Asumiendo que tienes un método getSubtotal()
            }
            return total;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }
}
