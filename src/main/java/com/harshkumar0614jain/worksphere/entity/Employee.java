package com.harshkumar0614jain.worksphere.entity;

import com.harshkumar0614jain.worksphere.enums.Department;
import com.harshkumar0614jain.worksphere.enums.EmployeeStatus;
import com.harshkumar0614jain.worksphere.enums.Gender;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.time.LocalDate;

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

    private String email;

    private LocalDate dateOfBirth;

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
