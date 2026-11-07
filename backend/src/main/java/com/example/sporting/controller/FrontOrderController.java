package com.example.sporting.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.sporting.entity.*;
import com.example.sporting.service.*;
import com.example.sporting.vo.CartVO;
import com.example.sporting.util.R;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/front/order")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class FrontOrderController {

    @Resource private OrderService orderService;
    @Resource private CartService cartService;
    @Resource private ProductService productService;
    @Resource private AddressService addressService;
    @Resource private OrderItemService orderItemService;
    @Resource private OrderTrackService orderTrackService;

    /**
     * 1. 结算预览
     */
    @GetMapping("/preview")
    public R preview(@RequestParam Long userId) {
        List<Cart> carts = cartService.lambdaQuery().eq(Cart::getUserId, userId).list();
        List<CartVO> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        for (Cart c : carts) {
            Product p = productService.getById(c.getProductId());
            if (p != null) {
                CartVO vo = new CartVO();
                vo.setProductId(p.getId());
                vo.setProductName(p.getName());
                vo.setProductImage(p.getMainImage());
                vo.setPrice(p.getPrice());
                vo.setQuantity(c.getQuantity());
                BigDecimal sub = p.getPrice().multiply(new BigDecimal(c.getQuantity()));
                vo.setSubtotal(sub);
                total = total.add(sub);
                items.add(vo);
            }
        }
        Address addr = addressService.lambdaQuery().eq(Address::getUserId, userId).eq(Address::getIsDefault, 1).one();
        Map<String, Object> res = new HashMap<>();
        res.put("items", items);
        res.put("totalAmount", total);
        res.put("address", addr != null ? addr : new HashMap<>());
        return R.ok(res);
    }

    /**
     * 2. 提交订单
     */
    @PostMapping("/submit")
    public R submit(@RequestBody Map<String, Object> params) {
        if (params.get("userId") == null || params.get("addressId") == null) {
            return R.fail("用户ID和地址ID不能为空");
        }

        try {
            Long userId = Long.valueOf(params.get("userId").toString());
            Long addressId = Long.valueOf(params.get("addressId").toString());

            Long orderId = orderService.createOrder(userId, addressId);

            Map<String, Object> result = new HashMap<>();
            result.put("orderId", orderId);
            return R.ok(result);
        } catch (NumberFormatException e) {
            return R.fail("参数格式错误");
        } catch (RuntimeException e) {
            return R.fail(e.getMessage());
        }
    }

    /**
     * 3. 我的订单列表（支持按订单号、商品名搜索）
     */
    @GetMapping("/list")
    public R list(@RequestParam Long userId,
                  @RequestParam(required = false) String orderNo,
                  @RequestParam(required = false) String productName) {

        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId);

        // A. 按订单号搜索
        if (orderNo != null && !orderNo.trim().isEmpty()) {
            wrapper.like(Order::getOrderNo, orderNo.trim());
        }

        // B. 按商品名搜索（需联表查询订单详情）
        if (productName != null && !productName.trim().isEmpty()) {
            List<OrderItem> items = orderItemService.lambdaQuery()
                    .like(OrderItem::getProductName, productName.trim())
                    .list();

            if (items.isEmpty()) {
                return R.ok(new ArrayList<>());
            }

            Set<Long> orderIds = items.stream().map(OrderItem::getOrderId).collect(Collectors.toSet());
            wrapper.in(Order::getId, orderIds);
        }

        wrapper.orderByDesc(Order::getCreatedAt);
        List<Order> orders = orderService.list(wrapper);
        return R.ok(orders);
    }

    /**
     * 4. 订单详情接口（展示商品明细和物流信息）
     */
    @GetMapping("/detail/{id}")
    public R detail(@PathVariable Long id) {
        Order order = orderService.getById(id);
        if (order == null) return R.fail("订单不存在");

        List<OrderItem> items = orderItemService.lambdaQuery()
                .eq(OrderItem::getOrderId, id)
                .list();

        List<OrderTrack> tracks = orderTrackService.lambdaQuery()
                .eq(OrderTrack::getOrderId, id)
                .orderByDesc(OrderTrack::getUpdatedAt)
                .list();

        Map<String, Object> data = new HashMap<>();
        data.put("order", order);
        data.put("items", items);
        data.put("tracks", tracks);

        return R.ok(data);
    }
}
