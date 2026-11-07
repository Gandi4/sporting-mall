package com.example.sporting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 订单项表
 * @TableName order_item
 */
@TableName(value ="order_item")
@Data
public class OrderItem {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    @TableField(value = "order_id")
    private Long orderId;

    /**
     * 
     */
    @TableField(value = "product_id")
    private Long productId;

    /**
     * 商品名称快照
     */
    @TableField(value = "product_name")
    private String productName;

    /**
     * 
     */
    @TableField(value = "product_image")
    private String productImage;

    /**
     * 下单时单价
     */
    @TableField(value = "price")
    private BigDecimal price;

    /**
     * 
     */
    @TableField(value = "quantity")
    private Integer quantity;

    /**
     * 小计金额
     */
    @TableField(value = "subtotal")
    private BigDecimal subtotal;
}