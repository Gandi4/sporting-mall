// src/main/java/com/example/sporting/controller/CategoryController.java
package com.example.sporting.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sporting.entity.Category;
import com.example.sporting.service.CategoryService;
import com.example.sporting.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/category")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"}, allowCredentials = "true")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 后台：分类分页
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        int current = params.get("page") == null ? 1 : Integer.parseInt(params.get("page").toString());
        int size = params.get("size") == null ? 10 : Integer.parseInt(params.get("size").toString());

        Page<Category> pageParam = new Page<>(current, size);

        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("sort_order");

        Page<Category> result = categoryService.page(pageParam, wrapper);

        Map<String, Object> data = new HashMap<>();
        data.put("list", result.getRecords());
        data.put("total", result.getTotal());

        return R.ok(data);
    }

    /**
     * 后台：所有启用分类（下拉框）
     */
    @GetMapping("/all")
    public R all() {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1)
                .orderByAsc("sort_order");
        return R.ok(categoryService.list(wrapper));
    }

    @PostMapping("/add")
    public R add(@RequestBody Category category) {
        if (category.getStatus() == null) {
            category.setStatus(1);
        }
        return categoryService.save(category) ? R.ok("添加成功") : R.fail("添加失败");
    }

    @PostMapping("/update")
    public R update(@RequestBody Category category) {
        return categoryService.updateById(category) ? R.ok("修改成功") : R.fail("修改失败");
    }

    @PostMapping("/delete")
    public R delete(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        return categoryService.removeById(id) ? R.ok("删除成功") : R.fail("删除失败");
    }
}
