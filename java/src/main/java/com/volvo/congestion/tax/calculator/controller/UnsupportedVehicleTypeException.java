package com.volvo.congestion.tax.calculator.controller;

public class UnsupportedVehicleTypeException extends RuntimeException {
    public UnsupportedVehicleTypeException(String message) {
        super(message);
    }
}
