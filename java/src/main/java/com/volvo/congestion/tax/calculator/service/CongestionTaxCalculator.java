package com.volvo.congestion.tax.calculator.service;

import com.volvo.congestion.tax.calculator.domain.Vehicle;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class CongestionTaxCalculator {

    private final static List<String> TOLL_FREE_VEHICLE_TYPES = List.of(
            "Motorcycle",
            "Tractor",
            "Emergency",
            "Diplomat",
            "Foreign",
            "Military");

    private final static int DAILY_MAX_TAX = 60;

    private final static long ONE_HOUR_IN_MILLIE_SECONDS = 60 * 60 * 1000;

    public int getTax(@NonNull Vehicle vehicle, @NonNull List<LocalDateTime> dateTimes) {

        if (dateTimes.isEmpty()) throw new IllegalArgumentException(); // FIXME: return 0 instead if no time was given?

        List<LocalDateTime> sortedDateTimes = dateTimes.stream().sorted().collect(toList());
        LocalDateTime intervalStart = sortedDateTimes.get(0);
        int totalFee = 0;

        for (int i = 0; i < sortedDateTimes.size(); i++) {
            LocalDateTime date = sortedDateTimes.get(i);
            int nextFee = getTollFee(date, vehicle);
            int tempFee = getTollFee(intervalStart, vehicle);

            if (Duration.between(intervalStart, date).toMillis() <= ONE_HOUR_IN_MILLIE_SECONDS) {
                if (totalFee > 0) totalFee -= tempFee;
                if (nextFee >= tempFee) tempFee = nextFee;
                totalFee += tempFee;
            } else {
                intervalStart = date;
                totalFee += nextFee;
            }
        }

        if (totalFee > DAILY_MAX_TAX) totalFee = DAILY_MAX_TAX;
        return totalFee;
    }

    public int getTollFee(@NonNull LocalDateTime dateTime, @NonNull Vehicle vehicle) {
        if (isTollFreeVehicle(vehicle) || isTollFreeDate(dateTime)) return 0;

        int hour = dateTime.getHour();
        int minute = dateTime.getMinute();

        if (hour == 6 && minute >= 0 && minute <= 29) return 8;
        else if (hour == 6 && minute >= 30 && minute <= 59) return 13;
        else if (hour == 7 && minute >= 0 && minute <= 59) return 18;
        else if (hour == 8 && minute >= 0 && minute <= 29) return 13;
        else if (hour == 8 && minute >= 30) return 8;
        else if (hour >= 9 && hour <= 14) return 8;
        else if (hour == 15 && minute >= 0 && minute <= 29) return 13;
        else if (hour == 15 && minute >= 0 || hour == 16 && minute <= 59) return 18;
        else if (hour == 17 && minute >= 0 && minute <= 59) return 13;
        else if (hour == 18 && minute >= 0 && minute <= 29) return 8;
        else return 0;
    }

    private boolean isTollFreeVehicle(Vehicle vehicle) {
        return TOLL_FREE_VEHICLE_TYPES.contains(vehicle.getVehicleType());
    }

    private Boolean isTollFreeDate(LocalDateTime date) {
        int year = date.getYear();
        int month = date.getMonthValue();
        DayOfWeek day = date.getDayOfWeek();
        int dayOfMonth = date.getDayOfMonth();

        boolean isTollFreeDate = false;

        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
            isTollFreeDate = true;
        }

        if (year == 2013) {
            if ((month == 1 && dayOfMonth == 1) ||
                    (month == 3 && (dayOfMonth == 28 || dayOfMonth == 29)) ||
                    (month == 4 && (dayOfMonth == 1 || dayOfMonth == 30)) ||
                    (month == 5 && (dayOfMonth == 1 || dayOfMonth == 8 || dayOfMonth == 9)) ||
                    (month == 6 && (dayOfMonth == 5 || dayOfMonth == 6 || dayOfMonth == 21)) ||
                    (month == 7) ||
                    (month == 11 && dayOfMonth == 1) ||
                    (month == 12 && (dayOfMonth == 24 || dayOfMonth == 25 || dayOfMonth == 26 || dayOfMonth == 31))) {
                isTollFreeDate = true;
            }
        }

        return isTollFreeDate;
    }
}
