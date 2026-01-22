package com.kowal.backend.customer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SetLogResponse {
    private int setNumber;
    private int reps;
    private double weight;
    private int rir;
}