package com.example.sporting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName vip_order
 */
@TableName(value ="vip_order")
@Data
public class VipOrder {
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
     * 
     */
    @TableField(value = "order_no")
    private String orderNo;

    /**
     * 
     */
    @TableField(value = "amount")
    private BigDecimal amount;

    /**
     * 购买月数
     */
    @TableField(value = "months")
    private Integer months;

    /**
     * 订单状态 1:已完成 0:已取消
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 
     */
    @TableField(value = "created_at")
    private Date createdAt;

    /**
     * 
     */
    @TableField(value = "pay_time")
    private Date payTime;
}