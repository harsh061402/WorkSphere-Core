package com.harshkumar0614jain.ems.repository;

import com.harshkumar0614jain.ems.entity.LeaveAllocation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LeaveAllocationRepository extends MongoRepository<LeaveAllocation,String> {
//    List<LeaveAllocation> findByEmployeeId(String employeeId);
//
//    Optional<LeaveAllocation> findByEmployeeIdAndLeaveTypeAndYear(
//            String employeeId, LeaveType leaveType, int year);
}
