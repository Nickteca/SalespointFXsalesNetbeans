package com.salespointfxsales.www.service.email;

import com.salespointfxsales.www.model.Configuracion;
import com.salespointfxsales.www.repo.ConfiguracionRepo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSender {

    private final JavaMailSender mailSender;
    private final ConfiguracionRepo cr;

    public void enviarCorreoConAdjunto(String asunto, String cuerpo, File archivoAdjunto) throws MessagingException {
        /*OBTEENEMOS LOS CORREO*/
        List<Configuracion> lc = cr.findByClaveAndSucursalEstatusSucursalTrue("correo_corte");
        /*LOS ASIGNAMOS  AUN LIST DE STRIMGS*/
        List<String> listaCorreos = new ArrayList<>();
        for (Configuracion config : lc) {
            String[] correos = config.getValor().split(",\\s*"); // soporta coma con o sin espacio
            for (String correo : correos) {
                if (!correo.isBlank()) {
                    listaCorreos.add(correo);
                }
            }
        }
        if (listaCorreos.isEmpty()) {
            throw new MessagingException("No se encontraron correos para enviar.");
        }
        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);

        helper.setTo(listaCorreos.toArray(new String[0]));
        helper.setSubject(asunto);
        helper.setText(cuerpo);

        FileSystemResource file = new FileSystemResource(archivoAdjunto);
        helper.addAttachment(archivoAdjunto.getName(), file);

        mailSender.send(mensaje);
    }
}
