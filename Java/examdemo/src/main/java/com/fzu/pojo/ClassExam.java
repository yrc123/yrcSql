package com.fzu.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
public class ClassExam {
    int classId;//班级编号(id自增)
    String className;//班级名
    int classStatus;//班级状态
    String examTime;//格式"YYYY/MM/DD HH:mm:ss ~ YYYY/MM/DD HH:mm:ss"


}
