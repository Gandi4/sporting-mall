package com.example.sporting.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sporting.entity.Cart;
import com.example.sporting.entity.Product;
import com.example.sporting.service.CartService;
import com.example.sporting.mapper.CartMapper;
import com.example.sporting.service.ProductService;
import com.example.sporting.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
* @author SPORTING
* @description 针对表【cart(购物车表)】的数据库操作Service实现
* @createDate 2026-03-19 21:22:30
*/
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart>
    implements CartService{
    @Autowired
    private ProductService productService;
    public List<CartVO> getCartVOList(Long userId) {

        List<Cart> carts = this.list(
                new QueryWrapper<Cart>().eq("user_id", userId)
        );

        List<CartVO> list = new ArrayList<>();

        for (Cart c : carts) {

            Product p = productService.getById(c.getProductId());

            CartVO vo = new CartVO();
            vo.setCartId(c.getId());

            vo.setProductId(p.getId());
            vo.setProductName(p.getName());
            vo.setProductImage(p.getMainImage());

            vo.setPrice(p.getPrice());
            vo.setQuantity(c.getQuantity());

            vo.setSubtotal(
                    p.getPrice().multiply(new BigDecimal(c.getQuantity()))
            );

            list.add(vo);
        }

        return list;
    }
}




