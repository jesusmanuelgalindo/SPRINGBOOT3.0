package gob.jmas.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.regex.Pattern;

@Component
public class EnviarEmail {

    @Autowired
    private JavaMailSender emailSender;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private static boolean validarEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
    public void enviarEmailHTML(String destino, String asunto, String mensaje) {
        if (!validarEmail(destino)) {
            throw new IllegalArgumentException("Direccion de Correo Invalida: " + destino);
        }
        else
        {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = null;
            try
            {
                helper = new MimeMessageHelper(message, true, "UTF-8");
                helper.setFrom("facturaenlineajmasparral@gmail.com");
                helper.setTo(destino);
                helper.setSubject(asunto);
                helper.setText(mensaje, true);
                emailSender.send(message);
            }
            catch (MessagingException e)
            {
                throw new RuntimeException(e);
                }
        }
    }
    public void enviarEmail(String destino, String asunto, String mensaje) {
        if (!validarEmail(destino)) {
            throw new IllegalArgumentException("Direccion de Correo Invalida: " + destino);
        }
        else {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(destino);
            message.setSubject(asunto);
            message.setText(mensaje);
            emailSender.send(message);
        }
    }

    public void enviarEmailConAdjunto(String destino, String asunto, String mensaje, String pathToAttachment) {
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(destino);
            helper.setSubject(asunto);
            helper.setText(mensaje);
            FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
            helper.addAttachment("Invoice", file);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        emailSender.send(message);
    }
}
