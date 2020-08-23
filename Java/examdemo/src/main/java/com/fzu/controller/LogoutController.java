package com.fzu.controller;


import com.alibaba.fastjson.JSONObject;
import com.fzu.config.UUIDUtils;
import com.fzu.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LogoutController {


    @ResponseBody
    @RequestMapping("/logout")
    public Map<String, Object> logout(HttpServletResponse response, HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        //String username=cookies[1].getValue();
        Map<String, Object> result = new HashMap<String, Object>();
        //System.out.println(username.substring(0,1));
        for(Cookie i:cookies){
            i.setMaxAge(0);
        }
        result.put("status", "1");
        /*
        //管理员
        if (username.equals("admin")){
            String userId = cookies[0].getValue();
            Cookie cookie1=new Cookie("userId",userId);
            cookie1.setMaxAge(0);
            cookie1.setPath("/");
            Cookie cookie2=new Cookie("username",username);
            cookie1.setMaxAge(0);
            cookie2.setPath("/");
            Cookie cookie3=new Cookie("character","admin");
            cookie3.setMaxAge(0);
            cookie3.setPath("/");
            //status=adminService.admCheck(username, password);
            result.put("status", "1");
            response.addCookie(cookie1);
            response.addCookie(cookie2);
            response.addCookie(cookie3);
        }
        //教师
        else if (username.substring(0,1).equals("T")){
            String userId = cookies[0].getValue();
            Cookie cookie1=new Cookie("userId",userId);
            Cookie cookie2=new Cookie("username",username);
            Cookie cookie3=new Cookie("character","teacher");
            cookie1.setMaxAge(0);
            cookie2.setMaxAge(0);
            cookie3.setMaxAge(0);
            cookie1.setPath("/");
            cookie2.setPath("/");
            cookie3.setPath("/");
            //status=teacherService.teaCheck(username,password);
            result.put("status", "1");
            response.addCookie(cookie1);
            response.addCookie(cookie2);
            response.addCookie(cookie3);
        }
        //学生
        else{
            String userId = cookies[0].getValue();
            Cookie cookie1=new Cookie("userId",userId);
            Cookie cookie2=new Cookie("username",username);
            Cookie cookie3=new Cookie("character","student");
            cookie1.setMaxAge(0);
            cookie2.setMaxAge(0);
            cookie3.setMaxAge(0);
            cookie1.setPath("/");
            cookie2.setPath("/");
            cookie3.setPath("/");
            //status=studentService.stuCheck(username, password);
            result.put("status", "1");
            response.addCookie(cookie1);
            response.addCookie(cookie2);
            response.addCookie(cookie3);
        }*/
        return result;
    }
}

