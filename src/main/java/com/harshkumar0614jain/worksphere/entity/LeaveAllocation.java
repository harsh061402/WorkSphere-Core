package com.harshkumar0614jain.worksphere.entity;

import com.harshkumar0614jain.worksphere.enums.LeaveType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "leave_allocations")
public class LeaveAllocation {

    @Id
    private String id;
    private String employeeId;
    private LeaveType leaveType;
    private int totalLeaves;
    private int usedLeaves;
    private int year;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    public int getRemainingLeaves() {
        return totalLeaves - usedLeaves;
    }
}
