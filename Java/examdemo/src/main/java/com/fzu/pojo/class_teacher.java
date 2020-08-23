package com.fzu.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
public class class_teacher {
    int classId;//班级编号(id自增)
    String className;//班级名
    String teacherId;//老师id
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date start_time;//开始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date over_time;//结束时间
    Integer classStatus;//班级状态
}
