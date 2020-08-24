package com.fzu.service;

import com.fzu.dao.*;
import com.fzu.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    StudentDao studentDao;
    @Autowired
    ClassDao classDao;
    @Autowired
    QuestionDao questionDao;
    @Autowired
    ExamDao examDao;

    @Override
    public void importStudent(List<STable> sTables,String teacherId) {
        //数据库中对应的需要操作到：1.student表(添加学生) 2.class_teacher(增加班级-老师的记录)
        Map<String,String> map=new HashMap<>();//为班级信息-与教师的键值对map
      for(int i=0;i<sTables.size();i++){
          Student student=new Student();
          student.setStudentId(sTables.get(i).getStudentId());
          student.setName(sTables.get(i).getName());
          student.setClassroom(sTables.get(i).getClassInfo());
          map.put(sTables.get(i).getClassInfo(),teacherId);//使用map避免重复的班级信息
          studentDao.addStudent(student);
      }
        System.out.println("键值对信息"+map.toString());
      classDao.addClass(map);
    }

    @Override
    public int stuCheck(String username, String password) {
        return studentDao.stuCheck(username, password);
    }

    @Override
    public void changePassword(String username, String password) {
        studentDao.changePassword(username, password);
    }

    @Override
    public Integer getClassId(String studentId) {
        return studentDao.getClassId(studentId);
    }

    @Override
    public ClassExam getClassExam(Integer classId) {
        return classDao.getClassExam(classId);
    }

    @Override
    public String getTeacherId(String studentId) {
        return studentDao.getTeacherId(studentId);
    }

    @Override
    public Date getStarttime(Integer classId) {
        return classDao.getStarttime(classId);
    }

    @Override
    public ExamPaper getExamPaper(String studentId) throws ParseException {
        ExamPaper examPaper=new ExamPaper();
        int paperId=examDao.initPaperId();
        List<Qdata> qdataList=new ArrayList<>();
        List<Question> questionList1=questionDao.getRandomQuestion("单选题");
        for (int i=0;i<questionList1.size();i++){
            Qdata qdata=new Qdata();
            qdata.setId(questionList1.get(i).getId());
            qdata.setTitle(questionList1.get(i).getTitle());
            List<String> choice=new ArrayList<>();
            choice.add(questionList1.get(i).getOption1());
            choice.add(questionList1.get(i).getOption2());
            choice.add(questionList1.get(i).getOption3());
            choice.add(questionList1.get(i).getOption4());
            qdata.setChoice(choice);

            qdataList.add(qdata);
            examDao.insert(paperId,qdata.getId(),i);
        }

        List<Question> questionList2=questionDao.getRandomQuestion("多选题");
        for (int i=0;i<questionList2.size();i++){
            Qdata qdata=new Qdata();
            qdata.setId(questionList2.get(i).getId());
            qdata.setTitle(questionList2.get(i).getTitle());
            List<String> choice=new ArrayList<>();
            choice.add(questionList2.get(i).getOption1());
            choice.add(questionList2.get(i).getOption2());
            choice.add(questionList2.get(i).getOption3());
            choice.add(questionList2.get(i).getOption4());
            qdata.setChoice(choice);

            qdataList.add(qdata);
            examDao.insert(paperId,qdata.getId(),15+i);
        }

        List<Question> questionList3=questionDao.getRandomQuestion("判断题");
        for (int i=0;i<questionList2.size();i++){
            Qdata qdata=new Qdata();
            qdata.setId(questionList3.get(i).getId());
            qdata.setTitle(questionList3.get(i).getTitle());
            List<String> choice=new ArrayList<>();
            choice.add(questionList3.get(i).getOption1());
            choice.add(questionList3.get(i).getOption2());
            qdata.setChoice(choice);

            qdataList.add(qdata);
            examDao.insert(paperId,qdata.getId(),30+1);
        }

        examPaper.setPaperId(paperId);
        examPaper.setQtype(new int[]{15,15,15});
        examPaper.setQdataList(qdataList);
        Date date=new Date();
        Date begin=date;
        date.setTime(date.getTime() + 1 * 60 * 60 * 1000);
        examPaper.setDate(date);
        Date over=date;
        String teacherId=this.getTeacherId(studentId);
        examDao.addPaper(paperId,begin,over,studentId,teacherId);
        return examPaper;
    }

    @Override
    public ExamPaper getExistPaper(Integer paperId) {
        List<Qdata> qdataList=new ArrayList<>();
        ExamPaper examPaper=new ExamPaper();
        List<Question> questions;
        questions=questionDao.getQuestionList(paperId);//获取该试卷的所有题目
        for(int i=0;i<questions.size();i++){
            Qdata qdata=new Qdata();
            qdata.setId(questions.get(i).getId());
            qdata.setTitle(questions.get(i).getTitle());
            List<String> choice=new ArrayList<>();
            choice.add(questions.get(i).getOption1());
            choice.add(questions.get(i).getOption2());
            choice.add(questions.get(i).getOption3());
            choice.add(questions.get(i).getOption4());
            qdata.setChoice(choice);
            qdataList.add(qdata);
        }
        Date over=getOvertime(paperId);
        examPaper.setPaperId(paperId);
        examPaper.setQtype(new int[]{15,15,15});
        examPaper.setQdataList(qdataList);
        examPaper.setDate(over);
        return examPaper;
    }


    @Override
    public Date getOvertime(Integer paperId) {
        return examDao.getOvertime(paperId);
    }

    @Override
    public List<List<Integer>> getAnswerList(Integer paperId) {
        List<Question> questions;
        List<List<Integer>> result=new ArrayList<>();
        List<Integer> number=new ArrayList<>();
        number.add(15);
        number.add(15);
        number.add(15);
        List<Integer> answer=new ArrayList<>();
        questions=questionDao.getQuestionList(paperId);
        System.out.println("接收顺序测试"+questions);
        for(int i=0;i<questions.size();i++){
            Question question=questions.get(i);
            System.out.println(question.getId() +" "+ question.getAnswer()+"--"+question.exchangeAnswer());
            answer.add(question.exchangeAnswer());
        }
        result.add(number);
        result.add(answer);
        return result;
    }

    @Override
    public void setScore(Integer paperId, String studentId,List<List<Integer>>stuAnswer) {
        int score=0;
        List<Question> questions=new ArrayList<>();
        List<Integer> stuAnswerList=stuAnswer.get(1);
        questions=questionDao.getQuestionList(paperId);
        for(int i=0;i<questions.size();i++){
            Question question=questions.get(i);
            if(question.checkAnswer(stuAnswerList.get(i)))
                score+=2;
        }
        studentDao.updateScore(score,studentId);
    }



}
