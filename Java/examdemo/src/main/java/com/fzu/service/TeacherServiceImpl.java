package com.fzu.service;

import com.fzu.dao.TeacherDao;
import com.fzu.pojo.TTable;
import com.fzu.pojo.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    TeacherDao teacherDao;
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

}
