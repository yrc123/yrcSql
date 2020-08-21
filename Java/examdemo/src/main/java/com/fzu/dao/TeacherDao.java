package com.fzu.dao;

import com.fzu.pojo.Student;
import com.fzu.pojo.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;

@Repository
public class TeacherDao {
    static final String ORIGINAL_PASSWORD="fae0b27c451c728867a567e8c1bb4e53";
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
    //教师登录检验
    public int teaCheck(String username,String password){
        String sql="select password from exam_system.teacher where teacher_id = ?";
        try{
            String result=jdbcTemplate.queryForObject(sql,new Object[]{username},String.class);
            if (result.equals(ORIGINAL_PASSWORD)) return 2;
            else if(result.equals(password)) return 1;
            else return 0;
        }catch (EmptyResultDataAccessException e){
            return 0;
        }
    }
    //修改密码
    public void changePassword(String username,String password){
        Object[] objects=new Object[2];
        objects[0]=password;
        objects[1]=username;
        String sql="update exam_system.teacher set password =? where teacher_id =?";
        jdbcTemplate.update(sql,objects);

    }
}
