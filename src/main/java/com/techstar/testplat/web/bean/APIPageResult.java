package com.techstar.testplat.web.bean;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "分页对象")
public class APIPageResult implements Serializable {

	/**
	 * serialVersionUID:
	 */
	private static final long serialVersionUID = 657406105094323352L;
	
	public APIPageResult() {
	}
	
	public APIPageResult(long page, int limit, long totalCount) {
		this.page = page;
		this.limit = limit;
		this.totalCount = totalCount;
	}
	
	/**
	 * 分页大小
	 */
	@ApiModelProperty(value = "分页大小")
	private Integer limit;
	
	/**
	 * 页数
	 */
	@ApiModelProperty(value = "页数")
	private Long page;
	
	/**
	 * 总记录数
	 */
	@ApiModelProperty(value = "总记录数")
	private Long totalCount;
	
	public Integer getLimit() {
		return limit;
	}
	
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	public Long getPage() {
		return page;
	}
	
	public void setPage(long page) {
		this.page = page;
	}
	
	public Long getTotalCount() {
		return totalCount;
	}
	
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	@ApiModelProperty(value = "总页数")
	public Long getTotalPages() {
		if (totalCount <= 0) {
			return 0L;
		}
		if (limit <= 0) {
			return 0L;
		}

		long count = totalCount / limit;
		if (totalCount % limit > 0) {
			count++;
		}
		return count;
	}
	
}
