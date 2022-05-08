package com.company.exp;

public class CategoryAlredyExistsException extends RuntimeException{
    public CategoryAlredyExistsException(String message) {
        super(message);
    }
}
