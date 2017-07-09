package com.jt.scene.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.jt.gateway.service.job.BasicServicveImpl;
import com.jt.scene.bean.ScenePage;
import com.jt.scene.service.ScenePageService;

public class ScenePageServiceImpl  extends BasicServicveImpl implements ScenePageService{
	

	//增加
	public void addScenePage(ScenePage scenePage){
		this.dao.save(scenePage);
	}
	
	//删除
	public void deleteScenePage(ScenePage scenePage){
		this.dao.delete(scenePage);
	}
	
	//修改
	public void updateScenePage(ScenePage scenePage){
		this.dao.update(scenePage);
	}
	public ScenePage getScenePageById(Integer scenePageId){
		List<ScenePage> list=new ArrayList<ScenePage>();
		list=this.dao.query("from com.jt.scene.bean.ScenePage where scenePageId='"+scenePageId+"'");
		if(list.size()!=0){
			return list.get(0);
		}else{
			return null;
		}
	}
	//获得场景词中的所有预设页面
	public List<ScenePage> getScenePages(Integer sceneWordId){
		//TODO
		List<ScenePage> list=this.dao.query("from com.jt.scene.bean.ScenePage"
				+ " where sceneWordId='"+sceneWordId+"'");
		return list;
	}
}
