package com.fzu.service;

import com.fzu.pojo.STable;
import com.fzu.pojo.TTable;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface StudentService {
    //导入学生名单
    void importStudent(List<STable> STables,String teacherId);
    //登录检验
    int stuCheck(String username,String password);
    //修改密码
    void changePassword(String username,String password);
}
