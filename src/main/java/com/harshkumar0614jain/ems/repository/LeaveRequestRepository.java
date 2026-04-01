package com.harshkumar0614jain.ems.repository;

import com.harshkumar0614jain.ems.entity.LeaveRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;


public interface LeaveRequestRepository extends MongoRepository<LeaveRequest,String> {

    List<LeaveRequest> findByEmployeeId(String employeeId);

    // Used to check for overlapping leave requests
    List<LeaveRequest> findByEmployeeIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            String employeeId, LocalDate endDate, LocalDate startDate);

}
