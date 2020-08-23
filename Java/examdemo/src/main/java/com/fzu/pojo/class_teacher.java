package com.fzu.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class class_teacher {
    int classId;//班级编号(id自增)
    String className;//班级名
    String teacherId;//老师id
    Timestamp start;//开始时间
    Timestamp over;//结束时间
    Integer classStatus;//班级状态
}
