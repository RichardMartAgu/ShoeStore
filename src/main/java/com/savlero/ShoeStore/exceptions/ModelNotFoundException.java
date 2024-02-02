package com.savlero.ShoeStore.exceptions;

public class ModelNotFoundException extends Exception {

    public ModelNotFoundException() {
        super();
    }

    public ModelNotFoundException(String message) {
        super(message);
    }

    public ModelNotFoundException(long id) {
        super("El modelo con id " + id + " no existe");
    }
}