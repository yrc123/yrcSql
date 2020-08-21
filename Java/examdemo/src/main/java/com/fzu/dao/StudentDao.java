package com.fzu.dao;

import com.fzu.pojo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;

@Repository
public class StudentDao {
    @Autowired
    JdbcTemplate jdbcTemplate;
    static final String ORIGINAL_PASSWORD="fae0b27c451c728867a567e8c1bb4e53";
    //添加学生
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
    //学生登录检验
    public int stuCheck(String username,String password){
        String sql="select password from exam_system.student where student_id = ?";
        try{
            String result=jdbcTemplate.queryForObject(sql,new Object[]{username},String.class);
            if (result.equals(ORIGINAL_PASSWORD)) return 2;
            else if(result.equals(password)) return 1;
            else return 0;
        }catch (EmptyResultDataAccessException e){
            return 0;
        }

    }
}
