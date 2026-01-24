package com.m01project.taskmanager.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException()
    {
        super("Invalid password reset token.");
    }
}
