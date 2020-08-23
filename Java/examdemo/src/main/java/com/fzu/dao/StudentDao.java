package com.fzu.dao;

import com.fzu.pojo.Student;
import com.fzu.pojo.StudentInfo;
import com.fzu.pojo.TTable;
import com.fzu.pojo.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class StudentDao {
    @Autowired
    JdbcTemplate jdbcTemplate;
    static final String ORIGINAL_PASSWORD="fae0b27c451c728867a567e8c1bb4e53";
    //添加学生
    public void addStudent(Student student){
        String sql="insert into exam_system.student (student_id, password, name, classroom, score) values (?,?,?,?,?)";
        Object[] objects=new Object[5];
        objects[0]=student.getStudentId();
        objects[1]=ORIGINAL_PASSWORD;//初始密码
        objects[2]=student.getName();
        objects[3]=student.getClassroom();
        objects[4]=null;
        jdbcTemplate.update(sql,objects);
    }
    //学生登录检验
    public int stuCheck(String username,String password){
        String sql="select password from exam_system.student where student_id = ?";
        try{
            String result=jdbcTemplate.queryForObject(sql,new Object[]{username},String.class);
            if (result.equals(ORIGINAL_PASSWORD)) return 2;
            else if(result.equals(password)) return 1;
            else return 0;
        }catch (EmptyResultDataAccessException e){
            return 0;
        }
    }
    //修改密码
    public void changePassword(String username,String password){
        Object[] objects=new Object[2];
        objects[0]=password;
        objects[1]=username;
        String sql="update exam_system.student set password =? where student_id=?";
        jdbcTemplate.update(sql,objects);
    }
    //获得班级id
    public Integer getClassId(String studentId){
        String sql = "select class_id from class_teacher, student " +
                "where class_teacher.class_name = student.classroom " +
                "AND student_id = ?";
        Integer integer;
        integer = jdbcTemplate.queryForObject(sql,new Object[]{ studentId},Integer.class);

        System.out.println(integer);
        return integer;
    }
    //获得教师id
    public String getTeacherId(String studentId){
        String sql="select paper.teacher_id from class_teacher,student where student.classroom=class_teacher.class_name";
        return jdbcTemplate.queryForObject(sql,String.class);
    }

    //写入正式考试的成绩
    public void updateScore(Integer score){
        String sql="update student set score =?";
        jdbcTemplate.update(sql,new Object[]{score});
    }

    //读取学生成绩信息
    public List<StudentInfo> getStudentInfoById(int classId){
        List<StudentInfo> result=new ArrayList<>();
        List<Student> list;
        if(classId==0){
            String sql2="select student_id,name,score from exam_system.student";
            list = jdbcTemplate.query(sql2,new BeanPropertyRowMapper<>(Student.class));
        }
        else{
            String sql1="select class_name from exam_system.class_teacher where class_id = ? ";
            String className = jdbcTemplate.queryForObject(sql1,new Object[]{ classId},String.class);
            System.out.println(classId);
            System.out.println(className);

            String sql2="select student_id,name,score from exam_system.student where classroom= ? ";
            list = jdbcTemplate.query(sql2,new BeanPropertyRowMapper<>(Student.class),className);
        }

        for(int i=0;i<list.size();i++){
            Student obj=list.get(i);
            StudentInfo cont=new StudentInfo();
            cont.setStudentNo(obj.getStudentId());
            cont.setStudentName(obj.getName());
            if(obj.getScore()==null) cont.setStudentScore(-1);
            else cont.setStudentScore(obj.getScore());
            result.add(cont);
        }
        System.out.println(result);
        return result;
    }
}
