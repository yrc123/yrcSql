package com.fzu.dao;

import com.fzu.pojo.ClassExam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
@Repository
public class ClassDao {
    @Autowired
    JdbcTemplate jdbcTemplate;
    //增加班级_教师表
    public void addClass(Map<String,String> map){
        //对map进行遍历
        for(Map.Entry<String, String> entry : map.entrySet()){
            String mapKey = entry.getKey();
            String mapValue = entry.getValue();
            String sql="insert into exam_system.class_teacher(class_name, teacher_id) values (?,?)";//id自增
            Object[] objects=new Object[2];
            objects[0]=mapKey;
            objects[1]=mapValue;
            System.out.println(mapKey);
            jdbcTemplate.update(sql,objects);
        }
    }
    //通过classId获取班级考试
    public ClassExam getClassExamById(Integer classId){

    }


}
