package com.kowal.backend.customer.controller;

import com.kowal.backend.customer.dto.request.*;
import com.kowal.backend.customer.dto.response.*;
import com.kowal.backend.customer.model.Equipment;
import com.kowal.backend.customer.model.WorkoutLog;
import com.kowal.backend.customer.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }

    @PostMapping("/add-routine")
    public ResponseEntity<RoutineResponse> addRoutine(@Valid @RequestBody AddRoutineRequest addRoutineRequest,
                                                        @AuthenticationPrincipal UserDetails userDetails){
        String userEmail = userDetails.getUsername();
        RoutineResponse response = customerService.addRoutine(addRoutineRequest, userEmail);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/routines")
    public ResponseEntity<List<RoutineResponse>> getAllRoutines(@AuthenticationPrincipal UserDetails userDetails){
        String userEmail = userDetails.getUsername();
        List<RoutineResponse> routines = customerService.getAllRoutines(userEmail);
        return ResponseEntity.ok(routines);
    }

    @GetMapping("/routines/{id}")
    public ResponseEntity<RoutineResponse> getRoutineById(@PathVariable("id") Long routineId,
                                                            @AuthenticationPrincipal UserDetails userDetails){
        String userEmail = userDetails.getUsername();
        RoutineResponse routine = customerService.getRoutineById(routineId, userEmail);
        return ResponseEntity.ok(routine);
    }

    @PutMapping("/routines/{id}")
    public ResponseEntity<RoutineResponse> updateRoutine(@PathVariable("id") Long routineId,
                                                            @RequestBody UpdateRoutineRequest updateRoutineRequest,
                                                                @AuthenticationPrincipal UserDetails userDetails){
        String userEmail = userDetails.getUsername();
        RoutineResponse updatedRoutine = customerService.updateRoutine(routineId, updateRoutineRequest, userEmail);
        return ResponseEntity.ok(updatedRoutine);
    }

    @DeleteMapping("/routines/{id}")
    public ResponseEntity<RoutineResponse> deleteRoutine(@PathVariable("id") Long routineId,
                                                @AuthenticationPrincipal UserDetails userDetails){
        String userEmail = userDetails.getUsername();
        RoutineResponse deletedRoutine = customerService.deleteRoutine(routineId, userEmail);
        return ResponseEntity.ok(deletedRoutine);
    }

    @PostMapping("/routines/log")
    public ResponseEntity<RoutineLogResponse> logRoutine(@RequestBody LogRoutineRequest logRoutineRequest,
                                                         @AuthenticationPrincipal UserDetails userDetails){
        String userEmail = userDetails.getUsername();
        RoutineLogResponse loggedRoutine = customerService.logRoutine(logRoutineRequest, userEmail);
        return ResponseEntity.ok(loggedRoutine);
    }

    @GetMapping("/routines/logs")
    public List<RoutineLogResponse> getLogsForDay(
            @RequestParam LocalDate date,
            @AuthenticationPrincipal UserDetails userDetails) {

        return customerService.getRoutineLogsForDay(userDetails.getUsername(), date);
    }












    @PostMapping("/add-day")
    public ResponseEntity<DayResponse> addDay(@Valid @RequestBody AddDayRequest addDayRequest,
                                                  @AuthenticationPrincipal UserDetails userDetails){
        String userEmail = userDetails.getUsername();
        DayResponse response = customerService.addDay(addDayRequest, userEmail);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/days")
    public ResponseEntity<List<DayResponse>> getAllDays(@AuthenticationPrincipal UserDetails userDetails){
        String userEmail = userDetails.getUsername();
        List<DayResponse> days = customerService.getAllDays(userEmail);
        return ResponseEntity.ok(days);
    }

    @GetMapping("/days/{id}")
    public ResponseEntity<DayResponse> getDayById(@PathVariable("id") Long dayId,
                                                          @AuthenticationPrincipal UserDetails userDetails){
        String userEmail = userDetails.getUsername();
        DayResponse day = customerService.getDayById(dayId, userEmail);
        return ResponseEntity.ok(day);
    }

    @PutMapping("/days/{id}")
    public ResponseEntity<DayResponse> updateDay(@PathVariable("id") Long dayId,
                                                         @RequestBody UpdateDayRequest updateDayRequest,
                                                         @AuthenticationPrincipal UserDetails userDetails){
        String userEmail = userDetails.getUsername();
        DayResponse updatedDay = customerService.updateDay(dayId, updateDayRequest, userEmail);
        return ResponseEntity.ok(updatedDay);
    }

    @DeleteMapping("/days/{id}")
    public ResponseEntity<DayResponse> deleteDay(@PathVariable("id") Long dayId,
                                                         @AuthenticationPrincipal UserDetails userDetails){
        String userEmail = userDetails.getUsername();
        DayResponse deletedDay = customerService.deleteDay(dayId, userEmail);
        return ResponseEntity.ok(deletedDay);
    }

    @GetMapping("/days/today")
    public ResponseEntity<DayResponse> getDayForDate(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        String userEmail = userDetails.getUsername();
        DayResponse day = customerService.getDayForDate(userEmail, date);
        return ResponseEntity.ok(day);
    }
















    @PostMapping("/add-exercise")
    public ResponseEntity<ExerciseResponse> addDay(@ModelAttribute AddExerciseRequest addExerciseRequest,
                                                   @AuthenticationPrincipal UserDetails userDetails){
        String userEmail = userDetails.getUsername();
        ExerciseResponse response = customerService.addExercise(addExerciseRequest, userEmail);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/exercises")
    public ResponseEntity<List<ExerciseResponse>> getAllExercises(@AuthenticationPrincipal UserDetails userDetails){
        String userEmail = userDetails.getUsername();
        List<ExerciseResponse> exercises = customerService.getAllExercises(userEmail);
        return ResponseEntity.ok(exercises);
    }

    @GetMapping("/exercises/{id}")
    public ResponseEntity<ExerciseResponse> getExerciseById(@PathVariable("id") Long exerciseId,
                                                  @AuthenticationPrincipal UserDetails userDetails){
        String userEmail = userDetails.getUsername();
        ExerciseResponse exercise = customerService.getExerciseById(exerciseId, userEmail);
        return ResponseEntity.ok(exercise);
    }

    @PutMapping("/exercises/{id}")
    public ResponseEntity<ExerciseResponse> updateExercise(@PathVariable("id") Long exerciseId,
                                                 @RequestBody UpdateExerciseRequest updateExerciseRequest,
                                                 @AuthenticationPrincipal UserDetails userDetails){
        String userEmail = userDetails.getUsername();
        ExerciseResponse updatedExercise = customerService.updateExercise(exerciseId, updateExerciseRequest, userEmail);
        return ResponseEntity.ok(updatedExercise);
    }

    @DeleteMapping("/exercises/{id}")
    public ResponseEntity<ExerciseResponse> deleteExercise(@PathVariable("id") Long exerciseId,
                                                 @AuthenticationPrincipal UserDetails userDetails){
        String userEmail = userDetails.getUsername();
        ExerciseResponse deletedExercise = customerService.deleteExercise(exerciseId, userEmail);
        return ResponseEntity.ok(deletedExercise);
    }

    @GetMapping("/exercises/equipment")
    public ResponseEntity<List<Equipment>> getAllEquipment(@AuthenticationPrincipal UserDetails userDetails){
        String userEmail = userDetails.getUsername();
        List<Equipment> equipment = customerService.getAllEquipment(userEmail);
        return ResponseEntity.ok(equipment);
    }










    @GetMapping("/workouts")
    public ResponseEntity<List<WorkoutLog>> getAllWorkoutLogs(@AuthenticationPrincipal UserDetails userDetails){
        String userEmail = userDetails.getUsername();
        return ResponseEntity.ok(customerService.getAllWorkoutLogs(userEmail));
    }

    @GetMapping("/workouts/by-date")
    public ResponseEntity<WorkoutDayResponse> getWorkoutByDate(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam LocalDate date
    ) {
        String userEmail = userDetails.getUsername();
        return ResponseEntity.ok(customerService.getWorkoutByDate(userEmail, date));
    }

    @PostMapping("/workouts/log-workout")
    public ResponseEntity<WorkoutLog> logWorkout(@AuthenticationPrincipal UserDetails userDetails, @RequestBody LogWorkoutRequest logWorkoutRequest){
        String userEmail = userDetails.getUsername();
        WorkoutLog workout = customerService.logWorkout(userEmail, logWorkoutRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(workout);
    }


    @PutMapping("/workouts/{id}")
    public ResponseEntity<WorkoutLog> deleteWorkoutLog(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long workoutLogId){
        String userEmail = userDetails.getUsername();
        return ResponseEntity.noContent().build();
    }



}
