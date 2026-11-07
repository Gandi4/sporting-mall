package com.example.sporting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 商品表
 * @TableName product
 */
@TableName(value ="product")
@Data
public class Product {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    @TableField(value = "category_id")
    private Long categoryId;

    /**
     * 商品名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 商品描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 售价
     */
    @TableField(value = "price")
    private BigDecimal price;

    /**
     * 原价
     */
    @TableField(value = "original_price")
    private BigDecimal originalPrice;

    /**
     * 库存数量
     */
    @TableField(value = "stock")
    private Integer stock;

    /**
     * 销量
     */
    @TableField(value = "sales_count")
    private Integer salesCount;

    /**
     * 主图URL
     */
    @TableField(value = "main_image")
    private String mainImage;

    /**
     * 状态：上架/下架
     */
    @TableField(value = "status")
    private Object status;

    /**
     * 
     */
    @TableField(value = "created_at")
    private Date createdAt;

    /**
     * 
     */
    @TableField(value = "updated_at")
    private Date updatedAt;
}