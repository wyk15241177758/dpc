package com.jt.scene.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.jt.gateway.service.job.BasicServicveImpl;
import com.jt.scene.bean.Scene;
import com.jt.scene.bean.SceneWord;
import com.jt.scene.service.SceneWordService;

public class SceneWordServiceImpl  extends BasicServicveImpl implements SceneWordService{
	//增加任务
	public void addSceneWord(SceneWord sceneWord){
		this.dao.save(sceneWord);
	}
	
	//删除任务
	public void deleteSceneWord(SceneWord sceneWord){
		this.dao.delete(sceneWord);
	}
	
	//修改任务
	public void updateSceneWord(SceneWord sceneWord){
		this.dao.update(sceneWord);
	}
	
	
	public SceneWord getSceneWordById(Integer sceneWordId){ 
		List<SceneWord> list=new ArrayList<SceneWord>();
		list=this.dao.query("from com.jt.scene.bean.SceneWord where sceneWordId='"+sceneWordId+"'");
		if(list.size()!=0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	//获得所有场景word
	public List<SceneWord> getAllSceneWords(){
		List<SceneWord> list=this.dao.query("from com.jt.scene.bean.SceneWord");
		return list;
	}
	
	//获得指定场景words
	public List<SceneWord> getWordsBySceneName(String sceneName){
		List<SceneWord> list=new ArrayList<SceneWord>();
		list=this.dao.query("from com.jt.scene.bean.SceneWord where sceneName='"+sceneName+"'");
		return list;
	}
	
	//获得指定场景words
	public List<SceneWord> getWordsBySceneId(Integer sceneId){
		List<SceneWord> list=new ArrayList<SceneWord>();
		list=this.dao.query("from com.jt.scene.bean.SceneWord where sceneId='"+sceneId+"'");
		return list;
	}
}
