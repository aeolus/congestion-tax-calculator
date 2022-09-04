package com.volvo.congestion.tax.calculator.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties
public class CityTaxRuleDto {

    public String city;
    public Integer dailyMax;
    public List<DayOfWeek> noTaxWeekdays;
    public List<Month> noTaxMonth;
    public List<LocalDate> noTaxDates;
    public List<TaxByTimeDto> taxByTime;

    @Getter
    @Setter
    public static class TaxByTimeDto {
        public LocalTime start;
        public LocalTime end;
        public Integer amount;
    }
}
