package com.salespointfxsales.www.service.watsapp;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PedidoWatsappService {

    @Value("${ultramsg.instance-id}")
    private String instanceId;

    @Value("${ultramsg.token}")
    private String token;

    @Value("${ultramsg.url}")
    private String url;

    public void enviarWatsapp(String numero, String mensaje) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).build();
        RequestBody body = new FormBody.Builder().add("token", token).add("to", numero).add("body", mensaje)
                .build();

        Request request = new Request.Builder().url("https://api.ultramsg.com/instance108464/messages/chat").post(body).addHeader("content-type", "application/x-www-form-urlencoded").build();

        Response response = client.newCall(request).execute();

        System.out.println(response.body().string());
    }

}
