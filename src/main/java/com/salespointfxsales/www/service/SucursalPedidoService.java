package com.salespointfxsales.www.service;

import com.salespointfxsales.www.model.SucursalPedido;
import com.salespointfxsales.www.model.SucursalPedidoDetalle;
import com.salespointfxsales.www.repo.SucursalPedidoRepo;
import com.salespointfxsales.www.repo.SucursalRepo;
import com.salespointfxsales.www.service.watsapp.PedidoWatsappService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SucursalPedidoService {

    private final SucursalPedidoRepo spr;
    private final SucursalRepo sr;

    private final PedidoWatsappService pws;

    @Transactional
    public SucursalPedido save(SucursalPedido spedido) throws Exception {
        try {
            spedido.setSucursal(sr.findByEstatusSucursalTrue().get());
            spedido.setCreatedAt(LocalDateTime.now());
            /*FORMATO PARA MANDAR EL MENSAGE*/
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("🧾 *Nuevo Pedido de Sucursal:*\n");
            mensaje.append(spedido.getSucursal().getNombreSucursal()).append("\n\n");

            int contador = 1;
            for (SucursalPedidoDetalle detalle : spedido.getListSucursalpedidoDetalle()) {
                String nombreProducto = detalle.getSucursalProducto().getProducto().getNombreProducto();
                int cantidad = detalle.getCantidad();
                mensaje.append(contador++).append(". ")
                        .append(nombreProducto)
                        .append(" - Cantidad: ").append(cantidad)
                        .append("\n");
            }

            mensaje.append("\n📅 Fecha: ").append(LocalDateTime.now().toLocalDate());
            /**/
            pws.enviarWatsapp("4341327947", mensaje.toString());
            return spr.save(spedido);
        } catch (Exception e) {
            throw e;
        }
    }
}
