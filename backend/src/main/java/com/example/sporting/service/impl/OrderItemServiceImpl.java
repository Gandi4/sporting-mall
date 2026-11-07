package com.example.sporting.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sporting.entity.OrderItem;
import com.example.sporting.service.OrderItemService;
import com.example.sporting.mapper.OrderItemMapper;
import org.springframework.stereotype.Service;

/**
* @author SPORTING
* @description 针对表【order_item(订单项表)】的数据库操作Service实现
* @createDate 2026-04-01 13:31:41
*/
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem>
    implements OrderItemService{

}




