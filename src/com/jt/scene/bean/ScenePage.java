package com.jt.scene.bean;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 预设场景对应的页面，可以指定多个
 * 
 * @author zxb
 *
 * 2016年11月27日
 */
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "scenepage")  
@XmlType(propOrder = {})  

public class ScenePage {
	@XmlElement  
	private Integer scenePageId;
	@XmlElement  
	private Integer sceneWordId;
	@XmlElement  
	private String pageTitle;
	@XmlElement  
	private String pageLink;
	@XmlElement  
	private String sjfl;
	@XmlElement  
	private String html;
	@XmlElement  
	private Date createTime;
	@XmlElement  
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
