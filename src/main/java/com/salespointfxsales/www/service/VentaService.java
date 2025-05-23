package com.salespointfxsales.www.service;

import com.salespointfxadmin.www.model.enums.Naturaleza;
import com.salespointfxsales.www.model.Folio;
import com.salespointfxsales.www.model.MovimientoCaja;
import com.salespointfxsales.www.model.ProductoPaquete;
import com.salespointfxsales.www.model.Sucursal;
import com.salespointfxsales.www.model.SucursalProducto;
import com.salespointfxsales.www.model.Venta;
import com.salespointfxsales.www.model.VentaDetalle;
import com.salespointfxsales.www.model.dto.ResultadoCobro;
import com.salespointfxsales.www.model.enums.TipoMovimiento;
import com.salespointfxsales.www.repo.FolioRepo;
import com.salespointfxsales.www.repo.MovimientoCajaRepo;
import com.salespointfxsales.www.repo.ProductoPaqueteRepo;
import com.salespointfxsales.www.repo.SucursalProductoRepo;
import com.salespointfxsales.www.repo.SucursalRepo;
import com.salespointfxsales.www.repo.VentaDetalleRepo;
import com.salespointfxsales.www.repo.VentaRepo;
import com.salespointfxsales.www.service.printer.PrinterTicketService;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepo vr;
    private final VentaDetalleRepo vdr;
    private final SucursalRepo sr;
    private final FolioRepo fr;
    private final ProductoPaqueteRepo ppr;
    private final SucursalProductoRepo spr;
    private final PrinterTicketService pts;
    private final MovimientoCajaRepo mcr;

    @Transactional
    public ResultadoVenta save(Venta v, ResultadoCobro rc, Folio f) {
        ResultadoVenta resultado = new ResultadoVenta();
        try {
            if (mcr.findFirstBySucursalEstatusSucursalTrueOrderByIdMovimientoCajaDesc().getTipoMovimientoCaja().equals(TipoMovimiento.CIERRE)) {
                throw new IllegalStateException("La caja no está abierta.");
            }
            v.setStatus(true);
            v.setCreatedAt(LocalDateTime.now());
            v.setNaturalezaVenta(Naturaleza.Salida);
            Sucursal sucursal = sr.findByEstatusSucursalTrue()
                    .orElseThrow(() -> new IllegalStateException("No hay sucursal activa"));

            v.setSucursal(sucursal);

            fr.incrementarNumeroFolio(f.getIdFolio());
            actualizarInventario(v);
            v = vr.save(v);
            resultado.setVenta(v);
            resultado.setGuardado(true);

            boolean impreso = pts.imprimirTicket(v, rc);
            resultado.setImpreso(impreso);

            if (!impreso) {
                resultado.setMensaje("⚠️ Venta guardada pero no se pudo imprimir el ticket.");
            } else {
                resultado.setMensaje("✅ Venta guardada e impresa correctamente.");
            }

            return resultado;
        } catch (IllegalStateException e) {
            // Aquí capturamos el caso cuando no hay sucursal activa o la caja no está abierta
            log.error("Error: " + e.getMessage());
            throw e;  // Lanzamos la excepción para que el controller lo maneje
        } catch (Exception e) {
            // Captura cualquier otra excepción
            log.error("Error desconocido al guardar la venta: " + e.getMessage());
            throw e;
        }
    }

    private void actualizarInventario(Venta v) {
        try {
            // Acumular los descuentos por cada producto-sucursal
            Map<Short, Float> descuentos = new HashMap<>();

            for (VentaDetalle vd : v.getListVentaDetalle()) {
                SucursalProducto sp = vd.getSucursalProducto();
                if (sp.getProducto().isEsPaquete()) {
                    List<ProductoPaquete> lpp = ppr.findByPaquete(sp.getProducto());

                    for (ProductoPaquete pp : lpp) {
                        SucursalProducto spa = spr.findByProductoAndSucursal(pp.getProductoPaquete(), v.getSucursal())
                                .orElseThrow(() -> new RuntimeException("No se encontró producto en sucursal"));

                        short idSp = spa.getIdSucursalProducto();
                        float cantidadADescontar =  (pp.getCantidad() * vd.getCantidad());

                        descuentos.merge(idSp, cantidadADescontar, Float::sum);
                    }
                } else {
                    short idSp = sp.getIdSucursalProducto();
                    descuentos.merge(idSp, (float)vd.getCantidad(), Float::sum);
                }
            }

            // Aplicar descuentos ya acumulados
            for (Map.Entry<Short, Float> entry : descuentos.entrySet()) {
                SucursalProducto sp = spr.findById(entry.getKey()).orElseThrow();
                sp.setInventario(sp.getInventario() - entry.getValue());
                spr.save(sp);
            }

        } catch (Exception e) {
            log.error("Error al actualizar inventario: " + e.getMessage());
            throw e;
        }
    }
    public static class ResultadoVenta {
        private Venta venta;
        private boolean guardado;
        private boolean impreso;
        private String mensaje;

        // Getters y setters
        public Venta getVenta() {
            return venta;
        }

        public void setVenta(Venta venta) {
            this.venta = venta;
        }

        public boolean isGuardado() {
            return guardado;
        }

        public void setGuardado(boolean guardado) {
            this.guardado = guardado;
        }

        public boolean isImpreso() {
            return impreso;
        }

        public void setImpreso(boolean impreso) {
            this.impreso = impreso;
        }

        public String getMensaje() {
            return mensaje;
        }

        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }
    }

}
