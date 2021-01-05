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

@ApiModel(value = "com.onestop.ecosystem.entity.ProdOrder")
@Data
@TableName(value = "public.prod_order")
public class ProdOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value = "null")
    @Mapping
    private Long id;

    @TableField(value = "common_id")
    @ApiModelProperty(value = "null")
    private Long commonId;

    /**
     * 固定长16位，包括8位年月日信息、2位流程编码和6位流水线号
     */
    @TableField(value = "order_id")
    @ApiModelProperty(value = "固定长16位，包括8位年月日信息、2位流程编码和6位流水线号")
    @Mapping
    private String orderId;

    /**
     * PSS系统的产品定制单审批的管理员，如果不需审批填写AUTOMATION
     */
    @TableField(value = "operator_name")
    @ApiModelProperty(value = "PSS系统的产品定制单审批的管理员，如果不需审批填写AUTOMATION")
    private String operatorName;

    /**
     * 1～5，1最低，5最高
     */
    @TableField(value = "user_type")
    @ApiModelProperty(value = "1～5，1最低，5最高")
    private Integer userType;

    /**
     * 一般：0；紧急：1。
     */
    @TableField(value = "urgency_level")
    @ApiModelProperty(value = "一般：0；紧急：1。")
    @Mapping
    private Integer urgencyLevel;

    /**
     * 提交产品订购单人员
     */
    @TableField(value = "user_name")
    @ApiModelProperty(value = "提交产品订购单人员")
    @Mapping
    private String userName;

    /**
     * 客户联系方式
     */
    @TableField(value = "tel_mobile")
    @ApiModelProperty(value = "客户联系方式")
    private String telMobile;

    /**
     * 查询起始时间
     */
    @TableField(value = "start_date")
    @ApiModelProperty(value = "查询起始时间")
    private LocalDateTime startDate;

    /**
     * 查询截止时间
     */
    @TableField(value = "end_date")
    @ApiModelProperty(value = "查询截止时间")
    private LocalDateTime endDate;

    /**
     * 订单备注
     */
    @TableField(value = "mark")
    @ApiModelProperty(value = "订单备注")
    @Mapping
    private String mark;

    /**
     * 卫星
     */
    @TableField(value = "satellite_id")
    @ApiModelProperty(value = "卫星")
    @Mapping
    private String satelliteId;

    /**
     * 产品级别
     */
    @TableField(value = "product_level")
    @ApiModelProperty(value = "产品级别")
    private String productLevel;

    /**
     * 专题产品类型
     */
    @TableField(value = "thematic_type")
    @ApiModelProperty(value = "专题产品类型")
    @Mapping
    private String thematicType;

    /**
     * 比例尺
     */
    @TableField(value = "scale")
    @ApiModelProperty(value = "比例尺")
    private String scale;

    /**
     * 地球模型，取值范围：WGS_84（默认）、GRS_1980等。
     */
    @TableField(value = "earth_model")
    @ApiModelProperty(value = "地球模型，取值范围：WGS_84（默认）、GRS_1980等。")
    private String earthModel;

    /**
     * 产品生产方式，以行政区域生产还是按框选范围生产，0 行政区域，行政区域最终合并成一张图，1 框选范围，产品不需要合并,2 有源数据，提供源数据景序号、数据ID
     */
    @TableField(value = "produce_style")
    @ApiModelProperty(value = "产品生产方式，以行政区域生产还是按框选范围生产，0 行政区域，行政区域最终合并成一张图，1 框选范围，产品不需要合并,2 有源数据，提供源数据景序号、数据ID")
    private Integer produceStyle;

    /**
     * 行政区域范围，行政区域范围：经度，纬度；经度，纬度；经度，纬度；经度，纬度
     */
    @TableField(value = "region")
    @ApiModelProperty(value = "行政区域范围，行政区域范围：经度，纬度；经度，纬度；经度，纬度；经度，纬度")
    private String region;

    /**
     * 地图投影，取值范围：UTM（默认）、GEO（GeoGraphic）、LCC（Lambert Conformal Conic）等。
     */
    @TableField(value = "map_projection")
    @ApiModelProperty(value = "地图投影，取值范围：UTM（默认）、GEO（GeoGraphic）、LCC（Lambert Conformal Conic）等。")
    private String mapProjection;

    /**
     * 云覆盖量，范围0---100
     */
    @TableField(value = "cloud_percent")
    @ApiModelProperty(value = "云覆盖量，范围0---100")
    private Integer cloudPercent;

    /**
     * 景序列号，景数据提取单使用，景号(定制流程中使用)。多个数据以逗号，分隔
     */
    @TableField(value = "scene_id")
    @ApiModelProperty(value = "景序列号，景数据提取单使用，景号(定制流程中使用)。多个数据以逗号，分隔")
    private String sceneId;

    /**
     * 源数据ID，数据ID，多个数据以逗号，分隔
     */
    @TableField(value = "product_id")
    @ApiModelProperty(value = "源数据ID，数据ID，多个数据以逗号，分隔")
    @Mapping
    private String productId;

    /**
     * 左上角经度，取值范围：-180~180，精度为6个小数点
     */
    @TableField(value = "data_upper_left_long")
    @ApiModelProperty(value = "左上角经度，取值范围：-180~180，精度为6个小数点")
    private Double dataUpperLeftLong;

    /**
     * 左上角纬度，取值范围：-90~90，精度为6个小数点
     */
    @TableField(value = "data_upper_left_lat")
    @ApiModelProperty(value = "左上角纬度，取值范围：-90~90，精度为6个小数点")
    private Double dataUpperLeftLat;

    /**
     * 右上角经度，取值范围：-180~180，精度为6个小数点
     */
    @TableField(value = "data_upper_right_long")
    @ApiModelProperty(value = "右上角经度，取值范围：-180~180，精度为6个小数点")
    private Double dataUpperRightLong;

    /**
     * 右上角纬度，取值范围：-90~90，精度为6个小数点
     */
    @TableField(value = "data_upper_right_lat")
    @ApiModelProperty(value = "右上角纬度，取值范围：-90~90，精度为6个小数点")
    private Double dataUpperRightLat;

    /**
     * 右下角经度，取值范围：-180~180，精度为6个小数点
     */
    @TableField(value = "data_lower_right_long")
    @ApiModelProperty(value = "右下角经度，取值范围：-180~180，精度为6个小数点")
    private Double dataLowerRightLong;

    /**
     * 右下角纬度，取值范围：-90~90，精度为6个小数点
     */
    @TableField(value = "data_lower_right_lat")
    @ApiModelProperty(value = "右下角纬度，取值范围：-90~90，精度为6个小数点")
    private Double dataLowerRightLat;

    /**
     * 左下角经度，取值范围：-180~180，精度为6个小数点
     */
    @TableField(value = "data_lower_left_long")
    @ApiModelProperty(value = "左下角经度，取值范围：-180~180，精度为6个小数点")
    private Double dataLowerLeftLong;

    /**
     * 左下角纬度，取值范围：-90~90，精度为6个小数点
     */
    @TableField(value = "data_lower_left_lat")
    @ApiModelProperty(value = "左下角纬度，取值范围：-90~90，精度为6个小数点")
    private Double dataLowerLeftLat;

    /**
     * 状态
     */
    @TableField(value = "status")
    @ApiModelProperty(value = "状态")
    @Mapping
    private Integer status;

    /**
     * 创建或更新时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty(value = "创建或更新时间")
    private LocalDateTime updateTime;

    /**
     * 是否有效，主要用于标记删除
     */
    @TableField(value = "enable")
    @ApiModelProperty(value = "是否有效，主要用于标记删除")
    private Boolean enable;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    @Mapping
    private LocalDateTime createTime;


    public static final String COL_COMMON_ID = "common_id";

    public static final String COL_ORDER_ID = "order_id";

    public static final String COL_OPERATOR_NAME = "operator_name";

    public static final String COL_USER_TYPE = "user_type";

    public static final String COL_URGENCY_LEVEL = "urgency_level";

    public static final String COL_USER_NAME = "user_name";

    public static final String COL_TEL_MOBILE = "tel_mobile";

    public static final String COL_START_DATE = "start_date";

    public static final String COL_END_DATE = "end_date";

    public static final String COL_MARK = "mark";

    public static final String COL_SATELLITE_ID = "satellite_id";

    public static final String COL_PRODUCT_LEVEL = "product_level";

    public static final String COL_THEMATIC_TYPE = "thematic_type";

    public static final String COL_SCALE = "scale";

    public static final String COL_EARTH_MODEL = "earth_model";

    public static final String COL_PRODUCE_STYLE = "produce_style";

    public static final String COL_REGION = "region";

    public static final String COL_MAP_PROJECTION = "map_projection";

    public static final String COL_CLOUD_PERCENT = "cloud_percent";

    public static final String COL_SCENE_ID = "scene_id";

    public static final String COL_PRODUCT_ID = "product_id";

    public static final String COL_DATA_UPPER_LEFT_LONG = "data_upper_left_long";

    public static final String COL_DATA_UPPER_LEFT_LAT = "data_upper_left_lat";

    public static final String COL_DATA_UPPER_RIGHT_LONG = "data_upper_right_long";

    public static final String COL_DATA_UPPER_RIGHT_LAT = "data_upper_right_lat";

    public static final String COL_DATA_LOWER_RIGHT_LONG = "data_lower_right_long";

    public static final String COL_DATA_LOWER_RIGHT_LAT = "data_lower_right_lat";

    public static final String COL_DATA_LOWER_LEFT_LONG = "data_lower_left_long";

    public static final String COL_DATA_LOWER_LEFT_LAT = "data_lower_left_lat";

    public static final String COL_STATUS = "status";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_ENABLE = "enable";

    public static final String COL_CREATE_TIME = "create_time";
}