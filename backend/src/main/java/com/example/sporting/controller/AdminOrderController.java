package com.example.sporting.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sporting.entity.Order;
import com.example.sporting.service.OrderService;
import com.example.sporting.util.R;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/order/admin")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class AdminOrderController {

    @Resource private OrderService orderService;

    @GetMapping("/list")
    public R list(@RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "10") Integer size) {
        Page<Order> p = new Page<>(page, size);
        Page<Order> result = orderService.page(p);

        Map<String, Object> data = new HashMap<>();
        // MyBatis-Plus 分页结果在 records 字段，提取到 list 给前端
        data.put("list", result.getRecords());
        data.put("total", result.getTotal());

        return R.ok(data);
    }

    @DeleteMapping("/{id}")
    public R delete(@PathVariable Long id) {
        orderService.removeById(id);
        return R.ok("删除成功");
    }
} 