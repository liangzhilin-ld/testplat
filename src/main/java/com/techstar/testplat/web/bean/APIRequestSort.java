package com.techstar.testplat.web.bean;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/** 
 * API请求排序
 */
@ApiModel(value = "排序参数")
public class APIRequestSort implements Serializable {

	/**
	 * serialVersionUID:
	 */
	private static final long serialVersionUID = -6217331156187087489L;
	
	public APIRequestSort() {
		super();
	}
	public APIRequestSort(String field, String order) {
		super();
		this.field = field;
		this.order = order;
	}
	/**
	 * field:字段名
	 */
	@ApiModelProperty(required = true , value = "字段名")
	private String field;
	/**
	 * {@link com.techstar.framework.party.provider.api.base.query.Direction}
	 * order:排序方向
	 */
	@ApiModelProperty(required = true , value = "排序方向")
	private String order;
	
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	
}
