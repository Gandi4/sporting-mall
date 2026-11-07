// src/main/java/com/example/sporting/controller/ReviewController.java
package com.example.sporting.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sporting.entity.Review;
import com.example.sporting.service.ReviewService;
import com.example.sporting.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/review")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"}, allowCredentials = "true")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    /**
     * 评价列表（分页 + 可选搜索商品名或用户名）
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        int current = params.get("page") == null ? 1 : Integer.parseInt(params.get("page").toString());
        int size = params.get("size") == null ? 10 : Integer.parseInt(params.get("size").toString());
        String keyword = (String) params.get("keyword");

        Page<Review> pageParam = new Page<>(current, size);

        QueryWrapper<Review> wrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.and(w -> w.like("product_name", keyword).or().like("username", keyword));
        }
        wrapper.orderByDesc("created_at"); // 最新评价在最上面

        Page<Review> result = reviewService.page(pageParam, wrapper);

        Map<String, Object> data = new HashMap<>();
        data.put("list", result.getRecords());
        data.put("total", result.getTotal());

        return R.ok(data);
    }

    /**
     * 显示/隐藏评价（status: 1 显示，0 隐藏）
     */
    @PostMapping("/toggleStatus")
    public R toggleStatus(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        Integer status = Integer.valueOf(params.get("status").toString());

        Review review = new Review();
        review.setId(id);
        review.setStatus(status); // Lombok 自动生成的 setter

        boolean success = reviewService.updateById(review);
        return success ? R.ok("操作成功") : R.fail("操作失败");
    }

    /**
     * 删除评价
     */
    @PostMapping("/delete")
    public R delete(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        boolean success = reviewService.removeById(id);
        return success ? R.ok("删除成功") : R.fail("删除失败");
    }
}