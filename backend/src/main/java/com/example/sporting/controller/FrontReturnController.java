package com.example.sporting.controller;

import com.example.sporting.entity.ReturnRequest;
import com.example.sporting.service.ReturnRequestService;
import com.example.sporting.util.R;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/front/return")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class FrontReturnController {

    @Resource
    private ReturnRequestService returnRequestService;

    /**
     * 获取个人售后列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Long userId) {
        return R.ok(returnRequestService.lambdaQuery()
                .eq(ReturnRequest::getUserId, userId)
                .orderByDesc(ReturnRequest::getApplyAt).list());
    }

    /**
     * 核心接口：提交售后申请
     */
    @PostMapping("/apply")
    public R apply(@RequestBody ReturnRequest request) {
        // 设置初始数据
        request.setStatus(0); // 0:待审核
        request.setApplyAt(new Date());

        boolean saved = returnRequestService.save(request);
        return saved ? R.ok("申请已提交，等待执事审核") : R.fail("提交失败");
    }
}