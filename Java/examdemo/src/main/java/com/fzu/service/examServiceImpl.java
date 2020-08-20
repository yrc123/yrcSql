package com.fzu.service;

import com.fzu.dao.ClassDao;
import com.fzu.dao.StudentDao;
import com.fzu.dao.TeacherDao;
import com.fzu.pojo.STable;
import com.fzu.pojo.Student;
import com.fzu.pojo.TTable;
import com.fzu.pojo.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class examServiceImpl implements examService {
    @Autowired
    StudentDao studentDao;
    @Autowired
    ClassDao classDao;
    @Autowired
    TeacherDao teacherDao;

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
    public void importTeacher(List<TTable> tTables) {
        //数据库中对应的需要操作到：teacher表(添加教师)
        for(int i=0;i<tTables.size();i++){
            Teacher teacher=new Teacher();
            teacher.setTeacherId(tTables.get(i).getTeacherId());
            teacher.setName(tTables.get(i).getName());
            teacherDao.addTeacher(teacher);
        }
    }


}
