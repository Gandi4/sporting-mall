package com.example.sporting.controller;

import com.example.sporting.entity.Cart;
import com.example.sporting.entity.Product;
import com.example.sporting.service.CartService;
import com.example.sporting.service.ProductService;
import com.example.sporting.util.R;
import com.example.sporting.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController

@RequestMapping("/front/cart")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"}, allowCredentials = "true")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    /**
     * 获取购物车列表（前台）
     * 确保一个用户只能有一个购物车
     */
    @GetMapping("/list/{userId}")
    public R list(@PathVariable Long userId) {

        // 确保用户登录
        if (userId == null || userId <= 0) {
            return R.fail("用户未登录").setCode(401); // 未登录，返回 401 错误
        }

        // 查询购物车数据
        List<Cart> cartList = cartService.lambdaQuery()
                .eq(Cart::getUserId, userId)
                .list();

        // 映射成 CartVO，包含商品信息
        List<CartVO> result = cartList.stream().map(cart -> {
            Product product = productService.getById(cart.getProductId());

            CartVO vo = new CartVO();
            vo.setCartId(cart.getId());
            vo.setProductId(product.getId());
            vo.setProductName(product.getName());
            vo.setProductImage(product.getMainImage()); // 商品图片字段
            vo.setPrice(product.getPrice());
            vo.setQuantity(cart.getQuantity());
            vo.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity()))); // 小计

            return vo;
        }).toList();

        return R.ok().setData(result); // 返回购物车商品列表
    }

    /**
     * 加入购物车
     */
    @PostMapping("/add")
    public R add(@RequestBody Cart cart) {

        Cart exist = cartService.lambdaQuery()
                .eq(Cart::getUserId, cart.getUserId())
                .eq(Cart::getProductId, cart.getProductId())
                .one(); // 确保同一个用户只能有一个商品

        if (exist != null) {
            exist.setQuantity(exist.getQuantity() + cart.getQuantity()); // 增加数量
            cartService.updateById(exist);
            return R.ok("数量已更新");
        }

        // 新增购物车商品
        cartService.save(cart);
        return R.ok("已加入购物车");
    }

    /**
     * 修改数量
     */
    @PostMapping("/update")
    public R update(@RequestBody Cart cart) {
        cartService.updateById(cart); // 更新购物车中的商品数量
        return R.ok();
    }

    /**
     * 删除购物车商品
     * 修复：增加用户ID校验，防止越权删除
     */
    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable Long id, @RequestParam Long userId) {
        // 先查询该购物车项是否存在且属于当前用户
        Cart cart = cartService.lambdaQuery()
                .eq(Cart::getId, id)
                .eq(Cart::getUserId, userId)
                .one();

        if (cart == null) {
            return R.fail("购物车项不存在或无权操作");
        }

        cartService.removeById(id); // 删除指定的购物车商品
        return R.ok("删除成功");
    }
}
