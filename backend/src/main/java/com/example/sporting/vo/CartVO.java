//用来展示 购物车中的商品信息，包括商品名、单价、数量、总价等。你需要将数据库中存储的数据（例如 cart 表）和其他相关数据
//（例如 product 表中的商品名称和价格）结合起来，通过 VO（CartVO）来做处理和返回
package com.example.sporting.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartVO {

    private Long cartId;

    private Long productId;
    private String productName;
    private String productImage;

    private BigDecimal price;
    private Integer quantity;

    private BigDecimal subtotal;
}
