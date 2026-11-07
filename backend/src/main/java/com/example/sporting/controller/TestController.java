package com.example.sporting.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
// @Controller + @ResponseBody,返回json格式的数据
@RestController
@RequestMapping("/test")  //请求路径
public class TestController {
    @GetMapping("/hello")
    public String hello(){
        System.out.println("hello 测试 !");
        return "hello";
    }
}