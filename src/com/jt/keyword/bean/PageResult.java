package com.jt.keyword.bean;

import java.util.List;

public class PageResult {
	
	public int   pageNum;
	public int   pageSize;
	public int   totalNum;
	public int   totalPage;
	public List<QueryResut>  list;
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public List<QueryResut> getList() {
		return list;
	}
	public void setList(List<QueryResut> list) {
		this.list = list;
	}
	
	
	
	
	

}
