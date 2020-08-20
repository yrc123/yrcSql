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
        String sql="insert into exam_system.question (id, chapter, title, type, option1, option2, option3, option4, option5, option6, answer) " +
                "values(?,?,?,?,?,?,?,?,?,?,?)";
        Object[] objects = new Object[11];
        objects[0] = question.getId();
        objects[1] = question.getChapter();
        objects[2] = question.getTitle();
        objects[3] = question.getType();
        objects[4] = question.getOption1();
        objects[5] = question.getOption2();
        objects[6] = question.getOption3();
        objects[7] = question.getOption4();
        objects[8] = question.getOption5();
        objects[9] = question.getOption6();
        objects[10] = question.getAnswer();
        jdbcTemplate.update(sql,objects);
    }


}
