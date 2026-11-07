package com.example.sporting.service;

import com.example.sporting.entity.Cart;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sporting.vo.CartVO;

import java.util.List;

/**
* @author SPORTING
* @description 针对表【cart(购物车表)】的数据库操作Service
* @createDate 2026-03-19 21:22:30
*/
public interface CartService extends IService<Cart> {
    List<CartVO> getCartVOList(Long userId);
}
