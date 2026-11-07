package com.example.sporting.service;

import com.example.sporting.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import java.math.BigDecimal;

public interface UserService extends IService<User> {
    // 逻辑1：登录加经验
    void addLoginExp(Long userId);

    // 逻辑2：购买会员加经验
    void addVipPurchaseExp(Long userId, String levelName, int months);

    // 逻辑3：消费加经验
    void addConsumeExp(Long userId, BigDecimal money);
}