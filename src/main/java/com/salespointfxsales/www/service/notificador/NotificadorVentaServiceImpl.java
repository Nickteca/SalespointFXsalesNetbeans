package com.salespointfxsales.www.service.notificador;

import com.salespointfxsales.www.interfaces.NotificadorVentaService;
import com.salespointfxsales.www.model.Venta;
import com.salespointfxsales.www.model.VentaDetalle;
import com.salespointfxsales.www.repo.VentaDetalleRepo;
import com.salespointfxsales.www.repo.VentaRepo;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificadorVentaServiceImpl implements NotificadorVentaService {

    private final VentaRepo vr;
    private final VentaDetalleRepo vdr;

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    @Value("${app.notify.username}")      // p. ej. admin
    private String apiUser;

    @Value("${app.notify.password}")      // p. ej. winlinux6
    private String apiPass;

    @Value("${app.base-url}")             // p. ej. http://localhost:8080
    private String baseUrl;

    private String jwtToken;

    /**
     * Al arrancar el servicio, hacemos login y guardamos el token
     */
    @PostConstruct
    public void init() {
        // Wake-up request
        Request wakeUp = new Request.Builder()
                .url(baseUrl + "/api/venta") // alguna ruta pública sin auth
                .get()
                .build();

        try (Response wakeResp = client.newCall(wakeUp).execute()) {
            log.info("Render wake-up: {}", wakeResp.code());
        } catch (IOException e) {
            log.warn("Fallo al despertar Render", e);
        }
        login();
    }

    /*ESTE PROCESO LO ARA CDA 1 MINUTO PARA COMPRRBAR QUE NO HAY PENDIENTES*/
    @Scheduled(fixedDelay = 60000) // cada 60 segundos
    public void reenviarVentasPendientes() {
        log.info("⏰ Buscando ventas pendientes para reenviar...");

        // Suponiendo que tienes un método en VentaRepo:
        // List<Venta> findByEnviadoFalse();
        List<Venta> pendientes = vr.findByEnviadoFalse();

        for (Venta v : pendientes) {
            try {
                v.setListVentaDetalle(vdr.findByVenta(v));
                notificarVenta(v);
            } catch (Exception e) {
                log.error("❌ Error al reenviar venta {}: {}", v.getIdVenta(), e.getMessage());
            }
        }

        log.info("✅ Intento de reenvío finalizado. Total: {}", pendientes.size());
    }

    /**
     * Hace POST a /auth/login y guarda el token en jwtToken
     */
    private void login() {
        try {
            JSONObject cred = new JSONObject()
                    .put("nombreUsuario", apiUser)
                    .put("passwordUsuario", apiPass);

            RequestBody body = RequestBody.create(
                    cred.toString(),
                    MediaType.get("application/json")
            );

            Request req = new Request.Builder()
                    .url(baseUrl + "/auth/login")
                    .post(body)
                    .build();

            try (Response resp = client.newCall(req).execute()) {
                if (resp.isSuccessful() && resp.body() != null) {
                    String respBody = resp.body().string();
                    JSONObject o = new JSONObject(respBody);
                    this.jwtToken = o.getString("token");
                    log.info("Notificación: obtenida JWT válida");
                    log.info("Notificación: obtenida JWT válida — {}", jwtToken);
                } else {
                    log.error("Notificación: login fallido [{}]", resp.code());
                }
            }
        } catch (IOException e) {
            log.error("Notificación: excepción al hacer login", e);
        }
    }

    /**
     * Llamas este método desde tu VentaService en un hilo aparte
     */
    public void notificarVenta(Venta ventaf) {
        if (jwtToken == null) {
            login();  // por si no se obtuvo en init
        }
        if (ventaf.getListVentaDetalle() == null || ventaf.getListVentaDetalle().isEmpty()) {
            // Forzamos la carga de los detalles si aún no se han cargado
            ventaf.setListVentaDetalle(vdr.findByVenta(ventaf));
        }

        JSONObject ventaJson = new JSONObject()
                .put("folio", ventaf.getFolio())
                .put("status", ventaf.isStatus())
                .put("naturaleza", ventaf.getNaturalezaVenta())
                .put("totalVenta", ventaf.getTotalVenta())
                .put("createdAt", ventaf.getCreatedAt())
                .put("idSucursal", ventaf.getSucursal().getIdSucursal());
        // Array de detalles
        JSONArray detallesArr = new JSONArray();
        for (VentaDetalle det : ventaf.getListVentaDetalle()) {
            JSONObject detJson = new JSONObject();
            detJson.put("cantidad", det.getCantidad());
            detJson.put("peso", det.getPeso());
            detJson.put("precio", det.getPrecio());
            detJson.put("subtotal", det.getSubTotal());
            detJson.put("idSucursalProducto", det.getSucursalProducto().getIdSucursalProducto());
            detallesArr.put(detJson);
        }
        ventaJson.put("listVentaDetalle", detallesArr);

        RequestBody body = RequestBody.create(
                ventaJson.toString(),
                MediaType.get("application/json")
        );

        Request req = new Request.Builder()
                .url(baseUrl + "/api/venta") // o donde recibas la notificación
                .post(body)
                .addHeader("Authorization", "Bearer " + jwtToken)
                .build();

        try (Response resp = client.newCall(req).execute()) {
            if (resp.isSuccessful()) {
                String cuerpo = resp.body() != null ? resp.body().string() : "Sin cuerpo";
                ventaf.setEnviado(true);
                vr.save(ventaf);
                log.info("Venta {} enviada correctamente. Código: {}, Cuerpo: {}",
                        ventaf.getIdVenta(), resp.code(), cuerpo);
            } else if (resp.code() == 401) {
                // token expirado o inválido: re-login y retry una vez
                log.warn("Notificación: token expirado, reintentando login");
                login();
                notificarVenta(ventaf);
            } else {
                log.error("Notificación: error HTTP {} al enviar venta {}", resp.code(), ventaf.getIdVenta());
            }
        } catch (IOException e) {
            log.error("Notificación: excepción al notificar venta {}", ventaf.getIdVenta(), e);
        }
    }
    /*AQUI SE LLAMA LAS QUE NO SE DIERON DE ALTA*/
 /*public void reenviarenta(Venta ventaf) {
        if (jwtToken == null) {
            login();  // por si no se obtuvo en init
        }

        JSONObject ventaJson = new JSONObject()
                .put("folio", ventaf.getFolio())
                .put("status", ventaf.isStatus())
                .put("naturaleza", ventaf.getNaturalezaVenta())
                .put("totalVenta", ventaf.getTotalVenta())
                .put("createdAt", ventaf.getCreatedAt())
                .put("idSucursal", ventaf.getSucursal().getIdSucursal());
        // Array de detalles
        JSONArray detallesArr = new JSONArray();
        for (VentaDetalle det : ventaf.getListVentaDetalle()) {
            JSONObject detJson = new JSONObject();
            detJson.put("cantidad", det.getCantidad());
            detJson.put("peso", det.getPeso());
            detJson.put("precio", det.getPrecio());
            detJson.put("subtotal", det.getSubTotal());
            detJson.put("idSucursalProducto", det.getSucursalProducto().getIdSucursalProducto());
            detallesArr.put(detJson);
        }
        ventaJson.put("lvd", detallesArr);

        RequestBody body = RequestBody.create(
                ventaJson.toString(),
                MediaType.get("application/json")
        );

        Request req = new Request.Builder()
                .url(baseUrl + "/api/ventas") // o donde recibas la notificación
                .post(body)
                .addHeader("Authorization", "Bearer " + jwtToken)
                .build();

        try (Response resp = client.newCall(req).execute()) {
            if (resp.isSuccessful()) {
                String cuerpo = resp.body() != null ? resp.body().string() : "Sin cuerpo";
                ventaf.setEnviado(true);
                vr.save(ventaf);
                log.info("Venta {} enviada correctamente. Código: {}, Cuerpo: {}",
                        ventaf.getIdVenta(), resp.code(), cuerpo);
            } else if (resp.code() == 401) {
                // token expirado o inválido: re-login y retry una vez
                log.warn("Notificación: token expirado, reintentando login");
                login();
                notificarVenta(ventaf);
            } else {
                log.error("Notificación: error HTTP {} al enviar venta {}", resp.code(), ventaf.getIdVenta());
            }
        } catch (IOException e) {
            log.error("Notificación: excepción al notificar venta {}", ventaf.getIdVenta(), e);
        }*/

}
