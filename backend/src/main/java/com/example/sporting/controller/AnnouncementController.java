package com.example.sporting.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.sporting.entity.Announcement;
import com.example.sporting.service.AnnouncementService;
import com.example.sporting.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"}, allowCredentials = "true")
@RequestMapping("/api/announcement")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    // 查询
    @GetMapping("/list")
    public R list() {
        List<Announcement> list = announcementService.list(
                new QueryWrapper<Announcement>().orderByDesc("publish_at")
        );
        return R.ok().setData(list);
    }

    // 新增
    @PostMapping("/add")
    public R add(@RequestBody Announcement announcement) {
        announcementService.save(announcement);
        return R.ok();
    }

    // 修改
    @PutMapping("/update")
    public R update(@RequestBody Announcement announcement) {
        announcementService.updateById(announcement);
        return R.ok();
    }

    // 删除
    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable Integer id) {
        announcementService.removeById(id);
        return R.ok();
    }
}