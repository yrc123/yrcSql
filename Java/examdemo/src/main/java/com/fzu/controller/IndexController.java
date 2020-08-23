package com.fzu.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.fzu.pojo.ClassExam;
import com.fzu.pojo.Question;
import com.fzu.service.TeacherService;
import com.fzu.service.TeacherServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class IndexController {
    @RequestMapping("/hi")
    public String index(){
        return "index";
    }

    @Autowired
    TeacherService teacherService = new TeacherServiceImpl();
    @ResponseBody
    @RequestMapping("/test")
    public void test(@RequestBody ClassExam classExam){
        teacherService.updateClassExam(classExam);
    }

    @ResponseBody
    @RequestMapping("/test2")
    public void test(@RequestBody JSONObject jsonObject){
        String username=(String) jsonObject.get("username");
        System.out.println(username);
        System.out.println("password:"+jsonObject.get("password"));
    }

}
