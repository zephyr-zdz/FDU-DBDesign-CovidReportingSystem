package com.example.accesscontrolsystem.model.entity.reportNlog;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "enter_application")
public class EnterApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "passing-areas", nullable = false, length = 45)
    private String passingAreas;// 虹口区, 杨浦区, 闵行区 注意是英文逗号分隔
    @Column(name = "enter-time", nullable = false)
    private String enterTime; // utf-8编码的时间字符串，例如：2020-12-12 12:12:12
    @Column(name = "status", nullable = false, length = 10)
    private String status; // pending/accepted/rejected
    @Column(name = "student-id", nullable = false)
    private Integer studentId;
    @Column(name = "class-id", nullable = false) // 辅助索引
    private Integer classId;
    @Column(name = "major-id", nullable = false) // 辅助索引
    private Integer majorId;
    @Column(name = "date", nullable = false)
    private String date;
}