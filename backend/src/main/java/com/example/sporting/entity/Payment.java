package com.example.sporting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 支付记录表
 * @TableName payment
 */
@TableName(value ="payment")
@Data
public class Payment {
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
     * 支付平台流水号
     */
    @TableField(value = "payment_no")
    private String paymentNo;

    /**
     * 
     */
    @TableField(value = "payment_method")
    private Object paymentMethod;

    /**
     * 
     */
    @TableField(value = "amount")
    private BigDecimal amount;

    /**
     * 
     */
    @TableField(value = "status")
    private Object status;

    /**
     * 
     */
    @TableField(value = "paid_at")
    private Date paidAt;

    /**
     * 
     */
    @TableField(value = "created_at")
    private Date createdAt;
}