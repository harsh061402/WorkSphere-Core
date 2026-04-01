package com.harshkumar0614jain.worksphere.model;

import com.harshkumar0614jain.worksphere.enums.LeaveStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LeaveDecisionRequestModel {
    @NotNull(message = "Leave Status is required")
    private LeaveStatus leaveStatus;
    private String managerComment;
}
