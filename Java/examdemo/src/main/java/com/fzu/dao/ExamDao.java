package com.fzu.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    //添加试卷-题目关系(自动有序？？)
    public void insert(int paperId,int questionId,int idOrder){
        String sql="insert into paper_question(paper_id, question_id,id_order) values (?,?,?)";
        System.out.println("顺序测试"+paperId+questionId);
        jdbcTemplate.update(sql,new Object[]{paperId,questionId,idOrder});
    }

    //获得结束时间
    public Date getOvertime(Integer paperId){
        String sql="select paper.over from paper where paper.id=?";
        return jdbcTemplate.queryForObject(sql,new Object[]{paperId},Date.class);
    }
    public void setOfficialExam(String examTime){
        String sql="update class_teacher set start_time=?,over_time=?,class_status=2";
        String time =examTime;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //默认分两段
        if(time==null){
            time = "2020/08/05 00:00:00 ~ 2020/09/22 00:00:00";
        }
        String[] t = time.split("~");
        if(t.length<2){
            System.out.println("string.spilt结果小于2");
            return;
        }
        System.out.println(t[0]+" "+t[1]);

        Date d1 = null, d2 = null;
        try {
            d1 = sdf.parse(t[0]);
            d2 = sdf.parse(t[1]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Timestamp ts1 = new Timestamp(d1.getTime());
        System.out.println("ts1 = "+ts1.toString());
        Timestamp ts2 = new Timestamp(d2.getTime());
        System.out.println("ts2 = "+ts2.toString());
        Object[] params = new Object[]{ts1,ts2};
        jdbcTemplate.update(sql,params);
    }

}
