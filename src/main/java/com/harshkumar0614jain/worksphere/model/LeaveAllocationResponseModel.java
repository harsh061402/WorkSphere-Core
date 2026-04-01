package com.harshkumar0614jain.worksphere.model;

import com.harshkumar0614jain.worksphere.enums.LeaveType;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class LeaveAllocationResponseModel {
    private String id;
    private String employeeId;
    private LeaveType leaveType;
    private int totalLeaves;
    private int usedLeaves;
    private int remainingLeaves;
    private int year;
    private Instant createdAt;
    private Instant updatedAt;
}
