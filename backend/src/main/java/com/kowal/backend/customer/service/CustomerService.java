package com.kowal.backend.customer.service;

import com.kowal.backend.customer.dto.request.*;
import com.kowal.backend.customer.dto.response.*;
import com.kowal.backend.customer.model.Equipment;
import com.kowal.backend.customer.model.Exercise;
import com.kowal.backend.customer.model.WorkoutLog;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

public interface CustomerService {
    RoutineResponse addRoutine(AddRoutineRequest addRoutineRequest, String userEmail);
    RoutineResponse getRoutineById(Long routineId, String userEmail);
    List<RoutineResponse> getAllRoutines(String userEmail);
    RoutineResponse updateRoutine(Long routineId, UpdateRoutineRequest updateRoutineRequest, String userEmail);
    RoutineResponse deleteRoutine(Long routineId, String userEmail);

    DayResponse addDay(@Valid AddDayRequest addDayRequest, String userEmail);
    List<DayResponse> getAllDays(String userEmail);
    DayResponse getDayById(Long dayId, String userEmail);
    DayResponse updateDay(Long dayId, UpdateDayRequest updateRoutineRequest, String userEmail);
    DayResponse deleteDay(Long dayId, String userEmail);

    ExerciseResponse addExercise(@Valid AddExerciseRequest addExerciseRequest, String userEmail);
    List<ExerciseResponse> getAllExercises(String userEmail);
    ExerciseResponse getExerciseById(Long exerciseId, String userEmail);
    ExerciseResponse updateExercise(Long dayId, UpdateExerciseRequest updateExerciseRequest, String userEmail);
    ExerciseResponse deleteExercise(Long exerciseId, String userEmail);

    List<Equipment> getAllEquipment(String userEmail);

    List<WorkoutLog> getAllWorkoutLogs(String userEmail);

    WorkoutLog deleteWorkoutLog(String userEmail, Long workoutLogId);

    WorkoutLog logWorkout(String userEmail, LogWorkoutRequest logWorkoutRequest);

    WorkoutDayResponse getWorkoutByDate(String userEmail, LocalDate date);

    DayResponse getDayForDate(String userEmail, LocalDate date);

    RoutineLogResponse logRoutine(LogRoutineRequest logRoutineRequest, String userEmail);

    List<RoutineLogResponse> getRoutineLogsForDay(String username, LocalDate date);

    List<Exercise1RmResponse> getExercise1Rm(Long exerciseId, String userEmail, LocalDate from, LocalDate to);

    List<Exercise> getAllLoggedExercises(String userEmail);
}
