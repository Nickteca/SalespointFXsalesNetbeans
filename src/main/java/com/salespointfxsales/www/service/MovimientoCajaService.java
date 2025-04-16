package com.salespointfxsales.www.service;

import com.salespointfxsales.www.model.MovimientoCaja;
import com.salespointfxsales.www.model.SucursalGasto;
import com.salespointfxsales.www.model.SucursalRecoleccion;
import com.salespointfxsales.www.model.VentaDetalle;
import com.salespointfxsales.www.model.enums.TipoMovimiento;
import com.salespointfxsales.www.repo.MovimientoCajaRepo;
import com.salespointfxsales.www.repo.SucursalGastoRepo;
import com.salespointfxsales.www.repo.SucursalRecoleccionRepo;
import com.salespointfxsales.www.repo.SucursalRepo;
import com.salespointfxsales.www.repo.VentaDetalleRepo;
import java.time.LocalDateTime;
import java.util.List;
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
    public MovimientoCaja cerrarCaja() {
        try {
            MovimientoCaja mc = mcr.findFirstBySucursalEstatusSucursalTrueOrderByIdMovimientoCajaDesc();
            if (!mc.getTipoMovimientoCaja().equals(TipoMovimiento.APERTURA)) {
                throw new IllegalArgumentException("la caja no esta abierta, cierre el sistema y vuelva abrirlo");
            }
            List<VentaDetalle> lvd = vdr.findResumenVentasPorSucursalProducto(mc.getCreatedAt(), LocalDateTime.now());
            List<SucursalGasto> lsg = sgr.findBySucursalEstatusSucursalTrueAndCreatedAtBetween(mc.getCreatedAt(), LocalDateTime.now());
            List<SucursalRecoleccion> lsr = srr.findBySucursalEstatusSucursalTrueAndCreatedAtBetween(mc.getCreatedAt(), LocalDateTime.now());

            float total = toatalVenta(lvd);
            float saldoanterior = mc.getSaldoAnterior();
            float gasto = totalGasto(lsg);
            float recoleccion = totalRecoleccion(lsr);
            
            System.out.println("Venta: "+total+"\n Saldo anterior: "+saldoanterior+"\n Gasto: "+gasto+"\n Recoleccion:"+recoleccion);

            return mc;
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
