package com.boutique.exception;

public class ReferencedEntityException extends RuntimeException {
    public ReferencedEntityException(String message) {
        super(message);
    }
}
