package com.onestop.ecosystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;
import org.dozer.Mapping;

@ApiModel(value = "com.onestop.ecosystem.entity.PreprocessOrder")
@Data
@TableName(value = "public.preprocess_order")
public class PreprocessOrder implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value = "null")
    @Mapping
    private Long id;

    /**
     * 订单Id，格式为OrderID_index
     */
    @TableField(value = "task_id")
    @ApiModelProperty(value = "订单Id，格式为OrderID_index")
    @Mapping
    private String taskId;

    /**
     * 订单中的ID
     */
    @TableField(value = "order_id")
    @ApiModelProperty(value = "订单中的ID")
    @Mapping
    private String orderId;

    /**
     * 原始订单id
     */
    @TableField(value = "prodorder_id")
    @ApiModelProperty(value = "原始订单id")
    private Long prodorderId;

    /**
     * 数据地址
     */
    @TableField(value = "data_path")
    @ApiModelProperty(value = "数据地址")
    @Mapping
    private String dataPath;

    /**
     * 数据名称
     */
    @TableField(value = "data_name")
    @ApiModelProperty(value = "数据名称")
    @Mapping
    private String dataName;

    /**
     * 数据ID
     */
    @TableField(value = "product_id")
    @ApiModelProperty(value = "数据ID")
    @Mapping
    private String productId;

    /**
     * 景序列号
     */
    @TableField(value = "scene_id")
    @ApiModelProperty(value = "景序列号")
    private String sceneId;

    /**
     * 专题产品类型
     */
    @TableField(value = "thematic_type")
    @ApiModelProperty(value = "专题产品类型")
    @Mapping
    private String thematicType;

    /**
     * 状态
     */
    @TableField(value = "status")
    @ApiModelProperty(value = "状态")
    @Mapping
    private Integer status;

    /**
     * 操作员名称
     */
    @TableField(value = "operator_name")
    @ApiModelProperty(value = "操作员名称")
    private String operatorName;

    /**
     * 传感器标识
     */
    @TableField(value = "sensor_id")
    @ApiModelProperty(value = "传感器标识")
    private String sensorId;

    /**
     * 卫星标识
     */
    @TableField(value = "satellite_id")
    @ApiModelProperty(value = "卫星标识")
    @Mapping
    private String satelliteId;

    /**
     * 成像圈号
     */
    @TableField(value = "orbit_id")
    @ApiModelProperty(value = "成像圈号")
    private String orbitId;

    @TableField(value = "scene_path")
    @ApiModelProperty(value = "null")
    private Integer scenePath;

    @TableField(value = "scene_row")
    @ApiModelProperty(value = "null")
    private Integer sceneRow;

    /**
     * 产品类型
     */
    @TableField(value = "product_type")
    @ApiModelProperty(value = "产品类型")
    private String productType;

    /**
     * 创建时间
     */
    @TableField(value = "creation_time")
    @ApiModelProperty(value = "创建时间")
    @Mapping
    private LocalDateTime creationTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 失败原因
     */
    @Mapping
    @TableField(value = "reason")
    @ApiModelProperty(value = "失败原因")
    private String reason;

    /**
     * 失败原因
     */
    @TableField(value = "result_path")
    @ApiModelProperty(value = "结果路径")
    private String resultPath;

    private static final long serialVersionUID = 1L;

    public static final String COL_TASK_ID = "task_id";

    public static final String COL_ORDER_ID = "order_id";

    public static final String COL_PRODORDER_ID = "prodorder_id";

    public static final String COL_DATA_PATH = "data_path";

    public static final String COL_DATA_NAME = "data_name";

    public static final String COL_PRODUCT_ID = "product_id";

    public static final String COL_SCENE_ID = "scene_id";

    public static final String COL_THEMATIC_TYPE = "thematic_type";

    public static final String COL_STATUS = "status";

    public static final String COL_OPERATOR_NAME = "operator_name";

    public static final String COL_SENSOR_ID = "sensor_id";

    public static final String COL_SATELLITE_ID = "satellite_id";

    public static final String COL_ORBIT_ID = "orbit_id";

    public static final String COL_SCENE_PATH = "scene_path";

    public static final String COL_SCENE_ROW = "scene_row";

    public static final String COL_PRODUCT_TYPE = "product_type";

    public static final String COL_CREATION_TIME = "creation_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_REASON = "reason";

    public static final String COL_RESULT_PATH= "result_path";
}