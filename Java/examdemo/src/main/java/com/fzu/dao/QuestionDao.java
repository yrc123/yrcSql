package com.fzu.dao;

import com.fzu.pojo.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class QuestionDao {
    @Autowired
    JdbcTemplate jdbcTemplate;
    public void addQuestion(Question question){
        String sql="insert into exam_system.question (chapter, title, type, option1, option2, option3, option4, answer) values(?,?,?,?,?,?,?,?)";
        Object[] objects = new Object[8];
        objects[0] = question.getChapter();
        objects[1] = question.getTitle();
        objects[2] = question.getType();
        objects[3] = question.getOption1();
        objects[4] = question.getOption2();
        objects[5] = question.getOption3();
        objects[6] = question.getOption4();
        objects[7] = question.getAnswer();
        jdbcTemplate.update(sql,objects);
    }


}
