package com.jt.searchHis.bean;

import java.util.Date;

public class SearchHis  implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String searchContent;
	private String contentMd5;
	private int searchTimes;
	private Date createTime;
	private Date updateTime;
	
	//仅用于searchHisRtService，用于标记当前内存中的检索历史是否有修改，避免update没有修改的searchHis。
	//这个属性不存入数据库
	private boolean isChanged=false;
	
	public SearchHis(Long id, String searchContent, String contentMd5, int searchTimes, Date createTime,
			Date updateTime) {
		super();
		this.id = id;
		this.searchContent = searchContent;
		this.contentMd5 = contentMd5;
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
	public String getContentMd5() {
		return contentMd5;
	}
	public void setContentMd5(String contentMd5) {
		this.contentMd5 = contentMd5;
	}
	public boolean isChanged() {
		return isChanged;
	}
	public void setChanged(boolean isChanged) {
		this.isChanged = isChanged;
	}
	
}
