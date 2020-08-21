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
            Cookie cookie2=new Cookie("username",username);
            Cookie cookie3=new Cookie("character","admin");
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
    public void changePassword(HttpServletRequest request, @RequestBody JSONObject jsonObject){
        String newPassword=(String)jsonObject.get("newPassword");
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
           String username=cookies[1].getValue();
            System.out.println(username);
           //管理员
            if (username.equals("admin")){
                adminService.changePassword(username,newPassword);
            }
            //教师
            else if (username.substring(0,1).equals("T")){
                teacherService.changePassword(username,newPassword);
            }
            //学生
            else{
                studentService.changePassword(username,newPassword);
            }
        }
    }


}
