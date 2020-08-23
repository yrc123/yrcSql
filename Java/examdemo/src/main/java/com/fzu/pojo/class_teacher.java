package com.fzu.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
public class class_teacher {
    int classId;//班级编号
    String className;//班级名
    String teacherId;//老师id
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date startTime;//开始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date overTime;//结束时间
    Integer classStatus;//班级状态

}
