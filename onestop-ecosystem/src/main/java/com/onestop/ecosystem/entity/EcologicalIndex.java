package com.onestop.ecosystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import org.dozer.Mapping;

@ApiModel(value="com.onestop.ecosystem.entity.EcologicalIndex")
@Data
@TableName(value = "public.ei_ecological_index")
public class EcologicalIndex implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value="null")
    private String id;

    /**
     * 县区
     */
    @TableField(value = "country")
    @ApiModelProperty(value="县区")
    @Mapping
    private String country;

    /**
     * 县区编码
     */
    @TableField(value = "code")
    @ApiModelProperty(value="县区编码")
    private String code;

    /**
     * 年份
     */
    @TableField(value = "year")
    @ApiModelProperty(value="年份")
    private String year;

    /**
     * 生物丰度指数
     */
    @TableField(value = "biological_abundance")
    @ApiModelProperty(value="生物丰度指数")
    @Mapping
    private BigDecimal biologicalAbundance;

    /**
     * 植被覆盖指数
     */
    @TableField(value = "vegetational_cover")
    @ApiModelProperty(value="植被覆盖指数")
    @Mapping
    private BigDecimal vegetationalCover;

    /**
     * 水网密度指数
     */
    @TableField(value = "rivers_density")
    @ApiModelProperty(value="水网密度指数")
    @Mapping
    private BigDecimal riversDensity;

    /**
     * 土地胁迫指数
     */
    @TableField(value = "land_stress")
    @ApiModelProperty(value="土地胁迫指数")
    @Mapping
    private BigDecimal landStress;

    /**
     * 污染负荷指数
     */
    @TableField(value = "pollution_load")
    @ApiModelProperty(value="污染负荷指数")
    @Mapping
    private BigDecimal pollutionLoad;

    /**
     * 环境限制指数
     */
    @TableField(value = "environment_restriction")
    @ApiModelProperty(value="环境限制指数")
    @Mapping
    private BigDecimal environmentRestriction;

    /**
     * 生态状态指数
     */
    @TableField(value = "ecological_regime")
    @ApiModelProperty(value="生态状态指数")
    @Mapping
    private BigDecimal ecologicalRegime;

    /**
     * 分级
     */
    @TableField(value = "grade")
    @ApiModelProperty(value="分级")
    @Mapping
    private String grade;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_COUNTRY = "country";

    public static final String COL_CODE = "code";

    public static final String COL_YEAR = "year";

    public static final String COL_BIOLOGICAL_ABUNDANCE = "biological_abundance";

    public static final String COL_VEGETATIONAL_COVER = "vegetational_cover";

    public static final String COL_RIVERS_DENSITY = "rivers_density";

    public static final String COL_LAND_STRESS = "land_stress";

    public static final String COL_POLLUTION_LOAD = "pollution_load";

    public static final String COL_ENVIRONMENT_RESTRICTION = "environment_restriction";

    public static final String COL_ECOLOGICAL_REGIME = "ecological_regime";

    public static final String COL_GRADE = "grade";
}