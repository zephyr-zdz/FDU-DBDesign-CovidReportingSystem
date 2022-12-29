package com.example.accesscontrolsystem.controller.userManagement;

import com.example.accesscontrolsystem.model.entity.user.Student;
import com.example.accesscontrolsystem.service.StudentDataService;
import com.example.accesscontrolsystem.service.UserService;
import com.example.accesscontrolsystem.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("SuperUserStudentController")
@RequestMapping("/student")
public class StudentController {
    private final UserService userservice;
    private final StudentDataService studentDataService;
    @Autowired
    public StudentController(UserService userservice, StudentDataService studentDataService) {
        this.userservice = userservice;
        this.studentDataService = studentDataService;
    }
    @GetMapping("/all")
    public Response<List<Student>> getStudents() {
        return userservice.getStudents();
    }
/*
    @GetMapping("/part")
    public Response<List<Student>> getPartOfStudents(@RequestParam Integer schoolId, Integer classId) { // TODO
        return userservice.getStudents();
    }
*/
    @GetMapping("/outside-duration/{studentId}")
    public Response<Double> getStudentOutsideDuration(@PathVariable Integer studentId) { // hours 一年内
        return studentDataService.getStudentOutsideDuration(studentId);
    }
    @GetMapping("/otaku/") // n天未离校
    public Response<List<Student>> getEnterApplicationsByStudentId(Integer classId,
                                                                   Integer schoolId,
                                                                   Integer n) {
        return studentDataService.getNDaysOtakus(classId, schoolId, n);
    }
}
