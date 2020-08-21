package com.fzu.service;

import com.fzu.pojo.QTable;

import java.util.List;

public interface AdminService {
    //导入题目
    void importQuesiton(List<QTable> qTables);
}
