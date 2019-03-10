package com.zwt.configcenterserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zwt
 * @detail
 * @date 2018/12/25
 * @since 1.0
 */
@Controller
@RequestMapping("/login")
public class LoginController {
    @RequestMapping("/doLogin")
    @ResponseBody
    public String doLogin(HttpServletRequest request, HttpServletResponse response){
        String userName=request.getParameter("iUserName");
        String password=request.getParameter("iPassword");
        System.out.println(userName+","+password);
        return "1";
    }
}
