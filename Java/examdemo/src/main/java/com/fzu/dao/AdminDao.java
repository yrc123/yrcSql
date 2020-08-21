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
            if (result.equals(ORIGINAL_PASSWORD)) return 2;
            else if(result.equals(password)) return 1;
            else return 0;
        }catch (EmptyResultDataAccessException e){
            return 0;
        }
    }
    public void changePassword(String username,String password){

    }
}
