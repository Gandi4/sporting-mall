package com.example.sporting.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sporting.entity.User;
import com.example.sporting.entity.VipConfig;
import com.example.sporting.mapper.UserMapper;
import com.example.sporting.service.UserService;
import com.example.sporting.service.VipConfigService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Date;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private VipConfigService vipConfigService;

    @Override
    public void addLoginExp(Long userId) {
        User user = this.getById(userId);
        if (user == null) return;

        // 逻辑：1, 3, 5, 7, 9, 11, 15 (七天循环)
        int currentDay = (user.getLoginDays() == null ? 0 : user.getLoginDays()) % 7 + 1;
        long[] expTable = {1, 3, 5, 7, 9, 11, 15};
        long gain = expTable[currentDay - 1];

        user.setExperience((user.getExperience() == null ? 0 : user.getExperience()) + gain);
        user.setLoginDays(currentDay);
        user.setLastLoginDate(new Date());
        updateUserLevel(user);
    }

    @Override
    public void addVipPurchaseExp(Long userId, String levelName, int months) {
        User user = this.getById(userId);
        if (user == null) return;

        // 五曜每日+15，六合每日+20。这里计算一次性到账总经验
        long dailyRate = levelName.contains("六合") ? 20 : 15;
        long totalGain = dailyRate * 30 * months;

        user.setExperience((user.getExperience() == null ? 0 : user.getExperience()) + totalGain);
        updateUserLevel(user);
    }

    @Override
    public void addConsumeExp(Long userId, BigDecimal money) {
        User user = this.getById(userId);
        if (user == null) return;

        // 1元 = 1000 经验
        long gain = money.multiply(new BigDecimal(1000)).longValue();

        long newExp = (user.getExperience() == null ? 0 : user.getExperience()) + gain;
        user.setExperience(newExp);

        //
        updateUserLevel(user);
    }

    private void updateUserLevel(User user) {
        VipConfig match = vipConfigService.lambdaQuery()
                .le(VipConfig::getMinExp, user.getExperience())
                .orderByDesc(VipConfig::getMinExp)
                .last("limit 1").one();

        if (match != null) {
            user.setCurrentLevel(match.getLevelName());
            user.setIsVip(match.getIsSvip());
        }
        this.updateById(user);
    }
}