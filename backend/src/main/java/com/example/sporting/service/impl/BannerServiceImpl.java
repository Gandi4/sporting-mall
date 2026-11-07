package com.example.sporting.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sporting.entity.Banner;
import com.example.sporting.mapper.BannerMapper;
import com.example.sporting.service.BannerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {

    @Override
    public List<Banner> getAllBanners() {
        return this.list(); // 使用 MyBatis-Plus 提供的查询所有功能
    }

    @Override
    public boolean saveBanner(Banner banner) {
        return this.save(banner); // 保存轮播图
    }

    @Override
    public boolean updateBanner(Banner banner) {
        return this.updateById(banner); // 更新轮播图
    }

    @Override
    public boolean removeBanner(Long id) {
        return this.removeById(id); // 删除轮播图
    }
}
