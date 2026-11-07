package com.example.sporting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@TableName(value ="`order` ") // 必须带反引号
@Data
public class Order {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "order_no")
    private String orderNo;

    @TableField(value = "user_id")
    private Long userId;

    @TableField(value = "total_amount")
    private BigDecimal totalAmount;

    @TableField(value = "discount_amount")
    private BigDecimal discountAmount;

    @TableField(value = "pay_amount")
    private BigDecimal payAmount;

    @TableField(value = "address_snapshot")
    private String addressSnapshot;

    // 修复点：Object 改为 String 适配数据库 enum
    @TableField(value = "status")
    private String status;

    @TableField(value = "payment_method")
    private String paymentMethod;

    @TableField(value = "paid_at")
    private Date paidAt;

    @TableField(value = "created_at")
    private Date createdAt;

    @TableField(value = "remark")
    private String remark;
}