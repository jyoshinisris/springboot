package org.test.crash_course_springboot.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @GetMapping("/add_two_numbers")
    public int addTwoNumbers(
            @RequestParam int a,
            @RequestParam int b){
        return a + b;
    }

    @GetMapping("/mul_two_numbers")
    public int mulTwoNumbers(
            @RequestParam int a,
            @RequestParam int b){
        return a * b ;
    }
}
