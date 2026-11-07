package com.example.sporting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 系统公告表
 * @TableName announcement
 */
@TableName(value ="announcement")
@Data
public class Announcement {
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
    @TableField(value = "content")
    private String content;

    /**
     * 
     */
    @TableField(value = "publish_at")
    private Date publishAt;

    /**
     * 1发布，0草稿
     */
    @TableField(value = "status")
    private Integer status;
}