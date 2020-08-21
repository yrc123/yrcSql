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
    boolean classStatus;//班级状态
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deadline;//截止日期

}
