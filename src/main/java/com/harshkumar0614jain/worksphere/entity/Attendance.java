package com.harshkumar0614jain.worksphere.entity;

import com.harshkumar0614jain.worksphere.enums.AttendanceStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "attendance")
public class Attendance {
    @Id
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

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
