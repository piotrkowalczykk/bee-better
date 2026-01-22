package com.kowal.backend.customer.dto.request;

import lombok.Data;

@Data
public class SetLogRequest{
    private Integer setNumber;
    private Integer reps;
    private double weight;
    private Integer rir;
}

