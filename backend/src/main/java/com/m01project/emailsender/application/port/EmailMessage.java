package com.m01project.emailsender.application.port;

public record EmailMessage(
        String to,
        String subject,
        String body,
        boolean html
) {}
