package com.techstar.testplat.web.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.techstar.testplat.web.utils.BeanUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * API请求
 */
@ApiModel(value = "动态查询请求对象")
public class APIRequest implements Serializable {

	/**
	 * serialVersionUID:
	 */
	private static final long serialVersionUID = -2647543499875978629L;

	/**
	 * parameters:请求参数列表
	 */
	@ApiModelProperty(required = false, value = "查询参数")
	private List<APIRequestParameter> parameters;
	/**
	 * sorts:请求排序
	 */
	@ApiModelProperty(required = false, value = "排序参数")
	private List<APIRequestSort> sorts;
	/**
	 * requestPage:请求分页
	 */
	@ApiModelProperty(required = false, value = "分页参数")
	private APIRequestPage requestPage;

	public APIRequest() {
	}

	public APIRequest(int pageNo, int limit) {
		this.requestPage = new APIRequestPage(pageNo, limit);
	}

	public String getParameter(String key) {
		if (BeanUtils.isNotEmpty(parameters)) {
			for (APIRequestParameter parameter : parameters) {
				if (null != parameter.getKey() && parameter.getKey().equals(key)) { return parameter.getValue(); }
			}
		}

		return null;
	}

	public List<APIRequestParameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<APIRequestParameter> parameters) {
		this.parameters = parameters;
	}

	public void addParameters(String key, String value) {
		addParameters(new APIRequestParameter(key, value));
	}

	public void addParameters(APIRequestParameter parameter) {
		if (BeanUtils.isEmpty(this.parameters)) {
			this.parameters = new ArrayList<APIRequestParameter>();
			this.parameters.add(parameter);
		} else {
			// 覆盖原参数
			Iterator<APIRequestParameter> it = this.parameters.iterator();
			APIRequestParameter requestParameter = null;
			for (; it.hasNext();) {
				requestParameter = it.next();
				if (requestParameter.getKey().equalsIgnoreCase(parameter.getKey())) {
					it.remove();
				}
			}
			this.parameters.add(parameter);
		}
	}

	public List<APIRequestSort> getSorts() {
		return sorts;
	}

	public void setSorts(List<APIRequestSort> sorts) {
		this.sorts = sorts;
	}

	public void addSorts(String field, String order) {
		addSorts(new APIRequestSort(field, order));
	}

	public void addSorts(APIRequestSort sort) {
		if (BeanUtils.isEmpty(this.sorts)) {
			this.sorts = new ArrayList<APIRequestSort>();
			this.sorts.add(sort);
		} else {
			// 覆盖原参数
			Iterator<APIRequestSort> it = this.sorts.iterator();
			APIRequestSort requestSort = null;
			for (; it.hasNext();) {
				requestSort = it.next();
				if (requestSort.getField().equalsIgnoreCase(sort.getField())) {
					it.remove();
				}
			}
			this.sorts.add(sort);
		}
	}

	public APIRequestPage getRequestPage() {
		return requestPage;
	}

	public void setRequestPage(APIRequestPage requestPage) {
		this.requestPage = requestPage;
	}

	public void setRequestPage(int pageNo, int limit) {
		this.requestPage = new APIRequestPage(pageNo, limit);
	}

}
