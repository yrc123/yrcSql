package com.fzu.service;

import com.fzu.pojo.ClassExam;
import com.fzu.pojo.TTable;

import java.util.List;

public interface TeacherService {
    //导入教师名单
    void importTeacher(List<TTable> tTables);
    //登录检验
    int teaCheck(String username,String password);
    //修改密码
    void changePassword(String username,String password);
    //通过教师id获取班级考试列表
    List<ClassExam> getClassExamList(String teacherId);
}
