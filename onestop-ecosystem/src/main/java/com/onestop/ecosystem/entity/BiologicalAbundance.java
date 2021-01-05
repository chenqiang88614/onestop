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
import org.apache.ibatis.annotations.Mapper;
import org.dozer.Mapping;

@ApiModel(value="com.onestop.ecosystem.entity.BiologicalAbundance")
@Data
@TableName(value = "public.ei_biological_abundance")
public class BiologicalAbundance implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value="null")
    @ExcelIgnore
    private String id;

    /**
     * 县区
     */
    @TableField(value = "country")
    @ApiModelProperty(value="县区")
    @ExcelProperty(index = 1)
    @Mapping
    private String country;

    /**
     * 编码
     */
    @TableField(value = "code")
    @ApiModelProperty(value="编码")
    @ExcelProperty(index = 0)
    private String code;

    /**
     * 生境质量指数
     */
    @TableField(value = "environment_restriction")
    @ApiModelProperty(value="生境质量指数")
    @ExcelProperty(index = 2, converter = BigDecimalConverter.class)
    @Mapping
    private BigDecimal environmentRestriction;

    /**
     * 生物多样性
     */
    @TableField(value = "biodiversity")
    @ApiModelProperty(value="生物多样性")
    @ExcelProperty(index = 3, converter = BigDecimalConverter.class)
    @Mapping
    private BigDecimal biodiversity;

    /**
     * 生物丰度指数
     */
    @TableField(value = "index")
    @ApiModelProperty(value="生物丰度指数")
    @ExcelProperty(index = 4, converter = BigDecimalConverter.class)
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
    public static final String COL_COUNTRY = "country";

    @ExcelIgnore
    public static final String COL_CODE = "code";

    @ExcelIgnore
    public static final String COL_ENVIRONMENT_RESTRICTION = "environment_restriction";

    @ExcelIgnore
    public static final String COL_BIODIVERSITY = "biodiversity";

    @ExcelIgnore
    public static final String COL_INDEX = "index";

    @ExcelIgnore
    public static final String COL_YEAR = "year";
}