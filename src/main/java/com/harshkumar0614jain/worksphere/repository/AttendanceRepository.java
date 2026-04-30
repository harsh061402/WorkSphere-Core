package com.harshkumar0614jain.worksphere.repository;

import com.harshkumar0614jain.worksphere.entity.Attendance;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends MongoRepository<Attendance,String> {

    List<Attendance> findByEmployeeId(String employeeId);
    List<Attendance> findByEmployeeIdAndDateBetween(String employeeId,LocalDate startDate, LocalDate endDate);
    Optional<Attendance> findByEmployeeIdAndDate (String employeeId, LocalDate Date);
    boolean existsByEmployeeIdAndDate(String employeeId, LocalDate date);
}
