package com.example.sporting.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sporting.entity.Product;
import com.example.sporting.mapper.ProductMapper;
import com.example.sporting.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl
        extends ServiceImpl<ProductMapper, Product>
        implements ProductService {

    @Override
    public Page<Product> getProductByPage(int currentPage, int pageSize) {

        Page<Product> page = new Page<>(currentPage, pageSize);

        return this.page(page);
    }

    /**
     * 获取推荐商品上下文信息（用于AI对话上下文注入）
     * @return 推荐商品描述
     */
    @Override
    public String getRecommendProductsContext() {
        List<Product> productList = this.lambdaQuery()
                .last("LIMIT 5")
                .list();

        if (productList.isEmpty()) {
            return "目前没有可推荐的商品。";
        }

        StringBuilder recommendStr = new StringBuilder();
        for (Product product : productList) {
            recommendStr.append(product.getName())
                    .append("(价格:")
                    .append(product.getPrice())
                    .append("), ");
        }
        return recommendStr.toString();
    }
}
