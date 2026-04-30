package com.harshkumar0614jain.worksphere.controller;

import com.harshkumar0614jain.worksphere.model.AttendanceRequestModel;
import com.harshkumar0614jain.worksphere.model.AttendanceResponseModel;
import com.harshkumar0614jain.worksphere.model.ResponseModel;
import com.harshkumar0614jain.worksphere.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Attendance Management", description = "APIs for managing attendance")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Operation(summary = "Clock in for today")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee clocked in successfully"),
            @ApiResponse(responseCode = "400", description = "Already clocked in or validation failed"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping("/clock-in")
    public ResponseEntity<ResponseModel<AttendanceResponseModel>> clockIn(
            @Valid @RequestBody AttendanceRequestModel request) {
        AttendanceResponseModel attendance = attendanceService.clockIn(request);
        ResponseModel<AttendanceResponseModel> response = new ResponseModel<>(
                "Employee successfully clocked in", attendance);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Clock out for today")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee clocked out successfully"),
            @ApiResponse(responseCode = "400", description = "Not clocked in or already clocked out"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping("/clock-out")
    public ResponseEntity<ResponseModel<AttendanceResponseModel>> clockOut(
            @Valid @RequestBody AttendanceRequestModel request) {
        AttendanceResponseModel attendance = attendanceService.clockOut(request);
        ResponseModel<AttendanceResponseModel> response = new ResponseModel<>(
                "Employee successfully clocked out", attendance);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get all attendance records by employee ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attendance records retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/{employeeId}")
    public ResponseEntity<ResponseModel<List<AttendanceResponseModel>>> getAllAttendanceByEmployee(
            @PathVariable String employeeId) {
        List<AttendanceResponseModel> attendanceList = attendanceService
                .getAttendanceByEmployee(employeeId);
        ResponseModel<List<AttendanceResponseModel>> response = new ResponseModel<>(
                "Employee attendance list successfully retrieved", attendanceList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get attendance records by date range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attendance records retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid date range"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/{employeeId}/range")
    public ResponseEntity<ResponseModel<List<AttendanceResponseModel>>> getAttendanceByDateRange(
            @PathVariable String employeeId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<AttendanceResponseModel> attendanceList = attendanceService
                .getAttendanceByDateRange(employeeId, startDate, endDate);
        ResponseModel<List<AttendanceResponseModel>> response = new ResponseModel<>(
                "Employee attendance list successfully retrieved", attendanceList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}