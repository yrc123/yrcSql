package com.fzu.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudentInfo {
    String studentId;//学号
    String name;//姓名
    Integer score;//考试得分
}
