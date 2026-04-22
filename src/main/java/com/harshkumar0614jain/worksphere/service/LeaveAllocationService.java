package com.harshkumar0614jain.worksphere.service;

import com.harshkumar0614jain.worksphere.entity.LeaveAllocation;
import com.harshkumar0614jain.worksphere.enums.LeaveType;
import com.harshkumar0614jain.worksphere.exception.BusinessException;
import com.harshkumar0614jain.worksphere.exception.ResourceAlreadyExistsException;
import com.harshkumar0614jain.worksphere.exception.ResourceNotFoundException;
import com.harshkumar0614jain.worksphere.model.LeaveAllocationRequestModel;
import com.harshkumar0614jain.worksphere.model.LeaveAllocationResponseModel;
import com.harshkumar0614jain.worksphere.repository.EmployeeRepository;
import com.harshkumar0614jain.worksphere.repository.LeaveAllocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveAllocationService {

    private final LeaveAllocationRepository leaveAllocationRepository;
    private final EmployeeRepository employeeRepository;

    // Map Leave Allocation entity → response
    private LeaveAllocationResponseModel mapToResponse (LeaveAllocation leaveAllocation){
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
    public List<LeaveAllocationResponseModel> getAllocationByEmployeeId(String employeeId){

        //        Check Employee Exists
        if(!employeeRepository.existsById(employeeId))
            throw new ResourceNotFoundException("employeeId",
                    "Employee Id not found with id : "+ employeeId);

        return leaveAllocationRepository.findByEmployeeId(employeeId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

//    Get specific allocation use it inside deductLeave and restoreLeave to avoid code duplication
    private LeaveAllocation getAllocationByEmployeeAndTypeAndYear(
            String employeeId, LeaveType leaveType, int year){

        return leaveAllocationRepository.findByEmployeeIdAndLeaveTypeAndYear(employeeId,leaveType,year)
                .orElseThrow(() -> new ResourceNotFoundException("leaveAllocation",
                        "No leave allocation found with this leave type and year"));
    }

//    Called when a leave Request is approved
    public void deductLeave(String employeeId, LeaveType leaveType, int year,int days){
        LeaveAllocation leaveAllocation = getAllocationByEmployeeAndTypeAndYear(employeeId,leaveType,year);

        if(leaveAllocation.getRemainingLeaves() < days)
            throw new BusinessException("leaveBalance",
                    "Insufficient leave balance. Remaining: " +
                            leaveAllocation.getRemainingLeaves() + " days, Requested: " + days + " days");

        leaveAllocation.setUsedLeaves(leaveAllocation.getUsedLeaves()+days);
        leaveAllocationRepository.save(leaveAllocation);
    }

//    Called when a leave Request is cancelled
    public void restoreLeave (String employeeId, LeaveType leaveType, int year, int days){
        LeaveAllocation leaveAllocation = getAllocationByEmployeeAndTypeAndYear(employeeId,leaveType,year);

        if(leaveAllocation.getUsedLeaves() < days)
            throw new BusinessException("leaveBalance","Cannot restore " + days + " days. Employee has only used "
                    + leaveAllocation.getUsedLeaves() + " days.");

        leaveAllocation.setUsedLeaves(leaveAllocation.getUsedLeaves()-days);
        leaveAllocationRepository.save(leaveAllocation);

    }


}
