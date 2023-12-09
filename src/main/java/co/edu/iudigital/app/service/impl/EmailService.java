package co.edu.iudigital.app.service.impl;

import co.edu.iudigital.app.service.iface.IEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

// Anotación Service indica que esta clase es un servicio
@Service
public class EmailService implements IEmailService {

    // Declaración de un logger para realizar logging
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    // Inyección de dependencia del JavaMailSender
    @Autowired
    private JavaMailSender sender;

    // Método para enviar un correo electrónico
    @Override
    public boolean sendEmail(String mensaje, String email, String asunto) {
        LOGGER.info("Mensaje: {}", mensaje);
        return sendEmailTool(mensaje, email, asunto);
    }

    // Método privado para realizar el envío real del correo electrónico
    private boolean sendEmailTool(String mensaje, String email, String asunto) {
        boolean send = false;
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setFrom("jmachadoa12@gmail.com");
            helper.setTo(email);
            helper.setText(mensaje, true);
            helper.setSubject(asunto);
            sender.send(message);
            send = true;
            LOGGER.info("Email enviado!");
        } catch (MessagingException e) {
            LOGGER.error("Hubo un error al enviar el mail: {}", e);
        }
        return send;
    }
}
