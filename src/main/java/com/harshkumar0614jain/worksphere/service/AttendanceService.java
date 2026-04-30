package com.harshkumar0614jain.worksphere.service;

import com.harshkumar0614jain.worksphere.entity.Attendance;
import com.harshkumar0614jain.worksphere.enums.AttendanceStatus;
import com.harshkumar0614jain.worksphere.exception.BusinessException;
import com.harshkumar0614jain.worksphere.exception.ResourceNotFoundException;
import com.harshkumar0614jain.worksphere.model.AttendanceRequestModel;
import com.harshkumar0614jain.worksphere.model.AttendanceResponseModel;
import com.harshkumar0614jain.worksphere.repository.AttendanceRepository;
import com.harshkumar0614jain.worksphere.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;


    @Value("${attendance.standard-hours}")
    private double standardHours;

    @Value("${attendance.half-day-hours}")
    private double halfDayHours;

    @Value("${attendance.work-start-time}")
    private String workStartTime;

    @Value("${attendance.grace-period-minutes}")
    private int gracePeriodMinutes;

    public AttendanceResponseModel clockIn(AttendanceRequestModel request){

//        Check employee exists
        employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(
                        ()-> new ResourceNotFoundException(
                                "employeeId","Employee not found with id :- " + request.getEmployeeId()));

//        Check employee hasn't already clocked in today
        if(attendanceRepository.existsByEmployeeIdAndDate(request.getEmployeeId(), LocalDate.now()))
            throw new BusinessException("employeeId","Employee already clock in");

        LocalTime currentTime = LocalTime.now(ZoneId.of("Asia/Kolkata"));

        Attendance attendance = Attendance.builder()
                .employeeId(request.getEmployeeId())
                .date(LocalDate.now())
                .clockInTime(currentTime)
                .clockOutTime(null)
                .totalHours(0D)
                .overtimeHours(0D)
                .isLate(false)
                .lateByMinutes(0L)
                .status(AttendanceStatus.INCOMPLETE)
                .build();

        LocalTime workStart = LocalTime.parse(workStartTime);
        LocalTime graceEnd = workStart.plusMinutes(gracePeriodMinutes);

        if(currentTime.isAfter(graceEnd)){
            attendance.setLate(true);
            Long lateByMinutes = ChronoUnit.MINUTES
                    .between(workStart ,currentTime);

            attendance.setLateByMinutes(lateByMinutes);
        }
        attendanceRepository.save(attendance);
        return mapToResponse(attendance);
    }

    public AttendanceResponseModel clockOut(AttendanceRequestModel request){

        LocalDate currentDate = LocalDate.now();
//        Check employee exists
        employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(()-> new ResourceNotFoundException("employeeId",
                        "Employee not found with this id :- " + request.getEmployeeId())
                );

//        Today's attendance exists check
        Attendance attendance = attendanceRepository.findByEmployeeIdAndDate(
                request.getEmployeeId(),currentDate)
                .orElseThrow(()-> new ResourceNotFoundException("attendanceId",
                        "Today's attendance not found with employee id:- " + request.getEmployeeId())
                );

//        Check employee has clocked in
        if(attendance.getClockInTime() == null)
            throw new BusinessException("clockInTime",
                    "Employee has not clocked in today");

//        Check employee has already clocked out or not
        if(attendance.getClockOutTime()!=null)
            throw new BusinessException("clockOutTime",
                    "Employee has already clocked out");

        LocalTime currentTime = LocalTime.now(ZoneId.of("Asia/Kolkata"));
        attendance.setClockOutTime(currentTime);
        double totalWorkHours = (double) ChronoUnit.MINUTES.between(
                attendance.getClockInTime(),
                attendance.getClockOutTime()) / 60.0;
        attendance.setTotalHours(totalWorkHours);

        if(totalWorkHours >= standardHours) {
            attendance.setStatus(AttendanceStatus.PRESENT);
            attendance.setOvertimeHours(totalWorkHours-standardHours);
        } else if(totalWorkHours >= halfDayHours) {
            attendance.setStatus(AttendanceStatus.HALF_DAY);
        } else {
            attendance.setStatus(AttendanceStatus.INCOMPLETE);
        }

        attendanceRepository.save(attendance);

        return mapToResponse(attendance);

    }


    public List<AttendanceResponseModel> getAttendanceByEmployee(String employeeId){

        if(!employeeRepository.existsById(employeeId))
            throw new ResourceNotFoundException("employeeId",
                    "Employee not found with id :- " + employeeId);

        return attendanceRepository.findByEmployeeId(employeeId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<AttendanceResponseModel> getAttendanceByDateRange(String employeeId,
                                                                  LocalDate startDate,
                                                                  LocalDate endDate){
        if(!employeeRepository.existsById(employeeId))
            throw new ResourceNotFoundException("employeeId",
                    "Employee not found with id :- " + employeeId);

        if(startDate.isAfter(endDate))
            throw new BusinessException("startDate",
                    "Start Date must be before end date");

        return attendanceRepository.findByEmployeeIdAndDateBetween(
                employeeId, startDate,endDate).stream()
                .map(this::mapToResponse)
                .toList();
    }

    private AttendanceResponseModel mapToResponse(Attendance attendance) {
        return AttendanceResponseModel.builder()
                .id(attendance.getId())
                .employeeId(attendance.getEmployeeId())
                .date(attendance.getDate())
                .clockInTime(attendance.getClockInTime())
                .clockOutTime(attendance.getClockOutTime())
                .totalHours(attendance.getTotalHours())
                .overtimeHours(attendance.getOvertimeHours())
                .isLate(attendance.isLate())
                .lateByMinutes(attendance.getLateByMinutes())
                .status(attendance.getStatus())
                .createdAt(attendance.getCreatedAt())
                .updatedAt(attendance.getUpdatedAt())
                .build();
    }
}
