package com.salespointfxsales.www.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.File;
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

    public void enviarCorreoConAdjunto(String toEmail, String asunto, String cuerpo, File archivoAdjunto) throws MessagingException {
        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);

        helper.setTo(toEmail);
        helper.setSubject(asunto);
        helper.setText(cuerpo);

        FileSystemResource file = new FileSystemResource(archivoAdjunto);
        helper.addAttachment(archivoAdjunto.getName(), file);

        mailSender.send(mensaje);
    }
}
