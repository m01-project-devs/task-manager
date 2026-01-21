package com.m01project.emailsender.domain.exception;

public class EmailDisabledException extends RuntimeException {
    public EmailDisabledException(String message)
    {
        super("Email sending is disabled");
    }
}
