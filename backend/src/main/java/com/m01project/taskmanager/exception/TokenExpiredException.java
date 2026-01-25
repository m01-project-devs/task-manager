package com.m01project.taskmanager.exception;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException() {
        super("Password reset token has expired.");
    }
}
