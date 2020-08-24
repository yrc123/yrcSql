package com.fzu.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AdminDao {
    @Autowired
    JdbcTemplate jdbcTemplate;
    static final String ORIGINAL_PASSWORD="fae0b27c451c728867a567e8c1bb4e53";

    //管理员登录检验
    public int admCheck(String username,String password){
        String sql="select password from exam_system.admin where username = ?";
        try{
            String result=jdbcTemplate.queryForObject(sql,new Object[]{username},String.class);
            if (result.equals(password)&&result.equals(ORIGINAL_PASSWORD)) return 2;
            else if(result.equals(password)&&!result.equals(ORIGINAL_PASSWORD)) return 1;
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
        String sql="update exam_system.admin set password =? where username=?";
        jdbcTemplate.update(sql,objects);

    }
    //重置密码
    public void resetPassword(String username){
        if(username.substring(0,1).equals("T")){
            String sql="update exam_system.teacher set password = ? where teacher_id =?";
            Object[] objects=new Object[2];
            objects[0]=ORIGINAL_PASSWORD;//初始密码
            objects[1]=username;
            jdbcTemplate.update(sql,objects);
        }
        else{
            String sql="update exam_system.student set password = ? where student_id =?";
            Object[] objects=new Object[2];
            objects[0]=ORIGINAL_PASSWORD;//初始密码
            objects[1]=username;
            jdbcTemplate.update(sql,objects);
        }
    }
}
