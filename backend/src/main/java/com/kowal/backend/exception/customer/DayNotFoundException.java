package com.kowal.backend.exception.customer;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

@Getter
public class DayNotFoundException extends CustomException {
    public DayNotFoundException(Long dayId) {
        super("Day not found with ID " + dayId, HttpStatus.NOT_FOUND);
    }

    public DayNotFoundException(LocalDate date) {
        super("Day not found with date " + date, HttpStatus.NOT_FOUND);
    }
}