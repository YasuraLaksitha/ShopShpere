package com.shopsphere.exceptions;

public class InvalidCredentialsException extends RuntimeException {

    protected String email;
    public InvalidCredentialsException(String email) {
        super("Invalid credentials for email " + email);
        this.email = email;
    }
}
