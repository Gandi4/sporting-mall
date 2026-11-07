// src/main/java/com/example.sporting/controller/ManagerController.java
package com.example.sporting.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sporting.entity.Manager;
import com.example.sporting.service.ManagerService;
import com.example.sporting.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/manager")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"}, allowCredentials = "true")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    /**
     * 管理员列表（分页）
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        int current = params.get("page") == null ? 1 : Integer.parseInt(params.get("page").toString());
        int size = params.get("size") == null ? 10 : Integer.parseInt(params.get("size").toString());

        Page<Manager> pageParam = new Page<>(current, size);
        Page<Manager> result = managerService.page(pageParam);

        // 清除密码字段
        result.getRecords().forEach(m -> m.setPassword(null));

        Map<String, Object> data = new HashMap<>();
        data.put("list", result.getRecords());
        data.put("total", result.getTotal());

        return R.ok(data);
    }

    /**
     * 新增管理员
     */
    @PostMapping("/add")
    public R add(@RequestBody Manager manager) {
        boolean success = managerService.save(manager);
        return success ? R.ok("添加成功") : R.fail("添加失败");
    }

    /**
     * 修改管理员
     */
    @PostMapping("/update")
    public R update(@RequestBody Manager manager) {
        // 如果修改密码为空，不更新密码
        if (manager.getPassword() == null || manager.getPassword().trim().isEmpty()) {
            manager.setPassword(null); // MyBatis Plus 会忽略 null 字段
        }
        boolean success = managerService.updateById(manager);
        return success ? R.ok("修改成功") : R.fail("修改失败");
    }

    /**
     * 删除管理员
     */
    @PostMapping("/delete")
    public R delete(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        boolean success = managerService.removeById(id);
        return success ? R.ok("删除成功") : R.fail("删除失败");
    }
}