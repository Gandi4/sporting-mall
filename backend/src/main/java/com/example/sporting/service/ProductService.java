package com.example.sporting.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sporting.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author SPORTING
 * @description 针对表【product(商品表)】的数据库操作Service
 * @createDate 2025-12-17 17:23:26
 */
public interface ProductService extends IService<Product> {

    /**
     * 分页查询商品信息
     *
     * @param currentPage 页码
     * @param pageSize    每页显示条数
     * @return 分页结果
     */
    Page<Product> getProductByPage(int currentPage, int pageSize);
    String getRecommendProductsContext();
}