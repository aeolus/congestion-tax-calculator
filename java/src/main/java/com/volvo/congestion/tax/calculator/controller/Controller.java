package com.volvo.congestion.tax.calculator.controller;

import com.volvo.congestion.tax.calculator.domain.Car;
import com.volvo.congestion.tax.calculator.domain.Motorbike;
import com.volvo.congestion.tax.calculator.domain.Vehicle;
import com.volvo.congestion.tax.calculator.domain.VehicleTypes;
import com.volvo.congestion.tax.calculator.dto.GetTaxDto;
import com.volvo.congestion.tax.calculator.service.CongestionTaxCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class Controller {

    @Autowired
    private CongestionTaxCalculator calculator;

    private static final Logger LOG = LoggerFactory.getLogger(Controller.class);

    @GetMapping("/ping")
    String ping() {
        return "pong";
    }

    @PostMapping("/tax")
    int getTax(@RequestBody GetTaxDto dto) {

        LOG.info("Received http call with type: " + dto.getVehicleType() + ", times: " + dto.getDateTimes());

        Vehicle vehicle;
        if (dto.vehicleType == VehicleTypes.CAR) {
            vehicle = new Car();
        } else if (dto.vehicleType == VehicleTypes.MOTORCYCLE) {
            vehicle = new Motorbike();
        } else throw new UnsupportedVehicleTypeException("Unsupported vehicle type: " + dto.getVehicleType());

        List<LocalDateTime> times = dto.getDateTimes()
                .stream()
                .map(i -> i.withZoneSameInstant(ZoneId.of("CET")).toLocalDateTime())
                .collect(Collectors.toList());

        return calculator.getTax(vehicle, times);
    }
}
