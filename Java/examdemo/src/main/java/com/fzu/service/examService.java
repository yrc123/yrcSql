package com.fzu.service;

import com.fzu.pojo.STable;
import com.fzu.pojo.TTable;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface examService {
    //导入学生名单
    void importStudent(List<STable> STables,String teacherId);
    void importTeacher(List<TTable> tTables);
}
