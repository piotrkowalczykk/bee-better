package com.kowal.backend.exception.customer;

import org.springframework.http.HttpStatus;

import java.time.LocalDate;

public class WorkoutLogNotFoundException extends CustomException {
    public WorkoutLogNotFoundException(Long workoutLogId) {
        super("Workout log not found with id: " + workoutLogId, HttpStatus.NOT_FOUND);
    }

    public WorkoutLogNotFoundException(LocalDate workoutDate) {
        super("Workout log not found with date: " + workoutDate, HttpStatus.NOT_FOUND);
    }
}
