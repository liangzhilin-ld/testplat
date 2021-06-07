package com.techstar.testplat.web.bean;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "分页列表对象")
public class APIPageList<E> implements Serializable {

	/**
	 * serialVersionUID:
	 */
	private static final long serialVersionUID = -2031192118119239999L;

	@ApiModelProperty(value = "分页数据列表")
	private List<E> dataResult;
	
	@ApiModelProperty(value = "分页结果")
	private APIPageResult pageResult;
	
	public List<E> getDataResult() {
		return dataResult;
	}
	
	public void setDataResult(List<E> dataResult) {
		this.dataResult = dataResult;
	}

	public APIPageResult getPageResult() {
		return pageResult;
	}

	public void setPageResult(APIPageResult pageResult) {
		this.pageResult = pageResult;
	}
	
}
