package com.example.accesscontrolsystem.service.student;

import com.example.accesscontrolsystem.manager.DailyReportManager;
import com.example.accesscontrolsystem.manager.StudentManager;
import com.example.accesscontrolsystem.model.entity.reportNlog.DailyReport;
import com.example.accesscontrolsystem.service.TimeService;
import com.example.accesscontrolsystem.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("StudentDailyReportService")
public class DailyReportService {
    private final DailyReportManager dailyReportManager;
    private final StudentManager studentManager;
    private final TimeService timeService;
    @Autowired
    public DailyReportService(DailyReportManager dailyReportManager, StudentManager studentManager, TimeService timeService) {
        this.dailyReportManager = dailyReportManager;
        this.studentManager = studentManager;
        this.timeService = timeService;
    }
    public Response<List<DailyReport>> getDailyReports(Integer studentId) {
        if (studentId == null) {
            return new Response<>(Response.FAIL, "studentId为空", null);
        }
        if (studentManager.findStudentById(studentId) == null) {
            return new Response<>(Response.FAIL, "学生不存在", null);
        }
        return new Response<>(Response.SUCCESS, "成功", dailyReportManager.findAllByStudentId(studentId));
    }

    public Response<DailyReport> addDailyReport(DailyReport dailyReport) {
        if (dailyReport.getStudentId() == null) {
            return new Response<>(Response.FAIL, "studentId为空", null);
        }
        if (studentManager.findStudentById(dailyReport.getStudentId()) == null) {
            return new Response<>(Response.FAIL, "学生不存在", null);
        }
        if (dailyReportManager.getDailyReportByStudentIdAndDate(dailyReport.getStudentId(), timeService.getDate()) != null) {
            return new Response<>(Response.FAIL, "今日已填写", null);
        }
        dailyReportManager.addDailyReport(dailyReport);
        return new Response<>(Response.SUCCESS, "成功", dailyReport);
    }
}
