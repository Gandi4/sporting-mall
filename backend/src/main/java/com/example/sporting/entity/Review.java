package com.example.sporting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 商品评价表
 * @TableName review
 */
@TableName(value ="review")
@Data
public class Review {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    @TableField(value = "order_item_id")
    private Long orderItemId;

    /**
     * 
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 
     */
    @TableField(value = "product_id")
    private Long productId;

    /**
     * 评分1-5
     */
    @TableField(value = "rating")
    private Integer rating;

    /**
     * 评价内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 评价图片URL数组
     */
    @TableField(value = "images")
    private Object images;

    /**
     * 是否匿名：1是，0否
     */
    @TableField(value = "is_anonymous")
    private Integer isAnonymous;

    /**
     * 
     */
    @TableField(value = "created_at")
    private Date createdAt;

    public void setStatus(Integer status) {
    }
}