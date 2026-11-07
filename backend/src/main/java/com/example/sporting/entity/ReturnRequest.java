package com.example.sporting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 售后申请表
 * @TableName return_request
 */
@TableName(value ="return_request")
@Data
public class ReturnRequest {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关键修复：补全用户ID字段映射
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 订单ID
     */
    @TableField(value = "order_id")
    private Long orderId;

    /**
     * 订单项ID
     */
    @TableField(value = "order_item_id")
    private Long orderItemId;

    /**
     * 商品ID
     */
    @TableField(value = "product_id")
    private Long productId;

    /**
     * 商品名称
     */
    @TableField(value = "product_name")
    private String productName;

    /**
     * 商品图片
     */
    @TableField(value = "product_image")
    private String productImage;

    /**
     * 退货原因
     */
    @TableField(value = "reason")
    private String reason;

    /**
     * 类型：0退款 1换货 2退货
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 状态：0待审核 1通过 2拒绝 3完成
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 申请时间
     */
    @TableField(value = "apply_at")
    private Date applyAt;

    /**
     * 处理时间
     */
    @TableField(value = "processed_at")
    private Date processedAt;
}