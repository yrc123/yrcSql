package com.fzu.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;

@Repository
public class ExamDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    //初始化一个paperid;
    public int initPaperId(){
        Integer initPaperId=null;
        String sql="select MAX(paper_id) from paper_question";
        try {
            initPaperId= jdbcTemplate.queryForObject(sql,Integer.class);
            if(initPaperId==null) initPaperId=0;
            else initPaperId++;
        } catch (EmptyResultDataAccessException e) {
            initPaperId=0;
        }
        return initPaperId;
    }
    //添加试卷
    public void addPaper(int paperId, Date begin, Date over, String studentId, String teacherId){
        Object[]objects=new Object[5];
        objects[0]=paperId;
        objects[1]=begin;
        objects[2]=over;
        objects[3]=studentId;
        objects[4]=teacherId;
        String sql="insert into paper values (?,?,?,?,?)";
        jdbcTemplate.update(sql,objects);
    }
    //添加试卷-题目关系
    public void insert(int paperId,int questionId){
        String sql="insert into paper_question(paper_id, question_id) values (?,?)";
        jdbcTemplate.update(sql,new Object[]{paperId,questionId});
    }

    //获得结束时间
    public Date getOvertime(Integer paperId){
        String sql="select paper.over from paper where paper.id=?";
        return jdbcTemplate.queryForObject(sql,new Object[]{paperId},Date.class);
    }


}
