package com.kowal.backend.customer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kowal.backend.security.model.AuthUser;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "workouts")
public class WorkoutLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate workoutDate;

    @ManyToOne
    @JsonIgnore
    private AuthUser authUser;

    @ManyToOne
    @JsonIgnore
    private Day day;

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExerciseLog> exerciseLogs;
}