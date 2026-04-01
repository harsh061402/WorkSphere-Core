package com.harshkumar0614jain.worksphere.model;

import com.harshkumar0614jain.worksphere.enums.LeaveStatus;
import com.harshkumar0614jain.worksphere.enums.LeaveType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveResponseModel {
    private String id;
    private String title;
    private String description;
    private String employeeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private LeaveType leaveType;
    private LeaveStatus leaveStatus;
    private Instant createdAt;
    private Instant updatedAt;
    private String managerComment;
}
