package com.kowal.backend.customer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class WorkoutDayResponse {
    private LocalDate workoutDate;
    private List<ExerciseLogResponse> exercises;
}
