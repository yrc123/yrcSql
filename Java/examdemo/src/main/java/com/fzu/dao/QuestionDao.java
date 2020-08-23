package com.fzu.dao;

import com.fzu.pojo.Qdata;
import com.fzu.pojo.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QuestionDao {
    @Autowired
    JdbcTemplate jdbcTemplate;
    //添加题目集
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

    //抽取出数量为15的题目集
    public List<Question> getRandomQuestion(String type){
        String sql="select * from question where question.type= ? ORDER BY RAND() LIMIT 15";
        List<Question> questions;
        questions=jdbcTemplate.query(sql,new BeanPropertyRowMapper(Question.class),type);
        for (int i=0;i<questions.size();i++){
            System.out.println(i+""+questions.get(i));
        }
        return  questions;
    }

    //通过paperId得到题目集合
    public List<Question> getQuestionList(Integer paperId){
        String sql="select question.* from question,paper_question where paper_id=? and question_id=id";
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper(Question.class),paperId);

    }

}
