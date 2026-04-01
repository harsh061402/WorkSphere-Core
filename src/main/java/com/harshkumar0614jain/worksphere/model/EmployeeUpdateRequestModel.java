package com.harshkumar0614jain.worksphere.model;

import com.harshkumar0614jain.worksphere.enums.Department;
import com.harshkumar0614jain.worksphere.enums.EmployeeStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class EmployeeUpdateRequestModel {

    @Pattern(regexp = "^(\\+91[ ]?)?[6-9][0-9]{4}[0-9 ]{5,6}$",
            message = "Invalid Indian mobile number")
    private String mobileNumber;

    private EmployeeStatus employeeStatus;

    private Department department;

    private String designation;

    @Min(value = 0,message = "Salary must be positive")
    private Long salary;

    private AddressModel currentAddress;
}
