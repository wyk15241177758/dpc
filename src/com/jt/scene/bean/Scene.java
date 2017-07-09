package com.jt.scene.bean;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 预设场景类型，包括场景名称和ID
 * 
 * @author zxb
 *
 * 2016年11月27日
 */

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "scene")  
@XmlType(propOrder = {})  


public class Scene {
	@XmlElement  
	private Integer sceneId;
	@XmlElement  
	private String sceneName;
	@XmlElement  
	private Date createTime;
	@XmlElement  
	private Date updateTime;

	private List<SceneWord> sceneWordList;
	public Scene(){
		
	}
	
	public Scene(Integer sceneId, String sceneName, Date createTime, Date updateTime) {
		super();
		this.sceneId = sceneId;
		this.sceneName = sceneName;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public Scene(Integer sceneId, String sceneName, Date createTime, Date updateTime,List<SceneWord> sceneWordList) {
		super();
		this.sceneId = sceneId;
		this.sceneName = sceneName;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}
	
	public Integer getSceneId() {
		return sceneId;
	}
	public void setSceneId(Integer sceneId) {
		this.sceneId = sceneId;
	}
	public String getSceneName() {
		return sceneName;
	}
	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
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
	

	public List<SceneWord> getSceneWordList() {
		return sceneWordList;
	}
	public void setSceneWordList(List<SceneWord> sceneWordList) {
		this.sceneWordList = sceneWordList;
	}

	
}
