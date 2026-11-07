package com.example.sporting.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.sporting.entity.Favorite;
import com.example.sporting.entity.Product;
import com.example.sporting.service.FavoriteService;
import com.example.sporting.service.ProductService;
import com.example.sporting.util.R;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/front/favorite")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class FrontFavoriteController {
    @Resource private FavoriteService favoriteService;
    @Resource private ProductService productService;

    @PostMapping("/add")
    public R add(@RequestBody Favorite favorite) {
        Favorite exist = favoriteService.getOne(new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, favorite.getUserId()).eq(Favorite::getProductId, favorite.getProductId()));
        if (exist != null) return R.fail("已经在收藏夹了");
        favoriteService.save(favorite);
        return R.ok("收藏成功");
    }

    @GetMapping("/list")
    public R list(@RequestParam Long userId) {
        List<Favorite> favs = favoriteService.lambdaQuery().eq(Favorite::getUserId, userId).list();
        List<Product> products = new ArrayList<>();
        for (Favorite f : favs) {
            Product p = productService.getById(f.getProductId());
            if (p != null) products.add(p);
        }
        return R.ok(products);
    }

    @DeleteMapping("/remove")
    public R remove(@RequestParam Long userId, @RequestParam Long productId) {
        favoriteService.remove(new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, userId).eq(Favorite::getProductId, productId));
        return R.ok("取消收藏成功");
    }

    @GetMapping("/check")
    public R check(@RequestParam Long userId, @RequestParam Long productId) {
        Favorite exist = favoriteService.lambdaQuery()
                .eq(Favorite::getUserId, userId)
                .eq(Favorite::getProductId, productId).one();
        return R.ok(exist != null); // 返回 true 或 false
    }
    @PostMapping("/batch-remove")
    public R batchRemove(@RequestBody Map<String, Object> params) {
        Long userId = Long.valueOf(params.get("userId").toString());
        List<Integer> productIds = (List<Integer>) params.get("productIds");
        favoriteService.remove(new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, userId)
                .in(Favorite::getProductId, productIds));
        return R.ok("批量取消成功");
    }
}