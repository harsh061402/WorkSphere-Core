package com.harshkumar0614jain.ems.entity;

import com.harshkumar0614jain.ems.enums.LeaveStatus;
import com.harshkumar0614jain.ems.enums.LeaveType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "leave_requests")
public class LeaveRequest {
    @Id
    private String id;

    private String title;

    private String description;

    private String employeeId;

    private LocalDate startDate;

    private LocalDate endDate;

    private LeaveType leaveType;

    private LeaveStatus leaveStatus;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    private String managerComment;
}
