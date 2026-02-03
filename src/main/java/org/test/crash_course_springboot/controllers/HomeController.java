package org.test.crash_course_springboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class HomeController {
    @GetMapping
    public String getHomePage(){
        return "Welcome to home page";
    }

    @GetMapping("/dashboard")
    public String getDashboard(){
        return "Login Successful";
    }

}


