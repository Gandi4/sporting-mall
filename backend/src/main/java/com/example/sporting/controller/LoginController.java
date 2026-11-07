// src/main/java/com/example/sporting/controller/LoginController.java
package com.example.sporting.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.sporting.entity.Manager;
import com.example.sporting.service.ManagerService;
import com.example.sporting.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录专用控制器（独立于管理员管理）
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"}, allowCredentials = "true")
public class LoginController {

    @Autowired
    private ManagerService managerService;

    /**
     * 管理员登录
     */
    @PostMapping("/login")
    public R login(@RequestBody LoginParam loginParam) {
        // 参数校验
        if (loginParam.getName() == null || loginParam.getPwd() == null ||
                loginParam.getName().trim().isEmpty() || loginParam.getPwd().trim().isEmpty()) {
            return R.fail("用户名或密码不能为空");
        }

        QueryWrapper<Manager> wrapper = new QueryWrapper<>();
//        与数据库一致
        wrapper.eq("manager", loginParam.getName().trim());
        wrapper.eq("password", loginParam.getPwd().trim());

        Manager manager = managerService.getOne(wrapper);

        if (manager == null) {
            return R.fail("登录失败，用户名或密码错误");
        }

        // 登录成功：清除密码，防止泄露
        manager.setPassword(null);

        // 可返回必要信息（如 id、用户名、角色等）
        Map<String, Object> data = new HashMap<>();
        data.put("managerInfo", manager);

        return R.ok("登录成功").setData(data);
    }
}

/**
 * 登录参数接收类（放在同一个文件内或单独文件均可）
 */
class LoginParam {
    private String name;
    private String pwd;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "LoginParam{" +
                "name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                '}';
    }
}