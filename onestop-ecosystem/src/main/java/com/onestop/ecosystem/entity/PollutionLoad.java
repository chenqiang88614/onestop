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

@ApiModel(value="com.onestop.ecosystem.entity.PollutionLoad")
@Data
@TableName(value = "public.ei_pollution_load")
public class PollutionLoad implements Serializable {
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

    @TableField(value = "year")
    @ApiModelProperty(value="null")
    @ExcelIgnore
    private String year;

    /**
     * 县域面积
     */
    @TableField(value = "country_area")
    @ApiModelProperty(value="县域面积")
    @ExcelProperty(index = 2, converter = BigDecimalConverter.class)
    @Mapping
    private BigDecimal countryArea;

    /**
     * 需氧量排放
     */
    @TableField(value = "oxygen_demand")
    @ApiModelProperty(value="需氧量排放")
    @ExcelProperty(index = 3, converter = BigDecimalConverter.class)
    @Mapping
    private BigDecimal oxygenDemand;

    /**
     * 氨氮排放量
     */
    @TableField(value = "ammonia_nitrogen")
    @ApiModelProperty(value="氨氮排放量")
    @ExcelProperty(index = 4, converter = BigDecimalConverter.class)
    @Mapping
    private BigDecimal ammoniaNitrogen;

    /**
     * 二氧化硫排放量
     */
    @TableField(value = "fulfur_dioxide")
    @ApiModelProperty(value="二氧化硫排放量")
    @ExcelProperty(index = 5, converter = BigDecimalConverter.class)
    @Mapping
    private BigDecimal fulfurDioxide;

    /**
     * 氮氧化物排放量
     */
    @TableField(value = "oxynirtide")
    @ApiModelProperty(value="氮氧化物排放量")
    @ExcelProperty(index = 6, converter = BigDecimalConverter.class)
    @Mapping
    private BigDecimal oxynirtide;

    /**
     * 烟（粉尘）排放量
     */
    @TableField(value = "smoke_dust")
    @ApiModelProperty(value="烟（粉尘）排放量")
    @ExcelProperty(index = 7, converter = BigDecimalConverter.class)
    @Mapping
    private BigDecimal smokeDust;

    /**
     * 固体废物
     */
    @TableField(value = "solid_waste")
    @ApiModelProperty(value="固体废物")
    @ExcelProperty(index = 8, converter = BigDecimalConverter.class)
    @Mapping
    private BigDecimal solidWaste;

    /**
     * 区域年降水量
     */
    @TableField(value = "annual_precipitation")
    @ApiModelProperty(value="区域年降水量")
    @ExcelProperty(index = 9, converter = BigDecimalConverter.class)
    @Mapping
    private BigDecimal annualPrecipitation;

    @TableField(value = "index")
    @ApiModelProperty(value="null")
    @ExcelProperty(index = 10, converter = BigDecimalConverter.class)
    @Mapping
    private BigDecimal index;

    @ExcelIgnore
    private static final long serialVersionUID = 1L;

    @ExcelIgnore
    public static final String COL_ID = "id";

    @ExcelIgnore
    public static final String COL_CODE = "code";

    @ExcelIgnore
    public static final String COL_COUNTRY = "country";

    @ExcelIgnore
    public static final String COL_YEAR = "year";

    @ExcelIgnore
    public static final String COL_COUNTRY_AREA = "country_area";

    @ExcelIgnore
    public static final String COL_OXYGEN_DEMAND = "oxygen_demand";

    @ExcelIgnore
    public static final String COL_AMMONIA_NITROGEN = "ammonia_nitrogen";

    @ExcelIgnore
    public static final String COL_FULFUR_DIOXIDE = "fulfur_dioxide";

    @ExcelIgnore
    public static final String COL_OXYNIRTIDE = "oxynirtide";

    @ExcelIgnore
    public static final String COL_SMOKE_DUST = "smoke_dust";

    @ExcelIgnore
    public static final String COL_SOLID_WASTE = "solid_waste";

    @ExcelIgnore
    public static final String COL_ANNUAL_PRECIPITATION = "annual_precipitation";

    @ExcelIgnore
    public static final String COL_INDEX = "index";
}