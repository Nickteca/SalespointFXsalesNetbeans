package com.salespointfxsales.www.service.watsapp;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PedidoWatsappService {

    @Value("${ultramsg.instance-id}")
    private String instanceId;

    @Value("${ultramsg.token}")
    private String token;

    @Value("${ultramsg.url}")
    private String url;
    @Value("${ultramsg.numero}")
    private String numero;

    public boolean enviarWatsapp(String mensaje) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();

        RequestBody body = new FormBody.Builder()
                .add("token", token)
                .add("to", numero)
                .add("body", mensaje)
                .build();

        Request request = new Request.Builder()
                .url("https://api.ultramsg.com/instance108464/messages/chat")
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("❌ Error HTTP: " + response.code());
                log.error("❌ Error HTTP: {}", response.code());
                return false;
            }

            String responseBody = response.body().string();
            System.out.println("✅ Respuesta: " + responseBody);
            log.info("✅ Respuesta de UltraMsg: {}", responseBody);

            JSONObject json = new JSONObject(responseBody);
            String message = json.optString("message");
            log.info("✅ Respuesta de UltraMsg: {}", message);

            return message.equalsIgnoreCase("ok");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
