package com.example.sporting.controller;

import com.example.sporting.entity.User;
import com.example.sporting.service.UserService;
import com.example.sporting.util.R;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/front/vip")
public class VipController {

    @Resource
    private UserService userService;

    @PostMapping("/buy")
    public R buyVip(@RequestBody Map<String, Object> params) {
        Long userId = Long.valueOf(params.get("userId").toString());
        // 默认买1个月
        userService.addVipPurchaseExp(userId, "五曜", 1);

        // 返回更新后的用户信息
        return R.ok(userService.getById(userId));
    }
}