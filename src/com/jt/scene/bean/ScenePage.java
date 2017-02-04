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
	private String pageTitle;
	private String pageLink;
	private String sjfl;
	private String html;
	private Date createTime;
	private Date updateTime;
	public ScenePage(){
		
	}
	
	public String getSjfl() {
		return sjfl;
	}

	public void setSjfl(String sjfl) {
		this.sjfl = sjfl;
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

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public ScenePage(Integer scenePageId, Integer sceneWordId, String pageTitle, String pageLink, String sjfl,String html,
			Date createTime, Date updateTime) {
		super();
		this.scenePageId = scenePageId;
		this.sceneWordId = sceneWordId;
		this.pageTitle = pageTitle;
		this.pageLink = pageLink;
		this.sjfl = sjfl;
		this.html=html;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}


	
	
}
