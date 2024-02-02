package com.savlero.ShoeStore.exceptions;

public class BrandNotFoundException extends Exception {
    public BrandNotFoundException() {
        super();
    }

    public BrandNotFoundException(String message) {
        super(message);
    }

    public BrandNotFoundException(long id) {
        super("La marca con id " + id + " no existe");
    }
}
