package com.example.sporting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 收货地址表
 * @TableName address
 */
@TableName(value ="address")
@Data
public class Address {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 收货人姓名
     */
    @TableField(value = "receiver_name")
    private String receiverName;

    /**
     * 收货人手机号
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 
     */
    @TableField(value = "province")
    private String province;

    /**
     * 
     */
    @TableField(value = "city")
    private String city;

    /**
     * 
     */
    @TableField(value = "district")
    private String district;

    /**
     * 详细地址
     */
    @TableField(value = "detail_address")
    private String detailAddress;

    /**
     * 是否默认地址：1是，0否
     */
    @TableField(value = "is_default")
    private Integer isDefault;

    /**
     * 
     */
    @TableField(value = "created_at")
    private Date createdAt;
}