package com.example.sporting.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.sporting.entity.Review;
import com.example.sporting.entity.User;
import com.example.sporting.service.ReviewService;
import com.example.sporting.service.UserService;
import com.example.sporting.util.R;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/front/review")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class FrontReviewController {

    @Resource private ReviewService reviewService;
    @Resource private UserService userService;

    /**
     * 获取商品评价列表（带用户实时境界、身份及匿名处理）
     */
    @GetMapping("/list/{productId}")
    public R listByProduct(@PathVariable Long productId) {
        List<Review> reviews = reviewService.lambdaQuery()
                .eq(Review::getProductId, productId)
                .orderByDesc(Review::getCreatedAt)
                .list();

        if (reviews.isEmpty()) {
            return R.ok(new ArrayList<>());
        }

        // 性能优化：批量查询用户信息，避免 N+1 问题
        Set<Long> userIds = reviews.stream().map(Review::getUserId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, User> userMap = new HashMap<>();
        if (!userIds.isEmpty()) {
            List<User> users = userService.listByIds(userIds);
            userMap = users.stream().collect(Collectors.toMap(User::getId, u -> u));
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Review review : reviews) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", review.getId());
            map.put("rating", review.getRating());
            map.put("content", review.getContent());
            map.put("images", review.getImages());
            map.put("createdAt", review.getCreatedAt());

            User user = userMap.get(review.getUserId());

            // 处理匿名或用户不存在的情况
            if (Integer.valueOf(1).equals(review.getIsAnonymous()) || user == null) {
                map.put("username", "匿名道友");
                map.put("avatar", "");
                map.put("currentLevel", "神秘境界");
                map.put("isVip", 0);
            } else {
                map.put("username", user.getUsername());
                map.put("avatar", user.getAvatar());
                map.put("currentLevel", user.getCurrentLevel() == null ? "灵胎" : user.getCurrentLevel());
                map.put("isVip", user.getIsVip());
            }
            result.add(map);
        }
        return R.ok(result);
    }

    /**
     * 发布评价接口
     */
    @PostMapping("/add")
    public R addReview(@RequestBody Review review) {
        // 检查是否已经评价过（防止刷单评价）
        long count = reviewService.lambdaQuery()
                .eq(Review::getOrderItemId, review.getOrderItemId())
                .count();
        if (count > 0) return R.fail("该商品已评价过了");

        review.setCreatedAt(new Date());
        boolean saved = reviewService.save(review);

        //评价后增加少量修为（经验）
         userService.addConsumeExp(review.getUserId(), new BigDecimal("3"));

        return saved ? R.ok("评价发布成功") : R.fail("评价失败");
    }
}
