package com.example.accesscontrolsystem.service;

import com.example.accesscontrolsystem.manager.DailyReportManager;
import com.example.accesscontrolsystem.manager.GateLogManager;
import com.example.accesscontrolsystem.manager.LeaveApplicationManager;
import com.example.accesscontrolsystem.manager.StudentManager;
import com.example.accesscontrolsystem.model.entity.reportNlog.GateLog;
import com.example.accesscontrolsystem.model.entity.user.Student;
import com.example.accesscontrolsystem.model.vo.StudentWithLeaveTime;
import com.example.accesscontrolsystem.service.system.TimeService;
import com.example.accesscontrolsystem.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

@Service("StudentDataService")

public class StudentDataService {
    private final GateLogManager gateLogManager;
    private final DailyReportManager dailyReportManager;
    private final LeaveApplicationManager leaveApplicationManager;
    private final TimeService timeService;
    private final StudentManager studentManager;

    @Autowired
    public StudentDataService(GateLogManager gateLogManager, DailyReportManager dailyReportManager, LeaveApplicationManager leaveApplicationManager, TimeService timeService,
                              StudentManager studentManager) {
        this.gateLogManager = gateLogManager;
        this.dailyReportManager = dailyReportManager;
        this.leaveApplicationManager = leaveApplicationManager;
        this.timeService = timeService;
        this.studentManager = studentManager;
    }
    public Response<Double> getStudentOutsideDuration(Integer classId, Integer schoolId, Integer studentId) { // hours
        Student student;
        if (schoolId == -1) {
            student = studentManager.findStudentById(studentId);
        } else if (classId == -1) {
            student = studentManager.findStudentByIdAndSchoolId(studentId, schoolId);
        } else {
            student = studentManager.findStudentByIdAndClassId(studentId, classId);
        }
        if (student == null) {
            return new Response<>(Response.FAIL, "学生不存在", null);
        }
        long duration = 0;
        List<GateLog> gateLogs = gateLogManager.getOneYearGateLogsByStudentId(student.getId());
        long start = timeService.getTimeNDaysBefore(Objects.equals(student.getStatus(), "in") ? 0 : -1) - 100;
        long end = timeService.getTime();
        for (GateLog gateLog : gateLogs) {
            if (gateLog.getDirection().equals("out")) {
                start = gateLog.getTime();
                duration += end - start;
            }
            if (gateLog.getDirection().equals("in")) {
                end = gateLog.getTime();
            }
        }
        if (end < start) {
            duration += end - timeService.getTimeNDaysBefore(365);
        }
        return duration == 0 ? new Response<>(Response.SUCCESS,"成功",0.0) : new Response<>(Response.SUCCESS, "成功", new BigDecimal(duration / 3600000.0).setScale(2, RoundingMode.HALF_UP).doubleValue());
    }

    public Response<List<Student>> getNDaysOtakus(Integer classId, Integer schoolId, Integer n) {
        if (schoolId == -1) { // super admin
            return new Response<>(Response.SUCCESS, "获取成功", gateLogManager.findNDaysOtakus(n));
        } else if (classId == -1) { // school admin
            return new Response<>(Response.SUCCESS, "获取成功", gateLogManager.findNDaysOtakusBySchoolId(n, schoolId));
        } else if (classId > 0) { // counsellor
            return new Response<>(Response.SUCCESS, "获取成功", gateLogManager.findNDaysOtakusByMajorId(n, classId));
        } else {
            return new Response<>(Response.FAIL, "获取失败", null);
        }
    }
    public Response<List<Student>> catchNDaysScriptKiddies(Integer classId, Integer schoolId, Integer n) {
        if (schoolId == -1) { // super admin
            return new Response<>(Response.SUCCESS, "获取成功", dailyReportManager.catchNDaysScriptKiddies(n));
        } else if (classId == -1) { // school admin
            return new Response<>(Response.SUCCESS, "获取成功", dailyReportManager.catchNDaysScriptKiddiesBySchoolId(n, schoolId));
        } else if (classId > 0) { // counsellor
            return new Response<>(Response.SUCCESS, "获取成功", dailyReportManager.catchNDaysScriptKiddiesByClassId(n, classId));
        } else {
            return new Response<>(Response.FAIL, "获取失败", null);
        }
    }

    public Response<List<Student>> getAppliedButNotLeaved(Integer classId, Integer schoolId) {
        if (schoolId == -1) { // super admin
            return new Response<>(Response.SUCCESS, "获取成功", leaveApplicationManager.findAppliedButNotLeaved());
        } else if (classId == -1) { // school admin
            return new Response<>(Response.SUCCESS, "获取成功", leaveApplicationManager.findAppliedButNotLeavedBySchoolId(schoolId));
        } else if (classId > 0) { // counsellor
            return new Response<>(Response.SUCCESS, "获取成功", leaveApplicationManager.findAppliedButNotLeavedByClassId(classId));
        } else {
            return new Response<>(Response.FAIL, "获取失败", null);
        }
    }

    public Response<List<Student>> getLeaved24hrsButNotApplied(Integer classId, Integer schoolId) {
        if (schoolId == -1) { // super admin
            return new Response<>(Response.SUCCESS, "获取成功", leaveApplicationManager.findLeaved24hrsButNotApplied());
        } else if (classId == -1) { // school admin
            return new Response<>(Response.SUCCESS, "获取成功", leaveApplicationManager.findLeaved24hrsButNotAppliedBySchoolId(schoolId));
        } else if (classId > 0) { // counsellor
            return new Response<>(Response.SUCCESS, "获取成功", leaveApplicationManager.findLeaved24hrsButNotAppliedByClassId(classId));
        } else {
            return new Response<>(Response.FAIL, "获取失败", null);
        }
    }

    public Response<List<StudentWithLeaveTime>> getOutsideStudents(Integer classId, Integer schoolId) {
        if (schoolId == -1) { // super admin
            return new Response<>(Response.SUCCESS, "获取成功", leaveApplicationManager.findOutsideStudents());
        } else if (classId == -1) { // school admin
            return new Response<>(Response.SUCCESS, "获取成功", leaveApplicationManager.findOutsideStudentsBySchoolId(schoolId));
        } else if (classId > 0) { // counsellor
            return new Response<>(Response.SUCCESS, "获取成功", leaveApplicationManager.findOutsideStudentsByClassId(classId));
        } else {
            return new Response<>(Response.FAIL, "获取失败", null);
        }
    }

    public Response<List<Student>> getNStudentsWithMostLeaveTime(Integer classId, Integer schoolId, Integer n) {
        if (schoolId == -1) { // super admin
            return new Response<>(Response.SUCCESS, "获取成功", leaveApplicationManager.findNStudentsWithMostLeaveTime(n));
        } else if (classId == -1) { // school admin
            return new Response<>(Response.SUCCESS, "获取成功", leaveApplicationManager.findNStudentsWithMostLeaveTimeBySchoolId(n, schoolId));
        } else if (classId > 0) { // counsellor
            return new Response<>(Response.SUCCESS, "获取成功", leaveApplicationManager.findNStudentsWithMostLeaveTimeByClassId(n, classId));
        } else {
            return new Response<>(Response.FAIL, "获取失败", null);
        }
    }

    public Response<List<Student>> getNStudentsWithLongestAvgOutsideTime(Integer classId, Integer schoolId, Integer n) {
        if (schoolId == -1) { // super admin
            return new Response<>(Response.SUCCESS, "获取成功", leaveApplicationManager.findNStudentsWithLongestAvgOutsideTime(n));
        } else if (classId == -1) { // school admin
            return new Response<>(Response.SUCCESS, "获取成功", leaveApplicationManager.findNStudentsWithLongestAvgOutsideTimeBySchoolId(n, schoolId));
        } else if (classId > 0) { // counsellor
            return new Response<>(Response.SUCCESS, "获取成功", leaveApplicationManager.findNStudentsWithLongestAvgOutsideTimeByClassId(n, classId));
        } else {
            return new Response<>(Response.FAIL, "获取失败", null);
        }
    }
}
