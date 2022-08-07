package com.ywy.rest;

import java.util.List;

/**
 * 分页返回结果
 * @author ywy
 *
 */
public class PageResult {
	/**
	 * 总记录数
	 */
	private long count;
	
	/**
	 * 分页数据
	 */
	private List<?> items;

	public PageResult() {
	}

	public PageResult(long count, List<?> items) {
		this.count = count;
		this.items = items;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public List<?> getItems() {
		return items;
	}

	public void setItems(List<?> items) {
		this.items = items;
	}
}
