package com.jt.scene.bean;

import java.util.Date;
import java.util.List;

/**
 * 预设场景，包括场景名称、入口词、出口词。其中入口词和出口词都是多个词的组合，使用英文分号分割
 * 
 * @author zxb
 *
 * 2016年11月27日
 */
public class SceneWord {
	private Integer sceneWordId;
	private Integer sceneId;
	private String sceneName;
	private String enterWords;
	private String outWords;
	private Date createTime;
	private Date updateTime;
	private String sjfl;
	private List<ScenePage> scenePageList;
	public SceneWord(){
		
	}

	


	public SceneWord(Integer sceneWordId, Integer sceneId, String sceneName, String enterWords, String outWords,
			Date createTime, Date updateTime, String sjfl, List<ScenePage> scenePageList) {
		super();
		this.sceneWordId = sceneWordId;
		this.sceneId = sceneId;
		this.sceneName = sceneName;
		this.enterWords = enterWords;
		this.outWords = outWords;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.sjfl = sjfl;
		this.scenePageList = scenePageList;
	}




	public List<ScenePage> getScenePageList() {
		return scenePageList;
	}


	public void setScenePageList(List<ScenePage> scenePageList) {
		this.scenePageList = scenePageList;
	}


	public String getSjfl() {
		return sjfl;
	}


	public void setSjfl(String sjfl) {
		this.sjfl = sjfl;
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

	public String getSceneName() {
		return sceneName;
	}

	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}

	public String getEnterWords() {
		return enterWords;
	}
	public void setEnterWords(String enterWords) {
		this.enterWords = enterWords;
	}
	public String getOutWords() {
		return outWords;
	}
	public void setOutWords(String outWords) {
		this.outWords = outWords;
	}
	public Integer getSceneId() {
		return sceneId;
	}

	public void setSceneId(Integer sceneId) {
		this.sceneId = sceneId;
	}
	
}
