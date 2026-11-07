package com.example.sporting.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.sporting.entity.Category;
import com.example.sporting.service.CategoryService;
import com.example.sporting.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/front/category")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"}, allowCredentials = "true")
public class FrontCategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 前台：获取所有启用分类（用于导航栏 / 首页）
     * 按 sort_order 升序排列
     */
    @GetMapping("/list")
    public R list() {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1)              // 只返回启用状态（status=1）的分类
                .orderByAsc("sort_order");   // 按排序字段升序（前端展示顺序）

        List<Category> categories = categoryService.list(wrapper);

        if (categories.isEmpty()) {
            return R.ok("暂无分类数据").setData(categories);
        }

        return R.ok(categories);
    }
}