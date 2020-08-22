package com.fzu.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.fzu.pojo.Question;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class IndexController {
    @RequestMapping("/hi")
    public String index(){
        return "index";
    }

    @ResponseBody
    @RequestMapping("/test")
    public void test(){
        Question question = new Question();
        question.setAnswer("BDd");
        System.out.println(question.getAnswer());
    }

    @ResponseBody
    @RequestMapping("/test2")
    public void test(@RequestBody JSONObject jsonObject){
        String username=(String) jsonObject.get("username");
        System.out.println(username);
        System.out.println("password:"+jsonObject.get("password"));
    }

}
