package com.harshkumar0614jain.ems.service;

import com.harshkumar0614jain.ems.entity.LeaveAllocation;
import com.harshkumar0614jain.ems.enums.LeaveType;
import com.harshkumar0614jain.ems.exception.ResourceAlreadyExistsException;
import com.harshkumar0614jain.ems.exception.ResourceNotFoundException;
import com.harshkumar0614jain.ems.model.LeaveAllocationRequestModel;
import com.harshkumar0614jain.ems.model.LeaveAllocationResponseModel;
import com.harshkumar0614jain.ems.repository.EmployeeRepository;
import com.harshkumar0614jain.ems.repository.LeaveAllocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveAllocationService {

    @Autowired
    private LeaveAllocationRepository leaveAllocationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    // Map Leave Allocation entity → response
    public LeaveAllocationResponseModel mapToResponse (LeaveAllocation leaveAllocation){
        return LeaveAllocationResponseModel.builder()
                .id(leaveAllocation.getId())
                .employeeId(leaveAllocation.getEmployeeId())
                .leaveType(leaveAllocation.getLeaveType())
                .totalLeaves(leaveAllocation.getTotalLeaves())
                .usedLeaves(leaveAllocation.getUsedLeaves())
                .remainingLeaves(leaveAllocation.getRemainingLeaves())
                .year(leaveAllocation.getYear())
                .createdAt(leaveAllocation.getCreatedAt())
                .updatedAt(leaveAllocation.getUpdatedAt())
                .build();
    }

//    Admin assigns leave quota to the employee
    public LeaveAllocationResponseModel allocateLeave(LeaveAllocationRequestModel requestModel){

//        Check Employee Exists
        if(!employeeRepository.existsById(requestModel.getEmployeeId()))
            throw new ResourceNotFoundException("employeeId",
                    "Employee Id not found with id : "+ requestModel.getEmployeeId());

//        Check duplicate allocation with same employee id & same leave type & same year
        if(leaveAllocationRepository.existsByEmployeeIdAndLeaveTypeAndYear(
                requestModel.getEmployeeId(),
                requestModel.getLeaveType(),
                requestModel.getYear()))
            throw new ResourceAlreadyExistsException("leaveAllocation","Leave Allocation already exists for this leave type and year");

        LeaveAllocation leaveAllocation = LeaveAllocation.builder()
                .employeeId(requestModel.getEmployeeId())
                .leaveType(requestModel.getLeaveType())
                .totalLeaves(requestModel.getTotalLeaves())
                .usedLeaves(0)
                .year(requestModel.getYear())
                .build();

        LeaveAllocation allocation= leaveAllocationRepository.save(leaveAllocation);

        return mapToResponse(allocation);
    }

//    Get all allocation for the employee
    public List<LeaveAllocationResponseModel> getAllocationByEmployee(String employeeId){

        //        Check Employee Exists
        if(!employeeRepository.existsById(employeeId))
            throw new ResourceNotFoundException("employeeId",
                    "Employee Id not found with id : "+ employeeId);

        return leaveAllocationRepository.findByEmployeeId(employeeId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

//    Get specific allocation
    public LeaveAllocation getAllocationByEmployeeAndTypeAndYear(
            String employeeId, LeaveType leaveType, int year){

        return leaveAllocationRepository.findByEmployeeIdAndLeaveTypeAndYear(employeeId,leaveType,year)
                .orElseThrow(() -> new ResourceNotFoundException("leaveAllocation",
                        "No leave allocation found with this leave type and year"));
    }

//    Called when a leave Request is approved
    public void deductLeave(String employeeId, LeaveType leaveType, int year,int days){
        LeaveAllocation leaveAllocation = leaveAllocationRepository.findByEmployeeIdAndLeaveTypeAndYear(
                employeeId, leaveType,  year)
                .orElseThrow(()-> new ResourceNotFoundException("leaveAllocation",
                        "No allocation found with this leave type and year"));

        if(leaveAllocation.getRemainingLeaves() < days)
            throw new RuntimeException("Not enough leave days");

        leaveAllocation.setUsedLeaves(leaveAllocation.getUsedLeaves()+days);
        leaveAllocationRepository.save(leaveAllocation);
    }

//    Called when a leave Request is canceled
    public void restoreLeave (String employeeId, LeaveType leaveType, int year, int days){
        LeaveAllocation leaveAllocation = leaveAllocationRepository.findByEmployeeIdAndLeaveTypeAndYear(
                employeeId, leaveType, year)
                .orElseThrow(()-> new ResourceNotFoundException("leaveAllocation",
                        "No allocation found with this leave type and year"));

        if(leaveAllocation.getUsedLeaves() < days)
            throw new RuntimeException("Cannot restore " + days + " days. Employee has only used "
                    + leaveAllocation.getUsedLeaves() + " days.");

        leaveAllocation.setUsedLeaves(leaveAllocation.getUsedLeaves()-days);
        leaveAllocationRepository.save(leaveAllocation);

    }


}
