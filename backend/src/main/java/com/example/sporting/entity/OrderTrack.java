package com.example.sporting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 物流追踪表
 * @TableName order_track
 */
@TableName(value ="order_track")
@Data
public class OrderTrack {
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
     * 物流公司
     */
    @TableField(value = "logistics_company")
    private String logisticsCompany;

    /**
     * 运单号
     */
    @TableField(value = "tracking_no")
    private String trackingNo;

    /**
     * 当前状态描述
     */
    @TableField(value = "status")
    private String status;

    /**
     * 
     */
    @TableField(value = "updated_at")
    private Date updatedAt;
}