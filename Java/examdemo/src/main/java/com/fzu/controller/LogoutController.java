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
    @Autowired
    AdminService adminService=new AdminServiceImpl();

    @ResponseBody
    @RequestMapping("/logout")
    public Map<String, Object> logout(HttpServletResponse response, HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        //String username=cookies[1].getValue();
        Map<String, Object> result = new HashMap<String, Object>();
        //System.out.println(username.substring(0,1));
        for(Cookie i:cookies){
            if(i.getName().equals("userId")||i.getName().equals("username")||i.getName().equals("character")){
                i.setMaxAge(0);
                i.setPath("/");
                response.addCookie(i);
            }

        }
        result.put("status", "1");

        return result;
    }

    @ResponseBody
    @RequestMapping("/cleanAll")
    public Map<String,Integer> cleanAll(){
        adminService.cleanAll();
        Map<String,Integer> result=new HashMap<>();
        result.put("status",1);
        return result;
    }
}

