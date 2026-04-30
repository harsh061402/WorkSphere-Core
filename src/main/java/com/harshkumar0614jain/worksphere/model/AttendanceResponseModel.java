package com.harshkumar0614jain.worksphere.model;

import com.harshkumar0614jain.worksphere.enums.AttendanceStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class AttendanceResponseModel {
    private String id;
    private String employeeId;
    private LocalDate date;
    private LocalTime clockInTime;
    private LocalTime clockOutTime;
    private Double totalHours;
    private Double overtimeHours;
    private boolean isLate;
    private Long lateByMinutes;
    private AttendanceStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}
