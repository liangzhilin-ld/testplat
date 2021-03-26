package com.techstar.testplat.web.bean;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/** 
 * API请求参数
 */
@ApiModel(value = "动态查询参数")
public class APIRequestParameter implements Serializable {

	/**
	 * serialVersionUID:
	 */
	private static final long serialVersionUID = -2647543499875978629L;

	public APIRequestParameter() {
		super();
	}

	public APIRequestParameter(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}

	/**
	 * key:参数键
	 */
	@ApiModelProperty(required = true , value = "参数键")
	private String key;
	
	/**
	 * value:参数值
	 */
	@ApiModelProperty(required = true , value = "参数值")
	private String value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
