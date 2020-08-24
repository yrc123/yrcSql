package com.fzu.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudentInfo {
    String studentNo;//学号
    String studentName;//姓名
    Integer studentScore;//考试得分
}
