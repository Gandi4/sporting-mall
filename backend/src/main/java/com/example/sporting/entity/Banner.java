package com.example.sporting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 首页轮播图
 * @TableName banner
 */
@TableName(value ="banner")
@Data
public class Banner {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    @TableField(value = "title")
    private String title;

    /**
     * 
     */
    @TableField(value = "image_url")
    private String imageUrl;

    /**
     * 点击跳转链接
     */
    @TableField(value = "link_url")
    private String linkUrl;

    /**
     * 
     */
    @TableField(value = "sort_order")
    private Integer sortOrder;

    /**
     * 1显示，0隐藏
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 
     */
    @TableField(value = "created_at")
    private Date createdAt;
}