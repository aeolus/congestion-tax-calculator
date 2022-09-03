package com.volvo.congestion.tax.calculator.dto;

import com.volvo.congestion.tax.calculator.domain.VehicleTypes;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.List;

@Builder
public class GetTaxDto {
    @Getter
    public VehicleTypes vehicleType;

    @Getter
    public List<ZonedDateTime> dateTimes; // preferable UTC timestamp for data exchange between http endpoints
}
