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

@ApiModel(value="com.onestop.ecosystem.entity.LandStress")
@Data
@TableName(value = "public.ei_land_stress")
public class LandStress implements Serializable {
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
     * 重度腐蚀
     */
    @TableField(value = "severe_erosion")
    @ApiModelProperty(value="重度腐蚀")
    @ExcelProperty(index = 3, converter = BigDecimalConverter.class)
    @Mapping
    private BigDecimal severeErosion;

    /**
     * 中度侵蚀
     */
    @TableField(value = "moderate_erosion")
    @ApiModelProperty(value="中度侵蚀")
    @ExcelProperty(index = 4, converter = BigDecimalConverter.class)
    @Mapping
    private BigDecimal moderateErosion;

    /**
     * 建设用地总和
     */
    @TableField(value = "total_land")
    @ApiModelProperty(value="建设用地总和")
    @ExcelProperty(index = 8, converter = BigDecimalConverter.class)
    @Mapping
    private BigDecimal totalLand;

    /**
     * 其他用地总和
     */
    @TableField(value = "other_land")
    @ApiModelProperty(value="其他用地总和")
    @ExcelProperty(index = 16, converter = BigDecimalConverter.class)
    @Mapping
    private BigDecimal otherLand;

    /**
     * 年份
     */
    @TableField(value = "year")
    @ApiModelProperty(value="年份")
    @ExcelIgnore
    private String year;

    @TableField(value = "index")
    @ApiModelProperty(value="null")
    @ExcelProperty(index = 17, converter = BigDecimalConverter.class)
    @Mapping
    private BigDecimal index;

    @TableField(value = "country_area")
    @ApiModelProperty(value = "null")
    @ExcelProperty(index = 2)
    @Mapping
    private BigDecimal countryArea;

    @ExcelIgnore
    private static final long serialVersionUID = 1L;

    @ExcelIgnore
    public static final String COL_ID = "id";

    @ExcelIgnore
    public static final String COL_CODE = "code";

    @ExcelIgnore
    public static final String COL_COUNTRY = "country";

    @ExcelIgnore
    public static final String COL_SEVERE_EROSION = "severe_erosion";

    @ExcelIgnore
    public static final String COL_MODERATE_EROSION = "moderate_erosion";

    @ExcelIgnore
    public static final String COL_TOTAL_LAND = "total_land";

    @ExcelIgnore
    public static final String COL_OTHER_LAND = "other_land";

    @ExcelIgnore
    public static final String COL_YEAR = "year";

    @ExcelIgnore
    public static final String COL_INDEX = "index";

    @ExcelIgnore
    public static final String COUNTRY_AREA = "country_area";
}