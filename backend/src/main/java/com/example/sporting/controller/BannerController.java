package com.example.sporting.controller;

import com.example.sporting.entity.Banner;
import com.example.sporting.service.BannerService;
import com.example.sporting.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/banner")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    // 确保这个路径末尾有斜杠
    private static final String UPLOAD_DIR = "D:/code/sporting-vue/public/uploads/";

    @GetMapping("/list")
    public R list() {
        List<Banner> banners = bannerService.getAllBanners();
        return R.ok(banners);
    }

    @PostMapping("/add")
    public R add(@RequestBody Banner banner) {
        return bannerService.saveBanner(banner) ? R.ok("新增成功") : R.fail("新增失败");
    }

    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable Long id) {
        return bannerService.removeBanner(id) ? R.ok("删除成功") : R.fail("删除失败");
    }

    @PostMapping("/update")
    public R update(@RequestBody Banner banner) {
        return bannerService.updateBanner(banner) ? R.ok("修改成功") : R.fail("修改失败");
    }

    /**
     * 处理轮播图图片上传
     */
    @PostMapping("/upload")
    public R uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) return R.fail("上传文件不能为空");

        // 1. 确保目录存在
        File uploadDirectory = new File(UPLOAD_DIR);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }

        // 2. 构造文件名：增加时间戳防止文件名冲突和浏览器缓存
        String originalName = file.getOriginalFilename();
        String fileName = System.currentTimeMillis() + "_" + originalName;

        // 3. 保存文件到硬盘
        File dest = new File(UPLOAD_DIR + fileName);
        file.transferTo(dest);

        // 4. 返回前端可直接访问的 URL
        // 配合 WebConfig 里的映射，返回 http://localhost:80/uploads/文件名
        String imageUrl = "http://localhost:80/uploads/" + fileName;

        return R.ok("上传成功", imageUrl);
    }
}