package com.garage.exceptions;

public class ParkingSlotsFullException extends Exception {

    public ParkingSlotsFullException(String message) {
        super(message);
    }
}