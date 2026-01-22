package com.kowal.backend.customer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "set_logs")
public class SetLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int setNumber;
    private int reps;
    private double weight;
    private int rir;

    @ManyToOne
    @JsonIgnore
    private ExerciseLog exerciseLog;
}
