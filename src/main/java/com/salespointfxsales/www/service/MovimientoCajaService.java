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
    public MovimientoCaja cerrarCaja() {
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
            }else{
                pcs.imprimirCorte(corte, mcA, mcC);
            }
            /*mcr.save(mcC);
            /*CREAMOS EL CORTE*/
 /* Corte corte = new Corte(null, sr.findByEstatusSucursalTrue().get(), mcC, mcA.getSaldoAnterior(), total, recoleccion, gasto, mcC.getSaldoFinal(), mcA.getCreatedAt(), LocalDateTime.now(), (short) lvd.size(), lvd.getFirst().getVenta().getFolio(), lvd.getLast().getVenta().getFolio());
            /*AQUI TRATAREMOS DE LLENAR TODO LOS PRODUCTOS DE CORTE DETALLE*/
 /*List<CorteDetalle> listaCorteDetalle = new ArrayList<>();
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
            cr.save(corte);
            System.out.printf(
                    "%-30s %-8s %-8s %-8s %-8s %-8s %-10s %-10s%n %-10s%n",
                    "Producto / Paquete", "Entrada", "Venta", "Salida", "T.Entra", "T.Sale", "Canceladas", "Existencia","Peso"
            );
            System.out.println("---------------------------------------------------------------------------------------------");

            listaCorteDetalle.forEach(cd -> {
                String nombre = cd.getSucursalProducto().getProducto().getNombreProducto(); // o como se llame el campo nombre
                System.out.printf(
                        "%-30s %-8.2f %-8.2f %-8.2f %-8.2f %-8.2f %-10.2f %-10.2f%n %-10.2f%n",
                        nombre,
                        cd.getEntrada(),
                        cd.getVenta(),
                        cd.getSalida(),
                        cd.getTraspasoEntrada(),
                        cd.getTraspasoSalida(),
                        cd.getCanceladas(),
                        cd.getExistencia(),
                        cd.getPeso()
                );
            });
            System.out.println("El peso total es:" + peso);

// Convertimos a lista final para guardar
            /*listaCorteDetalle.addAll(mapaCorte.values());

            //List<VentaDetalle> lvd = vdr.ventasXsucursalXactivasXcorte(mc.getCreatedAt(), LocalDateTime.now());
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
            System.out.println("== RESUMEN DE VENTAS DE PRODUCTOS ==");
            for (VentaDetalle vd : resumenList) {
                String nombre = vd.getSucursalProducto().getProducto().getNombreProducto();
                float precio = vd.getPrecio();
                float cantidad = vd.getCantidad();
                float subtotal = vd.getSubTotal();

                System.out.printf("%-25s | Cantidad: %2f | Precio: $%.2f | Subtotal: $%.2f%n", nombre, cantidad, precio, subtotal);
            }

            /*Map<String, Map<String, Float>> ventas = new LinkedHashMap<>();
            lv.forEach(v->{
                String folio = v.getFolio();
                ventas.computeIfAbsent(folio, f-> new HashMap<>());
                v.getListVentaDetalle().forEach(detalle -> {
                    String producto = detalle.getSucursalProducto().getProducto().getNombreProducto();
                    float unidades = detalle.getCantidad();

                    ventas.get(folio).merge(producto, unidades, Float::sum);
                });
            });
            ventas.forEach((folio, productos) -> {
                System.out.println("== Ventas: " + folio + " ==");
                productos.forEach((producto, cantidad) -> {
                    System.out.println("Producto: " + producto + " | Unidades: " + cantidad);
                });
                System.out.println();
            });*/

 /* Corte corte = new Corte(null, sr.findByEstatusSucursalTrue().get(), mc, mc.getSaldoAnterior(), total, recoleccion, gasto, total + saldoanterior - recoleccion - gasto, mc.getCreatedAt(), LocalDateTime.now(), (short) lvd.size(), lvd.getFirst().getVenta().getFolio(), lvd.getLast().getVenta().getFolio());
            mc.setSaldoFinal(total + saldoanterior - gasto - recoleccion);*/

 /*mcr.save(mc);
            cr.save(corte);*/
            //System.out.println("Venta: " + total + "\n Saldo anterior: " + saldoanterior + "\n Gasto: " + gasto + "\n Recoleccion:" + recoleccion);
            /*Map<String, Map<String, Float>> resumenPorFolio = new LinkedHashMap<>();

            lmi.forEach(mi -> {
                String folio = mi.getFolio().toString();

                resumenPorFolio
                        .computeIfAbsent(folio, f -> new HashMap<>());

                mi.getListMovimientoInventarioDetalle().forEach(detalle -> {
                    String producto = detalle.getSucursalProducto().getProducto().getNombreProducto();
                    float unidades = detalle.getUnidades();

                    resumenPorFolio.get(folio).merge(producto, unidades, Float::sum);
                });
            });

// Mostrar resultados
            resumenPorFolio.forEach((folio, productos) -> {
                System.out.println("== Folio: " + folio + " ==");
                productos.forEach((producto, cantidad) -> {
                    System.out.println("Producto: " + producto + " | Unidades: " + cantidad);
                });
                System.out.println();
            });*/
            return mcC;
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
