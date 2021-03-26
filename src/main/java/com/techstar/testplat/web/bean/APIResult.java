package com.techstar.testplat.web.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.techstar.testplat.web.utils.StateEnum;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 服务实体，统一返回格式
 */
@ApiModel(value = "统一响应对象")
public class APIResult<T> implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/** 状态码，成功值200，失败请查阅错误编码表 */
	@ApiModelProperty(value = "响应状态", example = "可选值参照枚举类：com.lc.ibps.api.base.constants.StateEnum")
	private int state = StateEnum.SUCCESS.getCode();
	/** 请求路径 */
	@ApiModelProperty(value = "请求地址", example = "/login")
	private String request;
	/** 响应消息 */
	@ApiModelProperty(value = "响应信息", example = "操作成功")
	private String message = "";
	/** 错误原因 */
	@ApiModelProperty(value = "异常信息", example = "操作失败，username为空")
	private String cause = "";
	/** 响应变量 */
	@ApiModelProperty(value = "返回变量-可返回非主体数据", example = "如返回数据ID")
	private Map<String, Object> variables = new HashMap<String, Object>();
	/** 响应数据 */
	@ApiModelProperty(value = "返回数据-主体数据", example = "如返回机构对象")
	private T data;

	public APIResult() {
	}

	public APIResult(int state) {
		super();
		this.state = state;
	}

	public APIResult(int state, String message) {
		this.state = state;
		this.message = message;
	}

	public APIResult(int state, String message, String cause) {
		this.state = state;
		this.message = message;
		this.cause = cause;
	}

	public static <T> APIResult<T> success(T data) {
		APIResult<T> result = new APIResult<T>();
		result.setData(data);
		return result;
	}

	public static APIResult<Void> success() {
		return new APIResult<>();
	}

	/**
	 * 是否成功
	 *
	 * @return
	 */
	@JsonIgnore
	public boolean isSuccess() {
		return StateEnum.SUCCESS.getCode() == state;
	}

	/**
	 * 是否成功
	 *
	 * @return
	 */
	@JsonIgnore
	public boolean isFailed() {
		return StateEnum.SUCCESS.getCode() != state;
	}

	/**
	 * 添加响应变量
	 *
	 * @param key
	 *            变量key
	 * @param value
	 *            变量值
	 */
	public void addVariable(String key, Object value) {
		this.variables.put(key, value);
	}

	/**
	 * 获取变量key的值
	 *
	 * @param key
	 *            变量key
	 * @return
	 */
	public Object getVariable(String key) {
		if (null == variables) { return null; }
		return variables.get(key);
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public Map<String, Object> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, Object> vars) {
		this.variables = vars;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String toJsonString() {
		return JSON.toJSONString(this);
	}

	public String toString() {
		return toJsonString();
	}

}
