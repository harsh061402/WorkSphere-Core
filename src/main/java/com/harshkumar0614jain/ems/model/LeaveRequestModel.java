package com.harshkumar0614jain.ems.model;

import com.harshkumar0614jain.ems.enums.LeaveType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LeaveRequestModel {
    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Description is mandatory")
    private String description;

    @NotBlank(message = "Employee Id is mandatory")
    private String employeeId;

    @NotNull(message = "Start Date is mandatory")
    @FutureOrPresent(message = "Start Date must be future date")
    private LocalDate startDate;

    @NotNull(message = "End Date is mandatory")
    @Future(message = "End Date must be future date")
    private LocalDate endDate;

    @NotNull(message = "Leave type is mandatory")
    private LeaveType leaveType;
}
