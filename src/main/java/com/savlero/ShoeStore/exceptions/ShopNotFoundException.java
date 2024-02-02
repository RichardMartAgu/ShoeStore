package com.savlero.ShoeStore.exceptions;

public class ShopNotFoundException extends Exception {

    public ShopNotFoundException() {
        super();
    }

    public ShopNotFoundException(String message) {
        super(message);
    }

    public ShopNotFoundException(long id) {
        super("La Tienda con id " + id + " no existe");
    }
}

