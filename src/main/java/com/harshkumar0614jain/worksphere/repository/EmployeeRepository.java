package com.harshkumar0614jain.worksphere.repository;

import com.harshkumar0614jain.worksphere.entity.Employee;
import com.harshkumar0614jain.worksphere.enums.Department;
import com.harshkumar0614jain.worksphere.enums.EmployeeStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EmployeeRepository extends MongoRepository<Employee, String> {
    boolean existsByMobileNumber(String mobileNumber);

    boolean existsByUserId (String userId);

    List<Employee> findByDepartmentAndEmployeeStatusNot(Department department, EmployeeStatus status);

    List<Employee> findByEmployeeStatusNot(EmployeeStatus status);
}
