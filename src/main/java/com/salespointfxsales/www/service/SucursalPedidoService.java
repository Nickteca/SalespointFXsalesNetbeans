package com.salespointfxsales.www.service;

import com.salespointfxsales.www.model.Sucursal;
import com.salespointfxsales.www.model.SucursalPedido;
import com.salespointfxsales.www.model.SucursalPedidoDetalle;
import com.salespointfxsales.www.repo.SucursalPedidoRepo;
import com.salespointfxsales.www.repo.SucursalRepo;
import com.salespointfxsales.www.service.printer.PrinterPedidoService;
import com.salespointfxsales.www.service.watsapp.PedidoWatsappService;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SucursalPedidoService {

    private final SucursalPedidoRepo spr;
    private final SucursalRepo sr;
    private final PrinterPedidoService pps;
    private final PedidoWatsappService pws;
    
     Resultado resultado = new Resultado();

    @Transactional
    public Resultado save(SucursalPedido spedido) throws Exception {
        try {
            Optional<Sucursal> sucursalOpt = sr.findByEstatusSucursalTrue();
            if (sucursalOpt.isEmpty()) {
                throw new Exception("No se encontró una sucursal activa.");
            }
            spedido.setSucursal(sucursalOpt.get());
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
            SucursalPedido supedido = spr.save(spedido);
            resultado.guardado =  supedido != null;
            if(resultado.guardado){
                resultado.impreso = pps.imprimirPedido(spedido); // Puede fallar pero no lanza excepción

                mensaje.append("\n📅 Fecha: ").append(LocalDateTime.now().toLocalDate());
                resultado.whatsappEnviado = pws.enviarWatsapp(mensaje.toString());

                if (!resultado.whatsappEnviado) {
                    resultado.mensaje = "Pedido guardado, pero no se pudo enviar el mensaje de WhatsApp.";
                } else if (!resultado.impreso) {
                    resultado.mensaje = "Pedido guardado y enviado, pero no se pudo imprimir.";
                } else {
                    resultado.mensaje = "Pedido guardado, impreso y enviado correctamente.";
                }
            }
            /*if (supedido != null && pps.imprimirPedido(spedido)) {
                mensaje.append("\n📅 Fecha: ").append(LocalDateTime.now().toLocalDate());

                boolean enviado = pws.enviarWatsapp(mensaje.toString());
                if (!enviado) {
                    throw  new Exception("No se evio el mensage de Watsapp");
                }
            }*/
            
        } catch (Exception e) {
            log.error("✅ Respuesta de UltraMsg: {}", e.getMessage());
            throw e;
        }
        return resultado;
    }
    public static class Resultado {
        public boolean guardado;
        public boolean impreso;
        public boolean whatsappEnviado;
        public String mensaje;
        public Resultado() {
            this.guardado = false;
            this.impreso = false;
            this.whatsappEnviado = false;
            this.mensaje = "";
        }
    }
}
