package com.example.sporting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 
 * @TableName vip_config
 */
@TableName(value ="vip_config")
@Data
public class VipConfig {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 境界名称
     */
    @TableField(value = "level_name")
    private String levelName;

    /**
     * 晋升所需最小经验
     */
    @TableField(value = "min_exp")
    private Long minExp;

    /**
     * 是否属于天宫神境(SVIP)
     */
    @TableField(value = "is_svip")
    private Integer isSvip;

    /**
     * 该境界享有的折扣
     */
    @TableField(value = "discount_rate")
    private BigDecimal discountRate;
}