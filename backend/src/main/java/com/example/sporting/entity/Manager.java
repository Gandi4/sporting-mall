package com.example.sporting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 管理员表
 * @TableName manager
 */
@TableName(value ="manager")
@Data
public class Manager {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 管理员账号
     */
    @TableField(value = "manager")
    private String manager;

    /**
     * 管理员密码
     */
    @TableField(value = "password")
    private String password;
}