package com.example.sporting.controller.param;
//LoginParam 类是和前端的form对象相关
import lombok.Data;
@Data //生成getter和setter方法
public class LoginParam {
    private String name;
    private String pwd;
}
