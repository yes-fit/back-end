package com.gymTracker.GymTracker.Infracstructure.Utils;

import com.gymTracker.GymTracker.App.Controller.AppController;
import com.gymTracker.GymTracker.Domain.Constants.MailType;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Configuration
public class SendMails {

    @Value("${mail.sender}")
    private String mailFrom;

    private final JavaMailSender mailSender;

    public SendMails(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public String sendEmail(MailType type, String recipientEmail){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(mailFrom);
            helper.setTo(recipientEmail);
            helper.setSubject(type.getSubject());
            if (type.equals(MailType.SESSION_REGISTRATION)) {
                try (var inputStream = Objects.requireNonNull(AppController.class.getResourceAsStream("/templates/Registration.html"))) {
                    helper.setText(
                            new String(inputStream.readAllBytes(), StandardCharsets.UTF_8), true
                    );
                }
            }
            if (type.equals(MailType.SESSION_BOOKING)) {
                try (var inputStream = Objects.requireNonNull(AppController.class.getResourceAsStream("/templates/Booking.html"))) {
                    helper.setText(
                            new String(inputStream.readAllBytes(), StandardCharsets.UTF_8), true
                    );
                }
            }
            if (type.equals(MailType.SESSION_EDITED)) {
                try (var inputStream = Objects.requireNonNull(AppController.class.getResourceAsStream("/templates/SessionEdit.html"))) {
                    helper.setText(
                            new String(inputStream.readAllBytes(), StandardCharsets.UTF_8), true
                    );
                }
            }
            if (type.equals(MailType.SESSION_DELETION)) {
                try (var inputStream = Objects.requireNonNull(AppController.class.getResourceAsStream("/templates/SessionDelete.html"))) {
                    helper.setText(
                            new String(inputStream.readAllBytes(), StandardCharsets.UTF_8), true
                    );
                }
            }

            helper.addInline("union.png", new File("C:\\Users\\AbdulBasit\\Pictures\\union.png"));

            mailSender.send(message);
            return "Success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}

/* -------- former way of getting email type (Just plain text)
try {
        String messageBody = null;
        if (type.equals(MailType.SESSION_REGISTRATION)) {
        messageBody =  mailRegister;
        }
        if (type.equals(MailType.SESSION_BOOKING)) {
        messageBody =  mailBooked;
        }
        if (type.equals(MailType.SESSION_REMINDER)) {
        messageBody =  mailReminder;
        }
        if (type.equals(MailType.SESSION_EDITED)) {
        messageBody =  mailEdited;
        }
        if (type.equals(MailType.SESSION_DELETION)) {
        messageBody =  mailDeleted;
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(recipientEmail);
        message.setSubject(type.getSubject());
        message.setText(messageBody);

        mailSender.send(message);
        return "Success";
        }
        catch (Exception e){
        return e.getMessage();
        }
        }
*/