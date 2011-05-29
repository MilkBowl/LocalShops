package com.milkbukkit.localshops.exceptions;

public class TypeNotFoundException extends Exception {

    private static final long serialVersionUID = 23542315L;

    public TypeNotFoundException() {
    }

    public TypeNotFoundException(String message) {
        super(message);
    }

    public TypeNotFoundException(Throwable cause) {
        super(cause);
    }

    public TypeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}