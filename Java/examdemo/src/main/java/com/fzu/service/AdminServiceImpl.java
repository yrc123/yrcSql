package com.fzu.service;

import com.fzu.dao.AdminDao;
import com.fzu.dao.QuestionDao;
import com.fzu.pojo.QTable;
import com.fzu.pojo.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    QuestionDao questionDao;
    @Autowired
    AdminDao adminDao;

    @Override
    public void importQuestion(List<QTable> qTables) {

        for (int i = 0; i < qTables.size(); i++) {
            Question question = new Question();
            question.setChapter(qTables.get(i).getChapter());
            question.setTitle(qTables.get(i).getTitle());
            question.setType(qTables.get(i).getType());
            question.setOption1(qTables.get(i).getOption1());
            question.setOption2(qTables.get(i).getOption2());
            question.setOption3(qTables.get(i).getOption3());
            question.setOption4(qTables.get(i).getOption4());
            question.setOption5(qTables.get(i).getOption5());
            question.setOption6(qTables.get(i).getOption6());
            question.setAnswer(qTables.get(i).getAnswer());
            questionDao.addQuestion(question);
        }
    }

    @Override
    public int admCheck(String username, String password) {
        return adminDao.admCheck(username, password);
    }

    @Override
    public void changePassword(String username, String password) {
        adminDao.changePassword(username, password);
    }


}
