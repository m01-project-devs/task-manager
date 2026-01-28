package com.m01project.taskmanager.service;

import com.m01project.emailsender.application.port.EmailMessage;
import com.m01project.taskmanager.config.FrontendProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PasswordResetEmailComposer {

    private final FrontendProperties frontend;

    public EmailMessage compose(String to, UUID token) {
        String link = UriComponentsBuilder
                .fromHttpUrl(frontend.baseUrl())
                .path(frontend.resetPasswordPath())
                .queryParam("token", token.toString())
                .build()
                .toUriString();

        String subject = "Reset your password";

        String body = """
                <p>Hi,</p>
                <p>We received a request to reset your password.</p>
                <p><a href="%s">Reset Password</a></p>
                <p>If you did not request this, you can ignore this email.</p>
                """.formatted(link);

        return new EmailMessage(to, subject, body, true);
    }
}
