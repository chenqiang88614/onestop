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

@ApiModel(value = "com.onestop.ecosystem.entity.EnviromentRestrication")
@Data
@TableName(value = "public.ei_enviroment_restrication")
public class EnviromentRestrication implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value = "null")
    @ExcelIgnore
    private String id;

    @TableField(value = "code")
    @ApiModelProperty(value = "null")
    @ExcelProperty(index = 0)
    private String code;

    @TableField(value = "country")
    @ApiModelProperty(value = "null")
    @ExcelProperty(index = 1)
    @Mapping
    private String country;

    /**
     * 林地
     */
    @TableField(value = "forest")
    @ApiModelProperty(value = "林地")
    @ExcelProperty(index = 30, converter = BigDecimalConverter.class)
    @Mapping
    private BigDecimal forest;

    /**
     * 草地
     */
    @TableField(value = "grassland")
    @ApiModelProperty(value = "草地")
    @ExcelProperty(index = 31, converter = BigDecimalConverter.class)
    @Mapping
    private BigDecimal grassland;

    /**
     * 水域湿地
     */
    @TableField(value = "wetland")
    @ApiModelProperty(value = "水域湿地")
    @ExcelProperty(index = 32, converter = BigDecimalConverter.class)
    @Mapping
    private BigDecimal wetland;

    /**
     * 耕地
     */
    @TableField(value = "plowland")
    @ApiModelProperty(value = "耕地")
    @ExcelProperty(index = 33, converter = BigDecimalConverter.class)
    @Mapping
    private BigDecimal plowland;

    /**
     * 建设用地
     */
    @TableField(value = "construction_land")
    @ApiModelProperty(value = "建设用地")
    @ExcelProperty(index = 34, converter = BigDecimalConverter.class)
    @Mapping
    private BigDecimal constructionLand;

    /**
     * 未利用地
     */
    @TableField(value = "unused_land")
    @ApiModelProperty(value = "未利用地")
    @ExcelProperty(index = 35, converter = BigDecimalConverter.class)
    @Mapping
    private BigDecimal unusedLand;

    @TableField(value = "index")
    @ApiModelProperty(value = "null")
    @ExcelProperty(index = 36, converter = BigDecimalConverter.class)
    @Mapping
    private BigDecimal index;

    @TableField(value = "year")
    @ApiModelProperty(value = "null")
    @ExcelIgnore
    private String year;

    /**
     * 县域面积
     */
    @TableField(value = "country_area")
    @ApiModelProperty(value = "县域面积")
    @ExcelProperty(index = 2, converter = BigDecimalConverter.class)
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
    public static final String COL_FOREST = "forest";

    @ExcelIgnore
    public static final String COL_GRASSLAND = "grassland";

    @ExcelIgnore
    public static final String COL_WETLAND = "wetland";

    @ExcelIgnore
    public static final String COL_PLOWLAND = "plowland";

    @ExcelIgnore
    public static final String COL_CONSTRUCTION_LAND = "construction_land";

    @ExcelIgnore
    public static final String COL_UNUSED_LAND = "unused_land";

    @ExcelIgnore
    public static final String COL_INDEX = "index";

    @ExcelIgnore
    public static final String COL_YEAR = "year";

    @ExcelIgnore
    public static final String COL_COUNTRY_AREA = "country_area";
}