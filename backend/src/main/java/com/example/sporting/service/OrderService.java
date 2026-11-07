package com.example.sporting.service;

import com.example.sporting.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
* @author SPORTING
* @description 针对表【order(订单表)】的数据库操作Service
* @createDate 2026-04-01 13:31:37
*/
public interface OrderService extends IService<Order> {

    @Transactional(rollbackFor = Exception.class)
    Long createOrder(Long userId, Long addressId);
    // 获取用户最近的订单简述
    String getUserOrdersContext(Long userId);
}
