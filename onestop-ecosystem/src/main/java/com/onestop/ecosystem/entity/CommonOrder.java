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

@ApiModel(value = "com.onestop.ecosystem.model.CommonOrder")
@Data
@TableName(value = "public.common_order")
public class CommonOrder implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value = "null")
    private Long id;

    /**
     * 流程号
     */
    @TableField(value = "flow_id")
    @ApiModelProperty(value = "流程号")
    private String flowId;

    /**
     * 消息创建时间
     */
    @TableField(value = "message_creation_time")
    @ApiModelProperty(value = "消息创建时间")
    private LocalDateTime messageCreationTime;

    /**
     * 消息ID
     */
    @TableField(value = "message_id")
    @ApiModelProperty(value = "消息ID")
    private String messageId;

    /**
     * 发送方
     */
    @TableField(value = "originator")
    @ApiModelProperty(value = "发送方")
    private String originator;

    /**
     * 接收方
     */
    @TableField(value = "recipient")
    @ApiModelProperty(value = "接收方")
    private String recipient;

    /**
     * 卫星号
     */
    @TableField(value = "satellite_id")
    @ApiModelProperty(value = "卫星号")
    private String satelliteId;

    /**
     * 订单号
     */
    @TableField(value = "order_id")
    @ApiModelProperty(value = "订单号")
    private Long orderId;

    /**
     * 消息类型
     */
    @TableField(value = "message_type")
    @ApiModelProperty(value = "消息类型")
    private String messageType;

    /**
     * 文件路径
     */
    @TableField(value = "xml_path")
    @ApiModelProperty(value = "文件路径")
    private String xmlPath;

    private static final long serialVersionUID = 1L;

    public static final String COL_FLOW_ID = "flow_id";

    public static final String COL_MESSAGE_CREATION_TIME = "message_creation_time";

    public static final String COL_MESSAGE_ID = "message_id";

    public static final String COL_ORIGINATOR = "originator";

    public static final String COL_RECIPIENT = "recipient";

    public static final String COL_SATELLITE_ID = "satellite_id";

    public static final String COL_ORDER_ID = "order_id";

    public static final String COL_MESSAGE_TYPE = "message_type";

    public static final String COL_XML_PATH = "xml_path";
}