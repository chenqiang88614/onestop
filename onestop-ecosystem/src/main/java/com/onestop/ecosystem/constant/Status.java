package com.onestop.ecosystem.constant;

public enum Status {
	
	
	SUCCESS("操作成功","0"),
	FAILURE("操作失败","1"),
	SERVER_ERROR("服务端出错啦","666"),
	FAILTURE_param_null("参数为空","1"),
	SOURCE_NAME_NOT_EXIST("权限不存在","1"),
	ROLE_NAME_EXIST("角色名存在","1"),
	ROLE_NAME_NOT_EXIST("角色名不存在","1"),
	ROLE_NAME_NULL("角色名为空","1"),
	USER_NAME_EXIST("用户名存在","1"),
	USER_NAME_NOT_EXIST("用户名不存在","1"),
	VERIFICATION_CODE_ERROR("验证码错误","1"),
	USER_PASSWD_ERROR("用户密码错误","3"),
	TIME_FORMAT_ERROR("时间格式错误","1"),
	QUERY_NOT_FOUND("查询不到数据","1"),
	
	CARNO_NULL("车牌号已存在","1"),
	CARNUM_NULL("车辆机构编码已存在","1"),
	
	QUERY_ID_NOT_FOUND("对应ID不存在","1"),
	
	REQUIRE_FIELD_NULL("必填字段为空","1"),
	UNKOWN_DATASOURCE("未知数据源","1"),
	DATA_IMPORT_FAIL("数据导入失败","1"),
	FILE_UPLOAD_FAIL("文件上传失败","1"),
	CATALOG_ROOT("根节点不允许删除！","1"),
	PARENTID_NOT_EXIST("父亲节点不存在","1"),
	CATALOGID_NOT_EXIST("对应目录节点不存在，","1"),
	CATALOGID_Has_Children("对应目录节点存在子节点，不允许删除！","1"),
	SOURCE_NOT_EXIST("对应元数据不存在！","1"),
	LAYER_NAME_EXIST("图层名称已存在","1"),
	SERVICE_NOT_EXIST("服务不存在！","1");
	
	private String str;
	private String value;
	
	private Status(String str,String value) {
		this.str = str;
		this.value = value;
	}
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
