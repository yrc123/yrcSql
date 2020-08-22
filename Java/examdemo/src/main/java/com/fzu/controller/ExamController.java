package com.fzu.controller;

import com.alibaba.fastjson.JSONObject;
import com.fzu.pojo.ClassExam;
import com.fzu.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ExamController {
    @Autowired
    StudentService studentService=new StudentServiceImpl();
    @Autowired
    AdminService adminService=new AdminServiceImpl();
    @Autowired
    TeacherService teacherService=new TeacherServiceImpl();

    //为批量的班级添加考试
    @RequestMapping("/setClassInExam")
    @ResponseBody
    public Map<String,Integer> setClassInExam(@RequestBody List<ClassExam> classExamList){
        Map<String,Integer> result=new HashMap<>();
        for(int i=0;i<classExamList.size();i++){
            ClassExam classExam;
            classExam=classExamList.get(i);
            teacherService.updateClassExam(classExam);
            System.out.println(i+classExam.toString());
        }
        return result;
    }

    //获得班级考试信息(列表)
    @RequestMapping("/getClassInExam")
    @ResponseBody
    public List<ClassExam> getClassInExam(HttpServletRequest request){
        List<ClassExam> classExamList=new ArrayList<ClassExam>();
        //获取某个老师管理的班级的考试信息(需要从cookie中拿出教师id)
        Cookie[] cookies=request.getCookies();
        String teacher_id=cookies[1].getValue();
        classExamList=teacherService.getClassExamList(teacher_id);
        return classExamList;
    }

    //返回班级的考试是否开始的状态
    @RequestMapping("/examStatus")
    @ResponseBody
    public Map<String,Integer> examStatus(HttpServletRequest request,@RequestBody JSONObject jsonObject){
        Integer examType=jsonObject.getInteger("examType");//0代表正式，1代表模拟
        Cookie[]cookies=request.getCookies();
        String studentId=cookies[1].getValue();
        //通过学生id得到班级名字，通过班级名字得到班级id，通过班级id获取examstatus。
        Integer classId=studentService.getClassId(studentId);
        ClassExam classExam=studentService.getClassExam(classId);
        //如果为0则为未开始



        //如果非0，通过班级id获取考试开始时间。考试开始时间未到为未开始
        return null;
    }

}
