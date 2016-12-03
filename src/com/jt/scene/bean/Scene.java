package com.jt.scene.bean;

import java.util.Date;
import java.util.List;

/**
 * 预设场景类型，包括场景名称和ID
 * 
 * @author zxb
 *
 * 2016年11月27日
 */
public class Scene {
	private Integer sceneId;
	private String sceneName;
	private Date createTime;
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
