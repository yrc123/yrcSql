package com.fzu.service;

import com.fzu.pojo.ClassExam;
import com.fzu.pojo.STable;
import com.fzu.pojo.TTable;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface StudentService {
    //导入学生名单
    void importStudent(List<STable> STables,String teacherId);
    //登录检验
    int stuCheck(String username,String password);
    //修改密码
    void changePassword(String username,String password);
    //获得班级id(未完成)
    Integer getClassId(String studentId);
    //通过自己的班级id获得考试信息(未完成)
    ClassExam getClassExam(Integer classId);
}
