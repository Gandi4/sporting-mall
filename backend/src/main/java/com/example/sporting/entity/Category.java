package com.example.sporting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.List;

/**
 * 商品分类表
 * @TableName category
 */
@TableName(value ="category")
@Data
public class Category {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 分类名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 父分类ID
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 排序
     */
    @TableField(value = "sort_order")
    private Integer sortOrder;

    /**
     * 状态：1启用，0禁用
     */
    @TableField(value = "status")
    private Integer status;

    // 这个字段只用于返回树形结构，不映射到数据库
    @TableField(exist = false)  // 重要：告诉 MyBatis Plus 这个字段不在表里
    private List<Category> children;

    // getter 和 setter
    public List<Category> getChildren() {
        return children;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }
}