package com.example.sporting.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sporting.entity.User;
import com.example.sporting.service.UserService;
import com.example.sporting.util.R;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 1. 用户列表分页查询
     */
    @GetMapping("/list")
    public R list(@RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "10") Integer size) {
        Page<User> pageParam = new Page<>(page, size);
        Page<User> result = userService.page(pageParam);

        Map<String, Object> data = new HashMap<>();
        data.put("list", result.getRecords());
        data.put("total", result.getTotal());

        return R.ok(data);
    }

    /**
     * 2. 获取单个用户详情
     */
    @GetMapping("/{id}")
    public R getInfo(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return R.fail("用户不存在");
        }

        // 返回前清空密码字段，防止泄露
        user.setPassword(null);

        return R.ok(user);
    }

    /**
     * 3. 删除用户
     */
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Long id) {
        userService.removeById(id);
        return R.ok("删除成功");
    }   
}
