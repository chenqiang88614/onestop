package com.onestop.ftpclient.util;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

@ApiModel(value = "com.onestop.ftpclient.util.FtpParam")
@Data
@TableName(value = "public.ftp_param")
public class FtpParam implements Serializable {
    public static final String COL_USERNAME = "username";
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value = "null")
    private Long id;

    @TableField(value = "ip")
    @ApiModelProperty(value = "null")
    private String ip;

    @TableField(value = "port")
    @ApiModelProperty(value = "null")
    private Integer port;

    @TableField(value = "user_name")
    @ApiModelProperty(value = "null")
    private String userName;

    @TableField(value = "password")
    @ApiModelProperty(value = "null")
    private String password;

    @TableField(value = "folder")
    @ApiModelProperty(value = "null")
    private String folder;

    @TableField(value = "server_name")
    @ApiModelProperty(value = "null")
    private String serverName;

    @TableField(value = "alias")
    @ApiModelProperty(value = "null")
    private String alias;

    @TableField(value = "name")
    @ApiModelProperty(value = "null")
    private String name;

    private static final long serialVersionUID = 1L;

    public static final String COL_IP = "ip";

    public static final String COL_PORT = "port";

    public static final String COL_USER_NAME = "user_name";

    public static final String COL_PASSWORD = "password";

    public static final String COL_FOLDER = "folder";

    public static final String COL_SERVER_NAME = "server_name";

    public static final String COL_ALIAS = "alias";

    public static final String COL_NAME = "name";
}