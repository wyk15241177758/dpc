package com.jt.scene.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.jt.gateway.service.job.BasicServicveImpl;
import com.jt.scene.bean.Scene;
import com.jt.scene.service.SceneService;

public class SceneServiceImpl  extends BasicServicveImpl implements SceneService{
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
			return list.get(0);
		}else{
			return null;
		}
	}
	public Scene getSceneById(Integer sceneId){
		List<Scene> list=new ArrayList<Scene>();
		list=this.dao.query("from com.jt.scene.bean.Scene where sceneId='"+sceneId+"'");
		if(list.size()!=0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	//获得所有场景
	public List<Scene> getAllScenes(){
		List<Scene> list=this.dao.query("from com.jt.scene.bean.Scene");
		return list;
	}
}
