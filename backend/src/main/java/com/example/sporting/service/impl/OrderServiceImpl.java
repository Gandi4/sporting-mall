package com.example.sporting.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sporting.entity.*;
import com.example.sporting.mapper.OrderMapper;
import com.example.sporting.service.*;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Resource private CartService cartService;
    @Resource private ProductService productService;
    @Resource private AddressService addressService;
    @Resource private OrderItemService orderItemService;
    @Resource private OrderTrackService orderTrackService;

    // 关键注入 userService
    @Resource private UserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createOrder(Long userId, Long addressId) {
        // 1. 获取该用户的购物车数据
        List<Cart> carts = cartService.lambdaQuery().eq(Cart::getUserId, userId).list();
        if (carts.isEmpty()) throw new RuntimeException("购物车为空，无法下单");

        // 2. 提前计算总价并准备订单明细
        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> itemList = new ArrayList<>();

        for (Cart c : carts) {
            Product p = productService.getById(c.getProductId());
            if (p == null) continue;

            BigDecimal subtotal = p.getPrice().multiply(new BigDecimal(c.getQuantity()));
            total = total.add(subtotal);

            OrderItem item = new OrderItem();
            item.setProductId(p.getId());
            item.setProductName(p.getName());
            item.setProductImage(p.getMainImage());
            item.setPrice(p.getPrice());
            item.setQuantity(c.getQuantity());
            item.setSubtotal(subtotal);
            itemList.add(item);
        }

        // 3. 获取地址并生成快照
        Address addr = addressService.getById(addressId);
        String addrSnapshot = addr.getReceiverName() + " " + addr.getPhone() + " " +
                addr.getProvince() + addr.getCity() + addr.getDistrict() + addr.getDetailAddress();

        // 4. 创建并保存完整的订单记录
        Order order = new Order();
        order.setOrderNo("SN" + System.currentTimeMillis());
        order.setUserId(userId);
        order.setAddressSnapshot(addrSnapshot);
        order.setStatus("paid");
        order.setTotalAmount(total);
        order.setPayAmount(total);
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setCreatedAt(new Date());
        this.save(order);

        // 5. 保存订单明细项
        for (OrderItem item : itemList) {
            item.setOrderId(order.getId());
            orderItemService.save(item);
        }

        // 6. 初始化一条物流追踪记录
        OrderTrack track = new OrderTrack();
        track.setOrderId(order.getId());
        track.setStatus("订单已下单，等待发货");
        track.setUpdatedAt(new Date());
        orderTrackService.save(track);

        // 7. 清空用户的购物车
        cartService.lambdaUpdate().eq(Cart::getUserId, userId).remove();

        // 8. 购物的钱变为经验
        userService.addConsumeExp(userId, total);

        return order.getId();
    }
    /**
     * 获取用户订单上下文信息（用于AI对话上下文注入）
     * @param userId 用户ID
     * @return 订单上下文描述
     */
    public String getUserOrdersContext(Long userId) {
        List<Order> orders = this.lambdaQuery()
                .eq(Order::getUserId, userId)
                .orderByDesc(Order::getCreatedAt)
                .last("LIMIT 3")
                .list();

        if (orders.isEmpty()) {
            return "该用户目前没有订单。";
        }

        StringBuilder contextStr = new StringBuilder();
        for (Order order : orders) {
            contextStr.append(String.format("- 订单号:%s, 金额:%s, 状态:%s; ",
                    order.getOrderNo(), order.getTotalAmount(), order.getStatus()));
        }
        return contextStr.toString();
    }
}