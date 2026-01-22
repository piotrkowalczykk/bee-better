package com.kowal.backend.customer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ExerciseLogResponse {
    private Long exerciseId;
    private String name;
    private String muscleGroup;
    private String image;
    private List<SetLogResponse> sets;
}
