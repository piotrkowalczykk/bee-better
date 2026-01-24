package com.kowal.backend.customer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Exercise1RmResponse {
    private LocalDate date;
    private double oneRm;
}
