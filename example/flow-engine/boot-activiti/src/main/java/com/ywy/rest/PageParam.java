package com.ywy.rest;

import java.util.Map;

/**
 * 分页请求参数
 * @author ywy
 *
 */
public class PageParam {
	/**
	 * 当前页码
	 */
	private Integer page = 1;
	
	/**
	 * 每页数量
	 */
	private Integer limit = 10;

	/**
	 * 当前数据偏移量
	 */
	private Integer offset;
	
	/**
	 * 查询参数
	 */
	private String params;

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public Integer getOffset() {
		if (this.page == null || this.limit == null) {
			this.offset = 0;
		} else {
			this.offset = (this.page - 1) * this.limit;
		}
		return this.offset;
	}
}
