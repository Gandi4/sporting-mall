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
@RequestMapping("/front/announcement")
public class FrontAnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    /**
     * 前台获取公告（无需登录）
     */
    @GetMapping("/list")
    public R list() {
        QueryWrapper<Announcement> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1); // 只显示启用(1)的公告 0为草稿
        wrapper.orderByDesc("publish_at ");

        List<Announcement> list = announcementService.list(wrapper);
        return R.ok().setData(list);
    }
}