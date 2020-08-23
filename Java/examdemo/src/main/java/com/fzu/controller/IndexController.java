package com.fzu.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.fzu.dao.ClassDao;
import com.fzu.dao.QuestionDao;
import com.fzu.dao.StudentDao;
import com.fzu.pojo.Question;
import com.fzu.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class IndexController {
    @Autowired
    QuestionDao questionDao;
    @Autowired
    StudentDao studentDao;
    @Autowired
    ClassDao classDao;

    @RequestMapping("/hi")
    public String index(){
        return "index";
    }

    @ResponseBody
    @RequestMapping("/test")
    public void test(@RequestBody JSONObject jsonobject){
        classDao.getClassExamById(jsonobject.getString("teacherId"));
    }

    @ResponseBody
    @RequestMapping("/test2")
    public void test2(){
       studentDao.getClassId("20191112");
    }

}
