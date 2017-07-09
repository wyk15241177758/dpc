package com.jt.scene.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.jt.gateway.service.job.BasicServicveImpl;
import com.jt.scene.bean.Scene;
import com.jt.scene.bean.SceneWord;
import com.jt.scene.service.SceneService;
import com.jt.scene.service.SceneWordService;

public class SceneServiceImpl  extends BasicServicveImpl implements SceneService{
	
	private SceneWordService sceneWordService;
	
	public SceneWordService getSceneWordService() {
		return sceneWordService;
	}

	public void setSceneWordService(SceneWordService sceneWordService) {
		this.sceneWordService = sceneWordService;
	}

	//增加任务
	public void addScene(Scene scene){
		this.dao.save(scene);
	}
	
	//删除任务
	public void deleteScene(Scene scene){
		this.dao.delete(scene);
	}
	
	//修改任务
	public void updateScene(Scene scene){
		this.dao.update(scene);
	}
	
	//查询场景
	public Scene getSceneByName(String sceneName){
		List<Scene> list=new ArrayList<Scene>();
		list=this.dao.query("from com.jt.scene.bean.Scene where sceneName='"+sceneName+"'");
		if(list.size()!=0){
			//遍历scene，将其关联的sceneWord插入
			for(Scene scene:list){
				List<SceneWord> sceneWordList=sceneWordService.getWordsBySceneId(scene.getSceneId());
				scene.setSceneWordList(sceneWordList);
			}
			return list.get(0);
		}else{
			return null;
		}
	}
	public Scene getSceneById(Integer sceneId){
		List<Scene> list=new ArrayList<Scene>();
		list=this.dao.query("from com.jt.scene.bean.Scene where sceneId='"+sceneId+"'");
		if(list.size()!=0){
			//遍历scene，将其关联的sceneWord插入
			for(Scene scene:list){
				List<SceneWord> sceneWordList=sceneWordService.getWordsBySceneId(scene.getSceneId());
				scene.setSceneWordList(sceneWordList);
			}
			return list.get(0);
		}else{
			return null;
		}
	}
	
	//获得所有场景
	public List<Scene> getAllScenes(){
		List<Scene> list=this.dao.query("from com.jt.scene.bean.Scene");
		//遍历scene，将其关联的sceneWord插入
		for(Scene scene:list){
			List<SceneWord> sceneWordList=sceneWordService.getWordsBySceneId(scene.getSceneId());
			scene.setSceneWordList(sceneWordList);
		}
		return list;
	}
}
