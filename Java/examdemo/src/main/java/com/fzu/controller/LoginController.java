package com.fzu.controller;

import com.alibaba.fastjson.JSONObject;
import com.fzu.config.UUIDUtils;
import com.fzu.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class LoginController {

    @Autowired
    StudentService studentService=new StudentServiceImpl();
    @Autowired
    AdminService adminService=new AdminServiceImpl();
    @Autowired
    TeacherService teacherService=new TeacherServiceImpl();
    @ResponseBody
    @RequestMapping("/login")
    public Map<String, Object> login(HttpServletResponse response, @RequestBody JSONObject jsonObject){
        int status;
        String username=(String)jsonObject.get("username");
        String password=(String)jsonObject.get("password");
        Map<String, Object> result = new HashMap<String, Object>();
        System.out.println(username.substring(0,1));
        //管理员
        if (username.equals("admin")){
            String userId = UUIDUtils.getUUID();
            Cookie cookie1=new Cookie("userId",userId);
            cookie1.setPath("/");
            Cookie cookie2=new Cookie("username",username);
            cookie2.setPath("/");
            Cookie cookie3=new Cookie("character","admin");
            cookie3.setPath("/");
            status=adminService.admCheck(username, password);
            result.put("status", status);
            response.addCookie(cookie1);
            response.addCookie(cookie2);
            response.addCookie(cookie3);
        }
        //教师
        else if (username.substring(0,1).equals("T")){
            String userId = UUIDUtils.getUUID();
            Cookie cookie1=new Cookie("userId",userId);
            Cookie cookie2=new Cookie("username",username);
            Cookie cookie3=new Cookie("character","teacher");
            cookie1.setPath("/");
            cookie2.setPath("/");
            cookie3.setPath("/");
            status=teacherService.teaCheck(username,password);
            result.put("status", status);
            response.addCookie(cookie1);
            response.addCookie(cookie2);
            response.addCookie(cookie3);
        }
        //学生
        else{
            String userId = UUIDUtils.getUUID();
            Cookie cookie1=new Cookie("userId",userId);
            Cookie cookie2=new Cookie("username",username);
            Cookie cookie3=new Cookie("character","student");
            cookie1.setPath("/");
            cookie2.setPath("/");
            cookie3.setPath("/");
            status=studentService.stuCheck(username, password);
            result.put("status", status);
            response.addCookie(cookie1);
            response.addCookie(cookie2);
            response.addCookie(cookie3);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/changePassword")
    public Map<String,String> changePassword(HttpServletRequest request, @RequestBody JSONObject jsonObject){
        String newPassword=(String)jsonObject.get("newPassword");
        Map<String,String> result=new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
           String username=cookies[1].getValue();
            System.out.println(username);
           //管理员
            if (username.equals("admin")){
                adminService.changePassword(username,newPassword);
                result.put("status","1");
            }
            //教师
            else if (username.substring(0,1).equals("T")){
                teacherService.changePassword(username,newPassword);
                result.put("status","1");
            }
            //学生
            else{
                studentService.changePassword(username,newPassword);
                result.put("status","1");
            }
        }
        return result;
    }


}
