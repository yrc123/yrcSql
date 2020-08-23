package com.fzu.service;

import com.fzu.dao.ClassDao;
import com.fzu.dao.StudentDao;
import com.fzu.dao.TeacherDao;
import com.fzu.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    TeacherDao teacherDao;
    @Autowired
    ClassDao classDao;
    @Autowired
    StudentDao studentDao;
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

    @Override
    public int teaCheck(String username, String password) {
        return teacherDao.teaCheck(username,password);
    }

    @Override
    public void changePassword(String username, String password) {
        teacherDao.changePassword(username, password);
    }

    @Override
    public void updateClassExam(ClassExam classExam) {
        System.out.println(classExam);
        classDao.updateClassExam(classExam);

    }

    @Override
    public List<ClassExam> getClassExamList(String teacherId) {

        List<ClassExam> result=classDao.getClassExamById(teacherId);
        return result;

    }

    @Override
    public List<TTable> getTeacherList() {

        List<TTable> result=teacherDao.getTeacher();
        return result;

    }

    @Override
    public List<StudentInfo> getStudentInfo(String classId){
        int id=Integer.valueOf(classId);
        List<StudentInfo> result=studentDao.getStudentInfoById(id);

        return result;
    }
}
