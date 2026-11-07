package com.example.sporting.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sporting.entity.Product;
import com.example.sporting.service.ProductService;
import com.example.sporting.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/product")
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 后台商品分页列表（含分类 / 搜索）
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {

        int page = params.get("page") == null ? 1 : Integer.parseInt(params.get("page").toString());
        int size = params.get("size") == null ? 10 : Integer.parseInt(params.get("size").toString());

        Page<Product> pageParam = new Page<>(page, size);
        QueryWrapper<Product> wrapper = new QueryWrapper<>();

        if (params.get("name") != null) {
            wrapper.like("name", params.get("name"));
        }
        if (params.get("categoryId") != null) {
            wrapper.eq("category_id", params.get("categoryId"));
        }

        wrapper.orderByDesc("id");

        Page<Product> result = productService.page(pageParam, wrapper);

        Map<String, Object> data = new HashMap<>();
        data.put("list", result.getRecords());
        data.put("total", result.getTotal());

        return R.ok(data);
    }

    @PostMapping("/add")
    public R add(@RequestBody Product product) {
        return productService.save(product) ? R.ok() : R.fail();
    }

    @PostMapping("/update")
    public R update(@RequestBody Product product) {
        return productService.updateById(product) ? R.ok() : R.fail();
    }

    @PostMapping("/toggleStatus")
    public R toggleStatus(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        String status = params.get("status").toString();

        Product product = new Product();
        product.setId(id);
        product.setStatus(status);

        return productService.updateById(product) ? R.ok() : R.fail();
    }
}
