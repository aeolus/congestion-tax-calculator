package com.volvo.congestion.tax.calculator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.volvo.congestion.tax.calculator.Application;
import com.volvo.congestion.tax.calculator.domain.VehicleTypes;
import com.volvo.congestion.tax.calculator.dto.GetTaxDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.ZonedDateTime;
import java.util.List;

import static com.volvo.congestion.tax.calculator.domain.VehicleTypes.CAR;
import static com.volvo.congestion.tax.calculator.domain.VehicleTypes.DIPLOMAT;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerIT {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final HttpHeaders HTTP_HEADERS = new HttpHeaders();

    @BeforeAll
    public static void setup() {
        HTTP_HEADERS.setContentType(MediaType.APPLICATION_JSON);
        MAPPER.registerModule(new JavaTimeModule());
    }

    @Test
    public void pingTest() {
        assertThat(this.restTemplate.getForObject(BASE_URL + port + "/ping", String.class))
                .isEqualTo("pong");
    }

    @Test
    public void getTaxTest() throws Exception {
        String url = BASE_URL + port + "/tax";

        HttpEntity<String> request = new HttpEntity<>(MAPPER.writeValueAsString(getGetTaxDto(CAR)), HTTP_HEADERS);

        ResponseEntity<Integer> response = this.restTemplate.postForEntity(url, request, Integer.class);

        assertThat(response.getBody()).isEqualTo(18);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldFailWhenCallWithUnsupportedVehicleType() throws Exception {
        String url = BASE_URL + port + "/tax";

        GetTaxDto dto = getGetTaxDto(DIPLOMAT);

        HttpEntity<String> request = new HttpEntity<>(MAPPER.writeValueAsString(dto), HTTP_HEADERS);

        ResponseEntity<String> response = this.restTemplate.postForEntity(url, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR); // FIXME: should map exception to 400
    }

    private static GetTaxDto getGetTaxDto(VehicleTypes car) {
        GetTaxDto dto = GetTaxDto.builder()
                .vehicleType(car)
                .dateTimes(List.of(ZonedDateTime.parse("2013-03-18T06:00:00Z")))
                .build();
        return dto;
    }
}
