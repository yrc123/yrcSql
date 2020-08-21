package com.fzu.service;

import com.fzu.pojo.QTable;

import java.util.List;

public interface AdminService {
    //导入题目
    void importQuestion(List<QTable> qTables);
    //登录检验
    int admCheck(String username,String password);
}
