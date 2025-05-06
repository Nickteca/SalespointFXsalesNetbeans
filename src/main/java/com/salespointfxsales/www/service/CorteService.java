package com.salespointfxsales.www.service;

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
import com.salespointfxsales.www.model.enums.NombreFolio;
import com.salespointfxsales.www.repo.CorteRepo;
import com.salespointfxsales.www.repo.MovimientoInventarioRepo;
import com.salespointfxsales.www.repo.SucursalGastoRepo;
import com.salespointfxsales.www.repo.SucursalProductoRepo;
import com.salespointfxsales.www.repo.SucursalRecoleccionRepo;
import com.salespointfxsales.www.repo.SucursalRepo;
import com.salespointfxsales.www.repo.VentaDetalleRepo;
import com.salespointfxsales.www.repo.VentaRepo;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CorteService {

    private final CorteRepo cr;
    
    private final SucursalRepo sr;
    private final VentaDetalleRepo vdr;
    private final SucursalGastoRepo sgr;
    private final SucursalRecoleccionRepo srr;
    private final MovimientoInventarioRepo mir;
    private final SucursalProductoRepo spr;
    private final VentaRepo vr;

    public Corte save(MovimientoCaja mcA, MovimientoCaja mcC) {
        try {
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

            Corte corte = new Corte(null, sr.findByEstatusSucursalTrue().get(), mcC, mcA.getSaldoAnterior(), total, recoleccion, gasto, mcC.getSaldoFinal(), mcA.getCreatedAt(), LocalDateTime.now(), (short) lv.size(), lvd.getFirst().getVenta().getFolio(), lvd.getLast().getVenta().getFolio());
            /*AQUI TRATAREMOS DE LLENAR TODO LOS PRODUCTOS DE CORTE DETALLE*/
            List<CorteDetalle> listaCorteDetalle = new ArrayList<>();
            Map<SucursalProducto, CorteDetalle> mapaCorte = new HashMap<>();

// Inicializamos el mapa con todos los productos o paquetes
            lsp.forEach(sp -> {
                CorteDetalle cd = new CorteDetalle();
                cd.setSucursalProducto(sp);
                cd.setCorte(corte); // Tu objeto Corte actual
                cd.setEntrada(0f);
                cd.setVenta(0f);
                cd.setSalida(0f);
                cd.setTraspasoEntrada(0f);
                cd.setTraspasoSalida(0f);
                cd.setCanceladas(0f);
                cd.setExistencia(sp.getInventario()); // o como manejes la existencia actual
                mapaCorte.put(sp, cd);
            });
            // Sumamos las ventas
            //sumamos e peso de las costillas
            float peso = 0;
            for (VentaDetalle vd : lvd) {
                SucursalProducto sp = vd.getSucursalProducto();
                CorteDetalle cd = mapaCorte.get(sp);
                if (cd != null) {
                    cd.setVenta(cd.getVenta() + vd.getCantidad());
                    cd.setPeso(vd.getPeso());
                    peso += vd.getPeso();
                }
            }

// Sumamos entradas desde movimientos (pero solo si **NO** es paquete)
            for (MovimientoInventario mi : lmi) {
                for (MovimientoInventarioDetalle mid : mi.getListMovimientoInventarioDetalle()) {
                    SucursalProducto sp = mid.getSucursalProducto();
                    if (sp != null && mapaCorte.containsKey(sp) && !sp.getProducto().isEsPaquete()) {
                        CorteDetalle cd = mapaCorte.get(sp);
                        switch (mi.getNombreFolio()) {
                            case NombreFolio.Ajuste_Entrada:
                                cd.setEntrada(cd.getEntrada() + mid.getUnidades());
                                break;
                            case NombreFolio.Ajuste_salida:
                                cd.setSalida(cd.getSalida() + mid.getUnidades());
                                break;
                            case NombreFolio.Traspaso_Entrada:
                                cd.setTraspasoEntrada(cd.getTraspasoEntrada() + mid.getUnidades());
                                break;
                            case NombreFolio.Trspaso_Salida:
                                cd.setTraspasoSalida(cd.getTraspasoSalida() + mid.getUnidades());
                                break;
                            case NombreFolio.Cancelacion_Venta:
                                cd.setCanceladas(cd.getCanceladas() + mid.getUnidades());
                                break;
                        }
                    }
                }
            }
            listaCorteDetalle.addAll(mapaCorte.values());
            corte.setListCorteDetalle(listaCorteDetalle);
            return cr.save(corte);
            //return corte;
        } catch (NumberFormatException e) {
            throw e;
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
