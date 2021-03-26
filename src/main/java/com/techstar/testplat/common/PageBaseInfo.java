package com.techstar.testplat.common;

import java.util.List;

public class PageBaseInfo {
	//需要显示的数据集  每页显示的数据量
	private Object content;
	//当前页
	private long current;
	//总页数
	private long total;
	//总记录数
	private long records;
	//页大小
	private long size;
	
	//默认页面容量为20
	public final static long NO_SIZE = 20;
 
	
	public PageBaseInfo() {
		this.size= NO_SIZE;
	}
	
	public PageBaseInfo(Object content, long current, long records, long size) {
		this.content = content;//数据集合
		this.current = current;//当前页
		this.records = records;//总记录数
		this.size = size;//页面容量
		this.total = (records + this.size - 1)/this.size;//总页数
	}
 
	public long getSize() {
		return size;
	}
 
	public void setSize(long size) {
		this.size = size;
	}
 
	
	public Object getContent() {
		return content;
	}
 
	public void setRows(Object content) {
		this.content = content;
	}
 
	public long getCurrent() {
		return current;
	}
 
	public void setCurrent(long current) {
		this.current = current;
	}
 
	public long getTotal() {
		return total;
	}
 
	public void setTotal(long total) {
		this.total = total;
	}
 
	public long getRecords() {
		return records;
	}
 
	public void setRecords(long records) {
		this.records = records;
		//设置总记录数的时候同时设置总页数
		setTotal((records + this.size - 1)/this.size);
	}
}
