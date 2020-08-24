package com.fzu.service;

import com.fzu.pojo.QTable;

import java.util.List;

public interface AdminService {
    //导入题目
    void importQuestion(List<QTable> qTables);
    //登录检验
    int admCheck(String username,String password);
    //修改密码
    void changePassword(String username,String password);
    //重置密码
    void resetPassword(String username);
    //设置所有班级进行正式考试
    void setOfficialExam(String examTime);
    //删除教师
    void deleteTeacher(List<String> teacherId);
}
