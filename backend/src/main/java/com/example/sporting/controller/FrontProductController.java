package com.example.sporting.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.sporting.entity.Product;
import com.example.sporting.service.ProductService;
import com.example.sporting.util.R;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/front/product")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class FrontProductController {

    @Resource
    private ProductService productService;

    /**
     * 列表接口：支持分类、关键词搜索、价格排序
     */
    @GetMapping("/list")
    public R list(@RequestParam(required = false) Long categoryId,
                  @RequestParam(required = false) String keyword,
                  @RequestParam(required = false) String sort) {

        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getStatus, "on_sale");

        // 1. 分类筛选
        if (categoryId != null) {
            wrapper.eq(Product::getCategoryId, categoryId);
        }

        // 2. 关键词模糊搜索 (匹配名称)
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Product::getName, keyword);
        }

        // 3. 价格排序处理
        if ("priceAsc".equals(sort)) {
            wrapper.orderByAsc(Product::getPrice);
        } else if ("priceDesc".equals(sort)) {
            wrapper.orderByDesc(Product::getPrice);
        } else {
            wrapper.orderByDesc(Product::getCreatedAt);
        }

        return R.ok(productService.list(wrapper));
    }

    @GetMapping("/{id}")
    public R detail(@PathVariable Long id) {
        Product product = productService.getById(id);
        if (product == null || !"on_sale".equals(product.getStatus())) {
            return R.fail("商品不存在或已下架");
        }
        return R.ok(product);
    }
}