package com.techstar.testplat.web.utils;

import com.fasterxml.jackson.annotation.JsonFormat;

/** 
 * 编码标识
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum StateEnum /*implements BaseEnum*/ {

	WARN(103, "存在不正常的数据！")
	,SUCCESS(200, "请求成功")
	
	,BAD_REQUEST(400, "请求不完整")
	,NO_PERMISSION(401, "未授权")
	,REQUEST_404(404, "请求找不到")
	
	,ERROR(500, "请求失败")
	,NOT_IMPLEMENTED_REQUEST(501, "请求未实现或未开启")
	,HYSTRIX_FALLBACK(502, "Fallback异常")
	
	, UNAVAILABLE_GATEWAY(50001, "网关超时")
	, UNAVAILABLE_SERVICE(50002, "服务不可用")
	
	, CLIENT_INVOKE_EXCEPTION(50003, "客户端调用异常")
	, CONNECT_ERROR(50004, "网络连接错误")
	, SOCKET_READ_TIMEOUT(50005, "网络超时")
	, HYSTRIX_TIMEOUT(50006, "Hystrix断路器超时")
	, HYSTRIX_THREAD_POOL_FULL(50007, "Hystrix线程池已满")
	, HYSTRIX_CIRCUITBREAKER_OPEN(50008, "Hystrix断路器已打开")
	, HYSTRIX_SEMAPHORE_NOT_ACQUIRED(50009, "Hystrix信号量未获取")
	
	, ILLEGAL_REQUEST(6010101, "非法请求")
	, ILLEGAL_IP(6010102, "IP被禁止")
	, ILLEGAL_PERMISSION(6010103, "未授权")
	, ILLEGAL_PERMISSION_LIMIT(6010104, "接口限流")
	
	, ILLEGAL_CLIENT_ID_SECRET(6020101, "授权ID或密钥错误")
	, ILLEGAL_AUTHORIZE_CODE(6020102, "授权码错误")
	, ILLEGAL_LOGIN_STATE(6020103, "未登录")
	, ILLEGAL_LOGIN(6020104, "登录异常")
	, ILLEGAL_VALID_CODE(6020105, "验证码错误")
	, ILLEGAL_ACCOUNT_PASSWORD(6020106, "用户名或密码错误")
	, ILLEGAL_ACCOUNT_INACTIVE(6020107, "用户未激活")
	, ILLEGAL_ACCOUNT_EXPIRED(6020108, "用户过期")
	, ILLEGAL_ACCOUNT_EXPIRED_CREDENTIALS(6020109, "密码过期")
	, ILLEGAL_ACCOUNT_DISABLED(6020110, "用户被禁用")
	, ILLEGAL_ACCOUNT_LOCKED(6020111, "用户被锁定")
	, ILLEGAL_ACCOUNT_UNKOWN(6020112, "未知账号")
	, ILLEGAL_ORG_UNKOWN(6020113, "未知部门")
	, ILLEGAL_POSITION_UNKOWN(6020114, "未知岗位")
	, ILLEGAL_ACCOUNT_PASSWORD_REQUEST_VALIDCODE(6020115, "用户名或密码错误多次需要输入验证码")
	
	, ILLEGAL_TOKEN(6020201, "非法token")
	, ILLEGAL_GRANT_TYPE(6020202, "授权类型不支持")
	
	, EXPIRED_TOKEN(6020301, "token过期")
	
	,ERROR_NEWS(6030201,"公告异常")
	
	,ERROR_CAT_DICTIONARY(6030501,"数据字典异常")
	,ERROR_CAT_TYPE(6030502,"分类异常")
	,ERROR_CAT_CATEGORY(6030503,"分类标识异常")
	,ERROR_CAT_AREA(6030504,"地理区域异常")
	
	, ERROR_SYSTEM_SECURITY(6030601,"安全设置异常")
	, ERROR_SYSTEM_RESOURCES(6030602,"系统资源异常")
	, ERROR_SYSTEM_SUBSYSTEM(6030603,"子系统异常")
	, ERROR_SYSTEM_RIGHTSCONFIG(6030604,"权限配置异常")
	, ERROR_SYSTEM_RIGHTSDEF(6030605,"权限定义异常")
	, ERROR_SYSTEM_DESKTOP(6030606,"桌面管理异常")
	, ERROR_SYSTEM_AUTH(6030607,"授权管理异常")
	, ERROR_SYSTEM_APPRES(6030608,"app系统资源异常")
	
	, ERROR_COMMON_SCRIPT(6030701,"常用脚本异常")
	, ERROR_CONDITION_SCRIPT(6030702,"条件脚本异常")
	, ERROR_SCRIPT_INFO(6030703,"脚本管理异常")
	
	, ERROR_POSTPOSITION_EVENT(6030801,"服务后置事件异常")
	, ERROR_SERVICE(6030802,"服务管理异常")
	
	, ERROR_ENTITY(6040001, "参与者异常")
	, ERROR_LEVEL(6040101, "参与者等级异常")
	, ERROR_ATTR(6040201, "参与者属性异常")
	, ERROR_ROLE(6040301, "角色异常")
	, ERROR_ORG(6040401, "机构异常")
	, ERROR_POSITION(6040501, "岗位异常")
	, ERROR_EMPLOYEE(6040601, "员工异常")
	, ERROR_ORGAUTH(6040701, "分级机构异常")
	, ERROR_GROUP(6040801, "用户组异常")
	, ERROR_TENANT(6040901, "分支机构异常")
	, ERROR_RELATION(6041001, "参与者关系异常")
	, ERROR_PERSION_HOME(6041101, "个性首页异常")
	
	, ERROR_BPMN(6050000, "流程异常")
	, ERROR_BPMN_DEFINITION(6050100, "流程定义异常")
	, ERROR_BPMN_INSTANCE(6050200, "流程实例异常")
	, ERROR_BPMN_TASK(6050300, "流程任务异常")
	, ERROR_BPMN_MONITOR(6050400, "流程监控异常")
	, ERROR_BPMN_STATISTICS(6050500, "流程统计异常")
	
	, ERROR_FORM(6060000, "表单异常")
	, ERROR_FORM_DATASET(6060100, "数据集异常")
	, ERROR_FORM_DESIGN(6060200, "表单设计器异常")
	, ERROR_FORM_RENDER(6060300, "表单渲染异常")
	, ERROR_FORM_DATATPL(6060400, "数据模版异常")
	, ERROR_FORM_BIZ_DATATPL(6060500, "业务数据模版异常")
	, ERROR_FORM_BO(6060600, "业务对象异常")
	
	, ERROR_MAIL_CONFIG(6070201, "外部邮件用户设置异常")
	, ERROR_OUT_MAIL(6070202, "外部邮件异常")
	, ERROR_INNER_MESSAGE(6070203, "内部消息异常")
	, ERROR_INNER_MESSAGE_READ(6070204, "内部消息已读异常")
	, ERROR_INNER_MESSAGE_REPLY(6070205, "内部消息回复异常")
	, ERROR_MESSAGE_READ(6070206, "消息已读异常")
	, ERROR_MESSAGE_RECEIVER(6070207, "接收人异常")
	, ERROR_MESSAGE_REPLY(6070208, "消息回复异常")
	, ERROR_MESSAGE_TEMPLATE(6070209, "消息回复异常")
	, ERROR_MESSAGE(6070210, "消息类型异常")
	
	, ERROR_TIMER(6070401, "定时任务异常")
	, ERROR_TIMER_EXIST(6070402, "定时任务已存在")
	
	, ERROR_IDENTITIFIER(6070601, "流水号异常")
	, ERROR_IDENTITIFIER_EXIST(6070602, "流水号别名已存在")
	, ERROR_IDENTITIFIER_TEST(6070603, "流水号测试异常")
	
	, ERROR_ATTACHMENT(6070301, "附件操作失败")
	
	, ERROR_CODEGEN(6080000, "代码生成器异常")
	, ERROR_CODEGEN_SCHEME(6080001, "代码生成器生成方案异常")
	, ERROR_CODEGEN_TABLE_CONFIG(6080002, "代码生成器表配置异常")
	, ERROR_CODEGEN_TEMPLATE(6080003, "代码生成器模板异常")
	, ERROR_CODEGEN_VARIABLE(6080004, "代码生成器变量异常")
	
	, ERROR_SOCKET(6090001, "SOCKET文件消息异常")
	;
	
	private int code;
	private String text;
	
	/*@Override
	public String getIdentity() {
		return this.name();
	}*/
	
	private StateEnum(int code){
		this.code = code;
		this.text = ""+code;
	}
	
	private StateEnum(int code, String text){
		this.code = code;
		this.text = text;
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}