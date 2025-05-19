package com.gymTracker.GymTracker.Infracstructure.Utils;

import com.gymTracker.GymTracker.Domain.Constants.MailType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


@Configuration
public class SendMails {

    @Value("${mail.sender}")
    private String mailFrom;
    @Value("${mail.session.register}")
    private String mailRegister;

    @Value("${mail.session.booked}")
    private String mailBooked;

    @Value("${mail.session.reminder}")
    private String mailReminder;

    @Value("${mail.session.edited}")
    private String mailEdited;

    @Value("${mail.session.deleted}")
    private String mailDeleted;
    private final JavaMailSender mailSender;

    public SendMails(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public String sendEmail(MailType type, String recipientEmail){
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
}
