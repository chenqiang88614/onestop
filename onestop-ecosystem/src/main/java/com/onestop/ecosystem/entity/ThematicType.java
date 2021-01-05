package com.onestop.ecosystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

@ApiModel(value="com.onestop.ecosystem.entity.ThematicType")
@Data
@TableName(value = "public.thematic_type")
public class ThematicType implements Serializable {
     @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value="null")
    private String id;

    /**
     * 产品标识
     */
    @TableField(value = "tag")
    @ApiModelProperty(value="产品标识")
    private String tag;

    /**
     * 中文名称
     */
    @TableField(value = "nameCh")
    @ApiModelProperty(value="中文名称")
    private String namech;

    /**
     * 英文名称
     */
    @TableField(value = "nameEn")
    @ApiModelProperty(value="英文名称")
    private String nameen;

    private static final long serialVersionUID = 1L;

    public static final String COL_TAG = "tag";

    public static final String COL_NAMECH = "nameCh";

    public static final String COL_NAMEEN = "nameEn";
}