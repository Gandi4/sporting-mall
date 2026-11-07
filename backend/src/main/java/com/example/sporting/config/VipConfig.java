package com.example.sporting.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("vip_config")
public class VipConfig {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String levelName;
    private Long minExp;
    private Integer isSvip;
    private Integer dailyGain; // 每日加成
    @TableField("discount_rate") // 显式映射
    private BigDecimal discountRate;
}
