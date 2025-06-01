package com.learnjavawithkaushi.cdpatientmicroservice.exception;

public class PatientValidationException extends RuntimeException {
    public PatientValidationException(String message) {
        super(message);
    }
}