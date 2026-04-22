package com.harshkumar0614jain.worksphere.service;

import com.harshkumar0614jain.worksphere.entity.LeaveAllocation;
import com.harshkumar0614jain.worksphere.entity.LeaveRequest;
import com.harshkumar0614jain.worksphere.enums.LeaveStatus;
import com.harshkumar0614jain.worksphere.exception.BusinessException;
import com.harshkumar0614jain.worksphere.exception.ResourceNotFoundException;
import com.harshkumar0614jain.worksphere.model.LeaveDecisionRequestModel;
import com.harshkumar0614jain.worksphere.model.LeaveRequestModel;
import com.harshkumar0614jain.worksphere.model.LeaveResponseModel;
import com.harshkumar0614jain.worksphere.repository.EmployeeRepository;
import com.harshkumar0614jain.worksphere.repository.LeaveAllocationRepository;
import com.harshkumar0614jain.worksphere.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveAllocationRepository allocationRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveAllocationService leaveAllocationService;


    private LeaveResponseModel mapToResponse(LeaveRequest leaveRequest) {
        return   LeaveResponseModel.builder()
                .id(leaveRequest.getId())
                .title(leaveRequest.getTitle())
                .description(leaveRequest.getDescription())
                .employeeId(leaveRequest.getEmployeeId())
                .startDate(leaveRequest.getStartDate())
                .endDate(leaveRequest.getEndDate())
                .leaveType(leaveRequest.getLeaveType())
                .leaveStatus(leaveRequest.getLeaveStatus())
                .createdAt(leaveRequest.getCreatedAt())
                .updatedAt(leaveRequest.getUpdatedAt())
                .managerComment(leaveRequest.getManagerComment())
                .build();

    }


    public LeaveResponseModel applyLeave(LeaveRequestModel requestModel){

//        Check employee exists
        if(!employeeRepository.existsById(requestModel.getEmployeeId())){
            throw new ResourceNotFoundException("employeeId",
                    "Employee not found with employee id: " + requestModel.getEmployeeId());
        }

//        Check Start date should be before End date
        long leaveDuration = ChronoUnit.DAYS.between(
                requestModel.getStartDate(),
                requestModel.getEndDate()) + 1;

        if (leaveDuration<=0) {
            throw new BusinessException("startDate","Start date should be before end date");
        }

//        fetch the remaining balance of the employee leave
        LeaveAllocation allocation = allocationRepository.findByEmployeeIdAndLeaveTypeAndYear(
                        requestModel.getEmployeeId(),
                        requestModel.getLeaveType(),
                        requestModel.getStartDate().getYear())
                .orElseThrow(()-> new ResourceNotFoundException("leaveAllocation",
                        "No allocation found for this leave type and year"));

        int remainingBalance = allocation.getRemainingLeaves();

//      if employee have to apply the leave he has to enough balance to applied it
        if (leaveDuration > remainingBalance) {
            throw new BusinessException("leaveBalance","Insufficient leave balance. " +
                    "Remaining: " + remainingBalance +
                    " days, Requested: " + leaveDuration + " days");
        }

//      Check no overlapping leave requests
        List<LeaveRequest> overlapping = leaveRequestRepository
                .findByEmployeeIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        requestModel.getEmployeeId(),
                        requestModel.getEndDate(),
                        requestModel.getStartDate());

        if(!overlapping.isEmpty()){
            throw new BusinessException("leaveRequest","Leave request overlaps with an existing request");
        }

        LeaveRequest leaveRequest = LeaveRequest.builder()
                .title(requestModel.getTitle())
                .description(requestModel.getDescription())
                .employeeId(requestModel.getEmployeeId())
                .startDate(requestModel.getStartDate())
                .endDate(requestModel.getEndDate())
                .leaveType(requestModel.getLeaveType())
                .leaveStatus(LeaveStatus.PENDING) // set status pending
                .build();

        LeaveRequest response = leaveRequestRepository.save(leaveRequest);

        return mapToResponse(response);
    }

    @Transactional
    public LeaveResponseModel approveOrRejectLeave(String leaveRequestId,
                                                   LeaveDecisionRequestModel requestModel) {
//      Check leave exists
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("leaveRequestId",
                        "Leave request not found of id :"+ leaveRequestId));

//      Check whether leave request current status is pending
        if(leaveRequest.getLeaveStatus()!=LeaveStatus.PENDING)
            throw new BusinessException("leaveStatus","Leave Status is not PENDING");

//      Check Request body status should not be pending
        if(requestModel.getLeaveStatus().equals(LeaveStatus.PENDING)){
            throw new BusinessException("leaveStatus","Leave Status is already PENDING");
        }

//      If leave -> Rejected then comment is required
        if((requestModel.getLeaveStatus()==LeaveStatus.REJECTED) &&
                (requestModel.getManagerComment()==null ))
            throw new BusinessException("managerComment",
                    "Manager comment is required when rejecting the leave request");


//      If leave -> APPROVED then change leave status + call deductLeaves()
        if(requestModel.getLeaveStatus() == LeaveStatus.APPROVED){
            long days = ChronoUnit.DAYS.between(
                    leaveRequest.getStartDate(),
                    leaveRequest.getEndDate()) + 1;

            leaveAllocationService.deductLeave(
                    leaveRequest.getEmployeeId(),
                    leaveRequest.getLeaveType(),
                    leaveRequest.getStartDate().getYear(),
                    (int) days);
        }

//      Set status & comment
        leaveRequest.setLeaveStatus(requestModel.getLeaveStatus());
        if (requestModel.getManagerComment() != null)
            leaveRequest.setManagerComment(requestModel.getManagerComment());


        leaveRequestRepository.save(leaveRequest);

        return mapToResponse(leaveRequest);
    }

    @Transactional
    public LeaveResponseModel cancelLeave(String leaveId){

//      Check leave exists
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveId)
                .orElseThrow(()-> new ResourceNotFoundException("leaveRequestId",
                        "No Leave Request was found with id :"+ leaveId));

//      Already closed — nothing to cancel
        if(leaveRequest.getLeaveStatus()==LeaveStatus.REJECTED)
            throw new BusinessException("leaveStatus","Cannot cancel a rejected leave request");

//      Already canceled — can't cancel twice
        if(leaveRequest.getLeaveStatus()==LeaveStatus.CANCELLED)
            throw new BusinessException("leaveStatus","Leave Status is already CANCELLED");

//      If leave approved -> restore leave first
        if (leaveRequest.getLeaveStatus() == LeaveStatus.APPROVED) {
            long days = ChronoUnit.DAYS.between(
                    leaveRequest.getStartDate(),
                    leaveRequest.getEndDate()) + 1;

            leaveAllocationService.restoreLeave(
                    leaveRequest.getEmployeeId(),
                    leaveRequest.getLeaveType(),
                    leaveRequest.getStartDate().getYear(),
                    (int) days);
        }

//      Set status to canceled
        leaveRequest.setLeaveStatus(LeaveStatus.CANCELLED);
        leaveRequestRepository.save(leaveRequest);

        return mapToResponse(leaveRequest);
    }

//  Fetch all employees leave request only for admins
    public List<LeaveResponseModel> findAllLeaveRequest() {
        return leaveRequestRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    public LeaveResponseModel getLeaveDetailsById(String leaveRequestId) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(()-> new ResourceNotFoundException("leaveRequestId",
                        "Leave request not found of id :"+ leaveRequestId));
        return mapToResponse(leaveRequest);
    }

    public List<LeaveResponseModel> findLeaveRequestByEmployeeId(String employeeId) {

//      Check employee exist
        if (!employeeRepository.existsById(employeeId))
            throw new ResourceNotFoundException("employeeId", "Employee not found with id :"+ employeeId);

        return leaveRequestRepository.findByEmployeeId(employeeId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
}
