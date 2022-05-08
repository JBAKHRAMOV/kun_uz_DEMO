package com.company.exp;

public class PasswordOrEmailWrongException extends RuntimeException{
    public PasswordOrEmailWrongException(String message) {
        super(message);
    }
}
