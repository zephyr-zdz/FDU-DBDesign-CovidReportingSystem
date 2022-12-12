package com.example.accesscontrolsystem.service.superUser;

import com.example.accesscontrolsystem.manager.*;
import com.example.accesscontrolsystem.model.entity.*;
import com.example.accesscontrolsystem.model.entity.Class;
import com.example.accesscontrolsystem.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service("StudentApplicationService")
public class ApplicationService {
    private final EnterApplicationManager enterApplicationManager;
    private final LeaveApplicationManager leaveApplicationManager;
    private final StudentManager studentManager;
    private final ClassManager classManager;
    private final MajorManager majorManager;

    @Autowired
    public ApplicationService(EnterApplicationManager enterApplicationManager, LeaveApplicationManager leaveApplicationManager, StudentManager studentManager, ClassManager classManager, MajorManager majorManager) {
        this.enterApplicationManager = enterApplicationManager;
        this.leaveApplicationManager = leaveApplicationManager;
        this.studentManager = studentManager;
        this.classManager = classManager;
        this.majorManager = majorManager;
    }
    private Response<List<EnterApplication>> getMyStudentEnterApplications(Integer studentId, List<Student> students) {
        if (studentId == -1) { // all
            return new Response<>(Response.SUCCESS,"获取成功",enterApplicationManager.findAllByStudents(students));
        }
        Student student = studentManager.findStudentById(studentId); // single
        if (students.contains(student)) {
            return new Response<>(Response.SUCCESS, "获取成功", enterApplicationManager.findAllByStudentId(studentId));
        } else {
            return new Response<>(Response.FAIL,"你没有权限",null);
        }
    }
    public Response<List<EnterApplication>> getEnterApplications(Integer classId, Integer schoolId, Integer studentId, String status) {
        if (classId == -1 && schoolId == -1) { // super admin
            if (studentId == -1 && Objects.equals(status, "")) {
                return new Response<>(Response.SUCCESS, "获取成功", enterApplicationManager.findAll());
            } else if (Objects.equals(status, "")) {
                return new Response<>(Response.SUCCESS, "获取成功", enterApplicationManager.findAllByStudentId(studentId));
            } else if (studentId == -1) {
                return new Response<>(Response.SUCCESS, "获取成功", enterApplicationManager.findAllByStatus(status));
            } else {
                return new Response<>(Response.SUCCESS, "获取成功", enterApplicationManager.findAllByStudentIdAndStatus(studentId, status));
            }
        } else if (classId == -1) { // school admin
            Major major = majorManager.findMajorById(schoolId);
            List<Student> students = studentManager.findAllByMajorId(major.getId());
            return getMyStudentEnterApplications(studentId, students);
        } else if (classId > 0) { // counselor
            Class clazz = classManager.findClassById(classId);
            List<Student> students = studentManager.findAllByClassId(clazz.getId());
            return getMyStudentEnterApplications(studentId, students);
        } else {
            return new Response<>(Response.FAIL, "获取失败", null);
        }
    }
    private Response<List<LeaveApplication>> getMyStudentLeaveApplications(Integer studentId, List<Student> students) {
        if (studentId == -1) { // all
            return new Response<>(Response.SUCCESS,"获取成功",leaveApplicationManager.findAllByStudents(students));
        }
        Student student = studentManager.findStudentById(studentId); // single
        if (students.contains(student)) {
            return new Response<>(Response.SUCCESS, "获取成功", leaveApplicationManager.findAllByStudentId(studentId));
        } else {
            return new Response<>(Response.FAIL,"你没有权限",null);
        }
    }
    public Response<List<LeaveApplication>> getLeaveApplications(Integer classId, Integer schoolId, Integer studentId, String status) {
        if (classId == -1 && schoolId == -1) { // super admin
            if (studentId == -1 && Objects.equals(status, "")) {
                return new Response<>(Response.SUCCESS, "获取成功", leaveApplicationManager.findAll());
            } else if (Objects.equals(status, "")) {
                return new Response<>(Response.SUCCESS, "获取成功", leaveApplicationManager.findAllByStudentId(studentId));
            } else if (studentId == -1) {
                return new Response<>(Response.SUCCESS, "获取成功", leaveApplicationManager.findAllByStatus(status));
            } else {
                return new Response<>(Response.SUCCESS, "获取成功", leaveApplicationManager.findAllByStudentIdAndStatus(studentId, status));
            }
        } else if (classId == -1) { // school admin
            Major major = majorManager.findMajorById(schoolId);
            List<Student> students = studentManager.findAllByMajorId(major.getId());
            return getMyStudentLeaveApplications(studentId, students);
        } else if (classId > 0) { // counselor
            Class clazz = classManager.findClassById(classId);
            List<Student> students = studentManager.findAllByClassId(clazz.getId());
            return getMyStudentLeaveApplications(studentId, students);
        } else {
            return new Response<>(Response.FAIL, "获取失败", null);
        }
    }
}
