package com.fzu.service;

import com.fzu.pojo.STable;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface StudentService {
    //导入学生名单
    void importStudent(List<STable> STables,String teacherId);
}
