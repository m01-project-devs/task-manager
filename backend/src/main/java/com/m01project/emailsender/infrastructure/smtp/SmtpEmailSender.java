package com.m01project.emailsender.infrastructure.smtp;

import com.m01project.emailsender.application.port.EmailMessage;
import com.m01project.emailsender.application.port.EmailSender;
import com.m01project.emailsender.domain.exception.EmailSendFailedException;
import com.m01project.emailsender.infrastructure.config.EmailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SmtpEmailSender implements EmailSender {

    private final JavaMailSender javaMailSender;
    private final EmailProperties properties;

    @Override
    public void send(EmailMessage message) {
        if (!properties.enabled()){
            return;
        }

        var mime = javaMailSender.createMimeMessage();

        try {
            var helper = new MimeMessageHelper(mime, "UTF-8");
            helper.setFrom(properties.from());
            helper.setTo(message.to());
            helper.setSubject(message.subject());
            helper.setText(message.body(), message.html());
            javaMailSender.send(mime);
        }
        catch (Exception e){
            throw new EmailSendFailedException("Failed to send email", e);
        }
    }


}
