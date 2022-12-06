package com.example.accesscontrolsystem.mapper;

import com.example.accesscontrolsystem.model.entity.LeaveApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveApplicationMapper extends JpaRepository<LeaveApplication, Integer> {
    List<LeaveApplication> findAllByStudentId(Integer studentId);
    List<LeaveApplication> findAllByStudentIdAndStatus(Integer studentId, String status);
}
