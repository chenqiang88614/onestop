package com.onestop.ecosystem.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.onestop.ecosystem.excel.BigDecimalConverter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import org.dozer.Mapping;

@ApiModel(value="com.onestop.ecosystem.entity.RiversDensity")
@Data
@TableName(value = "public.ei_rivers_density")
public class RiversDensity implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value="null")
    @ExcelIgnore
    private String id;

    @TableField(value = "code")
    @ApiModelProperty(value="null")
    @ExcelProperty(index = 0)
    private String code;

    @TableField(value = "country")
    @ApiModelProperty(value="null")
    @ExcelProperty(index = 1)
    @Mapping
    private String country;

    /**
     * 县域面积
     */
    @TableField(value = "country_area")
    @ApiModelProperty(value="县域面积")
    @ExcelProperty(index = 2, converter = BigDecimalConverter.class)
    @Mapping
    private BigDecimal countryArea;

    /**
     * 河流畅度
     */
    @TableField(value = "river_length")
    @ApiModelProperty(value="河流畅度")
    @ExcelProperty(index = 3, converter = BigDecimalConverter.class)
    @Mapping
    private BigDecimal riverLength;

    /**
     * 河流面积
     */
    @TableField(value = "river_area")
    @ApiModelProperty(value="河流面积")
    @ExcelProperty(index = 4, converter = BigDecimalConverter.class)
    @Mapping
    private BigDecimal riverArea;

    /**
     * 湖泊
     */
    @TableField(value = "lake")
    @ApiModelProperty(value="湖泊")
    @ExcelProperty(index = 5, converter = BigDecimalConverter.class)
    @Mapping
    private BigDecimal lake;

    /**
     * 水库
     */
    @TableField(value = "reservior")
    @ApiModelProperty(value="水库")
    @ExcelProperty(index = 6, converter = BigDecimalConverter.class)
    @Mapping
    private BigDecimal reservior;

    /**
     * 永久性冰川雪地
     */
    @TableField(value = "snowfield")
    @ApiModelProperty(value="永久性冰川雪地")
    @ExcelProperty(index = 7, converter = BigDecimalConverter.class)
    @Mapping
    private BigDecimal snowfield;

    /**
     * 水资源量
     */
    @TableField(value = "water_area")
    @ApiModelProperty(value="水域面积")
    @ExcelProperty(index = 8, converter = BigDecimalConverter.class)
    @Mapping
    private BigDecimal waterArea;

    /**
     * 水资源量
     */
    @TableField(value = "water_resource")
    @ApiModelProperty(value="水资源量")
    @ExcelProperty(index = 9, converter = BigDecimalConverter.class)
    @Mapping
    private BigDecimal waterResource;

    @TableField(value = "index")
    @ApiModelProperty(value="null")
    @ExcelProperty(index = 10, converter = BigDecimalConverter.class)
    @Mapping
    private BigDecimal index;

    @TableField(value = "year")
    @ApiModelProperty(value="null")
    @ExcelIgnore
    private String year;

    @ExcelIgnore
    private static final long serialVersionUID = 1L;

    @ExcelIgnore
    public static final String COL_ID = "id";

    @ExcelIgnore
    public static final String COL_CODE = "code";

    @ExcelIgnore
    public static final String COL_COUNTRY = "country";

    @ExcelIgnore
    public static final String COL_COUNTRY_AREA = "country_area";

    @ExcelIgnore
    public static final String COL_RIVER_LENGTH = "river_length";

    @ExcelIgnore
    public static final String COL_RIVER_AREA = "river_area";

    @ExcelIgnore
    public static final String COL_LAKE = "lake";

    @ExcelIgnore
    public static final String COL_RESERVIOR = "reservior";

    @ExcelIgnore
    public static final String COL_SNOWFIELD = "snowfield";

    @ExcelIgnore
    public static final String COL_WATER_AREA = "water_area";

    @ExcelIgnore
    public static final String COL_WATER_RESOURCE = "water_resource";

    @ExcelIgnore
    public static final String COL_INDEX = "index";
    @ExcelIgnore
    public static final String COL_YEAR = "year";
}