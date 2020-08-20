package com.fzu.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Student {
    String studentId;//学号
    String password;//密码
    String name;//姓名
    String classroom;//班级
    Integer score;//正式考试得分

}
