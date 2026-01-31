package com.m01project.taskmanager.service;

public interface PasswordResetService {
    void forgotPassword(String email);
    void resetPassword(String token, String newPassword);
}
