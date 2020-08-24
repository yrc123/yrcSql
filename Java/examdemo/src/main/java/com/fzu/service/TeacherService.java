package com.fzu.service;

import com.fzu.pojo.ClassExam;
import com.fzu.pojo.StudentInfo;
import com.fzu.pojo.TTable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface TeacherService {
    //导入教师名单
    void importTeacher(List<TTable> tTables);
    //登录检验
    int teaCheck(String username,String password);
    //修改密码
    void changePassword(String username,String password);

    //添加(更新)班级考试
    /*update操作,将班级的考试status改变,将字段转化成数据库匹配的开始和结束时间*/
    void updateClassExam(ClassExam classExam);

    //先通过教师id获取班级id列表 teacherDao
    //再遍历每个班级id获得班级考试 classDao
    List<ClassExam> getClassExamList(String teacherId);
    //获取所有老师的id和name
    List<TTable> getTeacherList();
    //获取学生成绩信息
    List<StudentInfo> getStudentInfo(int flag,String username,String classId);
    //删除班级
    void deleteClass(List<Integer> classId);
}
