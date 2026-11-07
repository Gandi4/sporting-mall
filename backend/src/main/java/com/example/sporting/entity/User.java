package com.example.sporting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

@TableName(value ="user")
@Data
public class User {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String username;
    private String password;
    private String email;
    private String phone;
    private String avatar;
    private Integer status;

    @TableField("created_at")
    private Date createdAt;

    @TableField("updated_at")
    private Date updatedAt;

    @TableField("is_vip")
    private Integer isVip;

    @TableField("vip_expire_time")
    private Date vipExpireTime;

    @TableField("experience")
    private Long experience;

    @TableField("current_level")
    private String currentLevel;

    @TableField("login_days")
    private Integer loginDays;

    @TableField("last_login_date")
    private Date lastLoginDate;
}