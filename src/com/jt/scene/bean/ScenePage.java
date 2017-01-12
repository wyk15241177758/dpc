package com.jt.scene.bean;

import java.util.Date;

/**
 * 预设场景对应的页面，可以指定多个
 * 
 * @author zxb
 *
 * 2016年11月27日
 */
public class ScenePage {
	private Integer scenePageId;
	private Integer sceneWordId;
	private Date createTime;
	private Date updateTime;
	private String pageTitle;
	private String pageLink;
	public ScenePage(){
		
	}
	
	public Integer getScenePageId() {
		return scenePageId;
	}
	public void setScenePageId(Integer scenePageId) {
		this.scenePageId = scenePageId;
	}
	public Integer getSceneWordId() {
		return sceneWordId;
	}
	public void setSceneWordId(Integer sceneWordId) {
		this.sceneWordId = sceneWordId;
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
	public String getPageTitle() {
		return pageTitle;
	}
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	public String getPageLink() {
		return pageLink;
	}
	public void setPageLink(String pageLink) {
		this.pageLink = pageLink;
	}
	public ScenePage(Integer scenePageId, Integer sceneWordId, String pageTitle,
			String pageLink, Date createTime, Date updateTime) {
		super();
		this.scenePageId = scenePageId;
		this.sceneWordId = sceneWordId;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.pageTitle = pageTitle;
		this.pageLink = pageLink;
	}

	
	
}
