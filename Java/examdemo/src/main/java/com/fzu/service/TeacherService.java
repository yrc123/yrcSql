package com.fzu.service;

import com.fzu.pojo.TTable;

import java.util.List;

public interface TeacherService {
    //导入教师名单
    void importTeacher(List<TTable> tTables);
    //登录检验
    int teaCheck(String username,String password);
}
