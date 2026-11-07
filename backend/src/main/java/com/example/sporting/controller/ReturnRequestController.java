package com.example.sporting.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sporting.entity.ReturnRequest;
import com.example.sporting.entity.User;
import com.example.sporting.service.ReturnRequestService;
import com.example.sporting.service.UserService;
import com.example.sporting.util.R;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/return")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class ReturnRequestController {

    @Resource
    private ReturnRequestService returnRequestService;

    @Resource
    private UserService userService;

    /**
     * 管理员：获取所有售后申请列表（分页，包含用户名）
     */
    @GetMapping("/list")
    public R list(@RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "10") Integer size) {
        Page<ReturnRequest> pageParam = new Page<>(page, size);
        Page<ReturnRequest> result = returnRequestService.page(pageParam);

        // 遍历列表，根据 userId 查出对应的 username
        List<Map<String, Object>> recordsWithUser = result.getRecords().stream().map(req -> {
            Map<String, Object> map = new HashMap<>();

            map.put("id", req.getId());
            map.put("userId", req.getUserId());
            map.put("orderId", req.getOrderId());
            map.put("orderItemId", req.getOrderItemId());
            map.put("productId", req.getProductId());
            map.put("productName", req.getProductName());
            map.put("productImage", req.getProductImage());
            map.put("reason", req.getReason());
            map.put("type", req.getType());
            map.put("status", req.getStatus());
            map.put("applyAt", req.getApplyAt());
            map.put("processedAt", req.getProcessedAt());

            // 查用户名
            User user = userService.getById(req.getUserId());
            map.put("username", user != null ? user.getUsername() : "未知用户");

            return map;
        }).collect(Collectors.toList());

        Map<String, Object> data = new HashMap<>();
        data.put("list", recordsWithUser);
        data.put("total", result.getTotal());
        return R.ok(data);
    }

    /**
     * 管理员：审核通过
     */
    @PostMapping("/approve/{id}")
    public R approve(@PathVariable Long id) {
        ReturnRequest req = returnRequestService.getById(id);
        if (req != null) {
            req.setStatus(1); // 1: 已通过
            req.setProcessedAt(new Date());
            returnRequestService.updateById(req);
            return R.ok("审核已通过");
        }
        return R.fail("单据不存在");
    }

    /**
     * 管理员：拒绝申请
     */
    @PostMapping("/reject/{id}")
    public R reject(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String reason = body.get("reason");
        ReturnRequest req = returnRequestService.getById(id);
        if (req != null) {
            req.setStatus(2); // 2:被拒绝
            if (reason != null && !reason.isEmpty()) {
                req.setReason(reason);
            }
            req.setProcessedAt(new Date());
            returnRequestService.updateById(req);
            return R.ok("申请已驳回");
        }
        return R.fail("未找到该申请");
    }
}

