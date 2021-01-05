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

@ApiModel(value="com.onestop.ecosystem.entity.VegetationalCover")
@Data
@TableName(value = "public.ei_vegetational_cover")
public class VegetationalCover implements Serializable {
    @ExcelIgnore
    private static final long serialVersionUID = 1L;

    @ExcelIgnore
    public static final String COL_ID = "id";

    @ExcelIgnore
    public static final String COL_CODE = "code";

    @ExcelIgnore
    public static final String COL_COUNTRY = "country";

    @ExcelIgnore
    public static final String COL_INDEX = "index";

    @ExcelIgnore
    public static final String COL_YEAR = "year";

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

    @TableField(value = "index")
    @ApiModelProperty(value="null")
    @ExcelProperty(index = 2, converter = BigDecimalConverter.class)
    @Mapping
    private BigDecimal index;

    @TableField(value = "year")
    @ApiModelProperty(value="null")
    @ExcelIgnore
    private String year;

}