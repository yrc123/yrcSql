package com.fzu.service;

import com.fzu.dao.ClassDao;
import com.fzu.dao.StudentDao;
import com.fzu.dao.TeacherDao;
import com.fzu.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    StudentDao studentDao;
    @Autowired
    ClassDao classDao;

    @Override
    public void importStudent(List<STable> sTables,String teacherId) {
        //数据库中对应的需要操作到：1.student表(添加学生) 2.class_teacher(增加班级-老师的记录)
        Map<String,String> map=new HashMap<>();//为班级信息-与教师的键值对map
      for(int i=0;i<sTables.size();i++){
          Student student=new Student();
          student.setStudentId(sTables.get(i).getStudentId());
          student.setName(sTables.get(i).getName());
          student.setClassroom(sTables.get(i).getClassInfo());
          map.put(sTables.get(i).getClassInfo(),teacherId);//使用map避免重复的班级信息
          studentDao.addStudent(student);
      }
        System.out.println("键值对信息"+map.toString());
      classDao.addClass(map);
    }

    @Override
    public int stuCheck(String username, String password) {
        return studentDao.stuCheck(username, password);
    }

    @Override
    public void changePassword(String username, String password) {
        studentDao.changePassword(username, password);
    }

    @Override
    public Integer getClassId(String studentId) {
        return null;
    }

    @Override
    public ClassExam getClassExam(Integer classId) {
        return null;
    }


}
