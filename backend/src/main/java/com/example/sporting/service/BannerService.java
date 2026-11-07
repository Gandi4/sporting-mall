package com.example.sporting.service;

import com.example.sporting.entity.Banner;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
/**
* @author SPORTING
* @description 针对表【banner(首页轮播图)】的数据库操作Service
* @createDate 2025-12-22 11:57:42
*/

public interface BannerService extends IService<Banner> {
    List<Banner> getAllBanners();
    boolean saveBanner(Banner banner);
    boolean updateBanner(Banner banner);
    boolean removeBanner(Long id);
}
