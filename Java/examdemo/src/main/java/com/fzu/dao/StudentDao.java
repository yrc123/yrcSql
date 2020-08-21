package com.fzu.dao;

import com.fzu.pojo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;

@Repository
public class StudentDao {
    @Autowired
    JdbcTemplate jdbcTemplate;
    public void addStudent(Student student){
        String sql="insert into exam_system.student (student_id, password, name, classroom, score) values (?,?,?,?,?)";
        Object[] objects=new Object[5];
        objects[0]=student.getStudentId();
        objects[1]="666666";//初始密码
        objects[2]=student.getName();
        objects[3]=student.getClassroom();
        objects[4]=null;
        jdbcTemplate.update(sql,objects);
    }
}
