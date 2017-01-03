package com.jt.searchHis.bean;

import java.util.Date;

public class SearchHis {
	private Long id;
	private String searchContent;
	private int searchTimes;
	private Date createTime;
	private Date updateTime;
	
	public SearchHis(Long id, String searchContent, int searchTimes, Date createTime, Date updateTime) {
		super();
		this.id = id;
		this.searchContent = searchContent;
		this.searchTimes = searchTimes;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}
	public SearchHis(){
		
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSearchContent() {
		return searchContent;
	}
	public void setSearchContent(String searchContent) {
		this.searchContent = searchContent;
	}
	public int getSearchTimes() {
		return searchTimes;
	}
	public void setSearchTimes(int searchTimes) {
		this.searchTimes = searchTimes;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
}
