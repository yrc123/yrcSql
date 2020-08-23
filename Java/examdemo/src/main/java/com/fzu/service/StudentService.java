package com.fzu.service;

import com.fzu.pojo.ClassExam;
import com.fzu.pojo.ExamPaper;
import com.fzu.pojo.STable;
import com.fzu.pojo.TTable;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.util.Date;
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

    //获取一份试卷
    ExamPaper getExamPaper(String studentId) throws ParseException;
    //获得教师id
    String getTeacherId(String studentId);
    //获得班级考试的开始时间(用于判断是否可以点击开始考试)
    Date getStarttime(Integer classId);
    //获得该考试的结束时间
    Date getOvertime(Integer paperId);
    //获得考卷的答案列表(用于模拟考)
    List<List<Integer>> getAnswerList(Integer paperId);
    //得出学生的得分(用于正式考试)
    void setScore(Integer paperId,String studentId,List<List<Integer>> stuAnswer);
}
