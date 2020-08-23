package com.fzu.dao;

import com.fzu.pojo.ClassExam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
@Repository
public class ClassDao {
    @Autowired
    JdbcTemplate jdbcTemplate;
    //增加班级_教师表(已完成)
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
        //从数据库中拿到的数据转化成跟ClassExam对应的
        return null;
    }
    //添加(更新)考试
    public void updateClassExam(ClassExam classExam) {
        //先转化然后逐个参数对应上传到数据库。
        String sql1 = "update exam_system.class_teacher set start_time = ? where class_id = ?";
        String sql2 = "update exam_system.class_teacher set over_time = ? where class_id = ?";
        String time = classExam.getExamTime();
        String[] t = time.split("~");
        //默认分两段
        if(t.length<2){
            System.out.println("string.spilt结果小于2");
            return;
        }
        System.out.println(t[0]+" "+t[1]);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
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

        jdbcTemplate.update(sql1,ts1,classExam.getClassId());
        jdbcTemplate.update(sql2,ts2,classExam.getClassId());
    }
    //获得班级考试的开始时间(用于判断是否可以进入考试)
    public Date getStarttime(Integer classId){
        String sql="select class_teacher.start_time from class_teacher where class_id=?";
        return jdbcTemplate.queryForObject(sql,new Object[]{classId},Date.class);
    }


}
