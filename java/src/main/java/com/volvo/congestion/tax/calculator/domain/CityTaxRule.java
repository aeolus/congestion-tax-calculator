package com.volvo.congestion.tax.calculator.domain;

import com.volvo.congestion.tax.calculator.dto.CityTaxRuleDto;
import lombok.Builder;

import java.time.*;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public class CityTaxRule {
    public String city;
    public Integer dailyMax;
    public List<DayOfWeek> noTaxWeekDays;
    public List<Month> noTaxMonth;
    public List<LocalDate> noTaxDates;
    public List<TaxByTime> taxByTime;

    @Builder
    public static class TaxByTime {
        public LocalTime start;
        public LocalTime end;
        public Integer amount;
    }

    public static CityTaxRule fromDto(CityTaxRuleDto dto) {
        List<TaxByTime> taxByTimes =
                dto.taxByTime.stream()
                        .map(taxByTime ->
                                TaxByTime.builder()
                                        .start(taxByTime.start)
                                        .end(taxByTime.end)
                                        .amount(taxByTime.amount)
                                        .build())
                        .collect(Collectors.toList());
        return CityTaxRule.builder()
                .city(dto.city)
                .dailyMax(dto.dailyMax)
                .noTaxWeekDays(List.copyOf(dto.noTaxWeekdays))
                .noTaxMonth(List.copyOf(dto.noTaxMonth))
                .noTaxDates(List.copyOf(dto.noTaxDates))
                .taxByTime(taxByTimes)
                .build();
    }

    public int getTax(Vehicle vehicle, List<LocalDateTime> time) {
        return 0; // dummy
    }
}
