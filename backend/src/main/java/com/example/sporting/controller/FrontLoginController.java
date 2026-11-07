package com.example.sporting.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.sporting.entity.User;
import com.example.sporting.service.UserService;
import com.example.sporting.util.R;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/front/user")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class FrontLoginController {

    @Resource
    private UserService userService;

    /**
     * 1. 获取用户信息 (核心同步接口)
     * 支持前端传递 userId 查询，或根据当前登录 Session 自动获取
     */
    @GetMapping("/info")
    public R info(@RequestParam(required = false) Long userId) {
        if (userId == null) {
            return R.fail("请先登录").setCode(401);
        }

        User user = userService.getById(userId);
        if (user != null) {
            user.setPassword(null); // 安全处理：屏蔽密码
            return R.ok(user);
        }
        return R.fail("用户不存在");
    }

    /**
     * 2. 登录接口
     */
    @PostMapping("/login")
    public R login(@RequestBody User loginUser) {
        // 1. 根据用户名查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", loginUser.getUsername());
        User user = userService.getOne(queryWrapper);

        // 2. 校验密码
        if (user == null || !user.getPassword().equals(loginUser.getPassword())) {
            return R.fail("用户名或密码错误");
        }

        // 3. 登录成功，清除敏感信息后返回对象（确保包含 id）
        user.setPassword(null);
        return R.ok(user);
    }

    /**
     * 3. 注册接口
     * 初始化用户的修仙起点：灵胎境界
     */
    @PostMapping("/register")
    public R register(@RequestBody User user) {
        // 基础校验
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return R.fail("用户名不能为空");
        }
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            return R.fail("密码安全性过低（至少6位）");
        }

        // 查重
        User existingUser = userService.getOne(
                new QueryWrapper<User>()
                        .eq("username", user.getUsername())
        );
        if (existingUser != null) {
            return R.fail("该名号已被占用，请换一个");
        }

        // 初始化新用户属性
        user.setStatus(1);            // 激活状态
        user.setIsVip(0);             // 初始为普通用户
        user.setExperience(0L);       // 初始修为为 0
        user.setCurrentLevel("灵胎");  // 初始境界：灵胎
        user.setLoginDays(0);         // 登录天数

        Date now = new Date();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        boolean saved = userService.save(user);

        if (saved) {
            return R.ok("注册成功！请登录开始修炼");
        } else {
            return R.fail("注册失败，请稍后再试");
        }
    }

    /**
     * 4. 退出登录
     */
    @PostMapping("/logout")
    public R logout() {
        return R.ok("退出成功，期待再见");
    }
}
