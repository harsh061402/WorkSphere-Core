package com.harshkumar0614jain.worksphere.model;

import com.harshkumar0614jain.worksphere.enums.LeaveType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LeaveAllocationRequestModel {

    @NotBlank(message = "Employee Id is required")
    private String employeeId;

    @NotNull(message = "Leave type is required")
    private LeaveType leaveType;

    @Min(value = 1,message = "Total leaves must be at least 1")
    private int totalLeaves;

    @Min(value = 2025,message = "Year is required")
    private int year;
}
