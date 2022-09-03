package com.volvo.congestion.tax.calculator.domain;

public class Motorbike implements Vehicle {
    @Override
    public VehicleTypes getVehicleType() {
        return VehicleTypes.MOTORCYCLE;
    }
}
