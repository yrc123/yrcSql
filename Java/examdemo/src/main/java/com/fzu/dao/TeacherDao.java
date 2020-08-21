package com.fzu.dao;

import com.fzu.pojo.Student;
import com.fzu.pojo.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;

@Repository
public class TeacherDao {
    @Autowired
    JdbcTemplate jdbcTemplate;
    public void addTeacher(Teacher teacher){
        String sql="insert into exam_system.teacher (teacher_id, password, name) values (?,?,?)";
        Object[] objects=new Object[3];
        objects[0]=teacher.getTeacherId();
        objects[1]="666666";//初始密码
        objects[2]=teacher.getName();
        jdbcTemplate.update(sql,objects);
    }
}
