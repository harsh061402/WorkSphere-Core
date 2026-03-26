package com.harshkumar0614jain.ems.entity;

import com.harshkumar0614jain.ems.enums.Department;
import com.harshkumar0614jain.ems.enums.EmployeeStatus;
import com.harshkumar0614jain.ems.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "employees")
public class Employee {

    @Id
    private String id;
    
    private String firstName;

    private String lastName;

    private Gender gender;

    @Indexed(unique = true)
    private String mobileNumber;

    private EmployeeStatus employeeStatus;

    private Department department;

    private String designation;

    private Long salary;

    private Address currentAddress;

    private Address permanentAddress;

    private String userId;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

}
