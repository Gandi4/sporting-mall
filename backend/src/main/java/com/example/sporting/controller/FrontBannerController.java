package com.example.sporting.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.sporting.entity.Banner;
import com.example.sporting.service.BannerService;
import com.example.sporting.util.R;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/front/banner")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class FrontBannerController {

    @Resource
    private BannerService bannerService;

    /**
     * 前台：获取所有启用的轮播图
     * 用于首页展示，只返回状态为启用的轮播图
     */
    @GetMapping("/list")
    public R list() {
        QueryWrapper<Banner> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1)              // 只返回启用状态的轮播图
                .orderByAsc("sort_order");   // 按排序字段升序排列

        List<Banner> banners = bannerService.list(wrapper);

        if (banners.isEmpty()) {
            return R.ok("暂无轮播图数据").setData(banners);
        }

        return R.ok(banners);
    }
}
