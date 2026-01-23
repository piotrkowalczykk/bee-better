package com.kowal.backend.customer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class RoutineLogResponse {
    private LocalDate logDate;
    private Long routineId;
    private Integer value;
}
