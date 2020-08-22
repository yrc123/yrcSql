package com.fzu.controller;

import com.fzu.pojo.ClassExam;
import com.fzu.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
            ClassExam classExam=new ClassExam();
            classExam=classExamList.get(i);
            if(classExam.getClassStatus()==0){//删除考试

            }
            else if(classExam.getClassStatus()==1){//设置模拟考试

            }
            else if(classExam.getClassStatus()==2){//设置正式考试

            }
            System.out.println(i+classExam.toString());
        }
        return result;
    }
    @RequestMapping("/getClassInExam")
    @ResponseBody
    public List<ClassExam> getClassInExam(HttpServletRequest request){
        List<ClassExam> classExamList=new ArrayList<ClassExam>();
        //获取某个老师管理的班级的考试信息(需要从cookie中拿出教师id)
        request.getCookies();
        return classExamList;

    }

}
