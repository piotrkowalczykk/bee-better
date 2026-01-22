package com.kowal.backend.customer.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class LogWorkoutRequest{
    private Long dayId;
    private LocalDate workoutDate;
    private Long exerciseId;
    private List<SetLogRequest> sets;
}

