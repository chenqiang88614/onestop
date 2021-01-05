package com.onestop.ecosystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

@ApiModel(value="com.onestop.ecosystem.entity.CountryCode")
@Data
@TableName(value = "public.country_code")
public class CountryCode implements Serializable {
    /**
     * 代码
     */
    @TableField(value = "code")
    @ApiModelProperty(value="代码")
    private String code;

    /**
     * 县区
     */
    @TableField(value = "country")
    @ApiModelProperty(value="县区")
    private String country;

    private static final long serialVersionUID = 1L;

    public static final String COL_CODE = "code";

    public static final String COL_COUNTRY = "country";
}