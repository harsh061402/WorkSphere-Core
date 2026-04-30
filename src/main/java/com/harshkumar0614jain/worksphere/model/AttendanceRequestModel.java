package com.harshkumar0614jain.worksphere.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AttendanceRequestModel {
    @NotBlank(message = "Employee Id is required")
    private String employeeId;
}
