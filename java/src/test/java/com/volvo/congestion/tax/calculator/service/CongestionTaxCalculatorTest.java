package com.volvo.congestion.tax.calculator.service;

import com.volvo.congestion.tax.calculator.domain.Car;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.parse;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CongestionTaxCalculatorTest {

    @Autowired
    private CongestionTaxCalculator calculator;

    private static final Car car = new Car();

    @Test
    public void shouldValidateNonNullVehicle() {
        assertThrows(IllegalArgumentException.class, () -> calculator.getTax(null, of(now())));
        assertThrows(IllegalArgumentException.class, () -> calculator.getTax(car, null));
        assertThrows(IllegalArgumentException.class, () -> calculator.getTax(car, List.of()));
    }

    @Test
    public void shouldGetCorrectTollFeeForSpecificTime() {
        assertEquals(0, calculator.getTollFee(parse("1999-07-29T19:30:40"), car));
        assertEquals(0, calculator.getTollFee(parse("2013-07-29T19:30:40"), car));
        assertEquals(0, calculator.getTollFee(parse("2013-08-29T18:30:40"), car));
        assertEquals(8, calculator.getTollFee(parse("2013-08-29T18:28:40"), car));
    }

    @ParameterizedTest
    @CsvSource({
            "0,2013-01-14T21:00:00",
            "0,2013-01-15T21:00:00",
            "8,2013-02-07T06:23:27",
            "13,2013-02-07T15:27:00",
            "8,2013-02-08T06:27:00",
            "8,2013-02-08T14:35:00",
            "13,2013-02-08T15:29:00",
            "18,2013-02-08T15:47:00",
            "18,2013-02-08T16:01:00",
            "18,2013-02-08T16:48:00",
            "13,2013-02-08T17:49:00",
            "8,2013-02-08T18:29:00",
            "0,2013-02-08T18:35:00",
            "8,2013-03-26T14:25:00",
            "0,2013-03-28T14:07:27"})
    public void shouldCalcTaxWithSingleDatetime(int tax, String time) {
        assertEquals(tax, calculator.getTax(car, of(parse(time))));
    }

    @Test
    public void shouldCalcTaxWithDatetimeSeries() {
        assertEquals(55,
                calculator.getTax(car, of(
                        parse("2013-08-29T06:00:00"),
                        parse("2013-08-29T07:00:01"),
                        parse("2013-08-29T08:00:02"),
                        parse("2013-08-29T09:00:03"),
                        parse("2013-08-29T10:00:04")
                )));
    }

    @Test
    public void shouldCalTaxAndReturnMaxTaxOnceAboveThreshHold() {
        assertEquals(60,
                calculator.getTax(car, of(
                        parse("2013-08-29T06:00:00"),
                        parse("2013-08-29T07:00:01"),
                        parse("2013-08-29T08:00:02"),
                        parse("2013-08-29T09:00:03"),
                        parse("2013-08-29T10:00:04"),
                        parse("2013-08-29T10:00:04"),
                        parse("2013-08-29T17:00:04")
                )));
    }
}