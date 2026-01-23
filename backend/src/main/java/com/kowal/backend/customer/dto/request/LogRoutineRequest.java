package com.kowal.backend.customer.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LogRoutineRequest {
    private LocalDate logDate;
    private Long routineId;
    private Integer value;
}
