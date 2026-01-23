package com.m01project.taskmanager.exception;

public class TokenAlreadyUsedException extends RuntimeException {
    public TokenAlreadyUsedException() {
        super("Password reset token has already been used.");
    }
}
