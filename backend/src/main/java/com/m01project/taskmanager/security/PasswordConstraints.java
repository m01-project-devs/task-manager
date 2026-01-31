package com.m01project.taskmanager.security;

public final class PasswordConstraints
{
    private PasswordConstraints(){};

    public static final int MIN_LENGTH = 6;
    public static final int MAX_LENGTH = 24;

    public static final String REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{" +
                    MIN_LENGTH + "," + MAX_LENGTH + "}$";


    public static final String ERROR_MESSAGE =
            "Password must be " + MIN_LENGTH + " to " + MAX_LENGTH +
                    " characters long and contain at least one uppercase letter, " +
                    "one lowercase letter, one number, and one special character (@$!%*?&)";
}
