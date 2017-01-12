package com.jt.scene.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.jt.gateway.service.job.BasicServicveImpl;
import com.jt.scene.bean.ScenePage;
import com.jt.scene.bean.SceneWord;
import com.jt.scene.service.ScenePageService;
import com.jt.scene.service.SceneWordService;

public class SceneWordServiceImpl  extends BasicServicveImpl implements SceneWordService{
	private ScenePageService scenePageService;

	
	public ScenePageService getScenePageService() {
		return scenePageService;
	}

	public void setScenePageService(ScenePageService scenePageService) {
		this.scenePageService = scenePageService;
	}

	//增加任务
	public void addSceneWord(SceneWord sceneWord){
		this.dao.save(sceneWord);
		List<ScenePage> scenePageList=sceneWord.getScenePageList();
		if(scenePageList!=null){
			for(ScenePage scenePage:scenePageList){
				if(scenePage.getScenePageId()!=null&&scenePage.getScenePageId()!=0){
					scenePageService.updateScenePage(scenePage);	
				}else{
					scenePageService.addScenePage(scenePage);
				}
			}
		}
	}
	
	//删除，将关联的scenePage删除
	public void deleteSceneWord(SceneWord sceneWord){
		List<ScenePage> scenePageList=sceneWord.getScenePageList();
		if(scenePageList!=null){
			for(ScenePage scenePage:scenePageList){
				scenePageService.deleteScenePage(scenePage);
			}
		}
		this.dao.delete(sceneWord);
	}
	
	//修改，需要将scenePage保存起来
	public void updateSceneWord(SceneWord sceneWord){
		this.dao.update(sceneWord);
		List<ScenePage> scenePageList=sceneWord.getScenePageList();
		if(scenePageList!=null){
			for(ScenePage scenePage:scenePageList){
				if(scenePage.getScenePageId()!=null&&scenePage.getScenePageId()!=0){
					scenePageService.updateScenePage(scenePage);	
				}else{
					scenePageService.addScenePage(scenePage);
				}
			}
		}
	}
	
	
	public SceneWord getSceneWordById(Integer sceneWordId){ 
		List<SceneWord> list=new ArrayList<SceneWord>();
		list=this.dao.query("from com.jt.scene.bean.SceneWord where sceneWordId='"+sceneWordId+"'");
		if(list.size()!=0){
			for(SceneWord sceneWord:list){
				List<ScenePage> scenePageList=scenePageService.getScenePages(sceneWord.getSceneWordId());
				sceneWord.setScenePageList(scenePageList);
			}
			return list.get(0);
		}else{
			return null;
		}
	}
	
	//获得所有场景word
	public List<SceneWord> getAllSceneWords(){
		List<SceneWord> list=this.dao.query("from com.jt.scene.bean.SceneWord");
		for(SceneWord sceneWord:list){
			List<ScenePage> scenePageList=scenePageService.getScenePages(sceneWord.getSceneWordId());
			sceneWord.setScenePageList(scenePageList);
		}
		return list;
	}
	
	//获得指定场景words
	public List<SceneWord> getWordsBySceneName(String sceneName){
		List<SceneWord> list=new ArrayList<SceneWord>();
		list=this.dao.query("from com.jt.scene.bean.SceneWord where sceneName='"+sceneName+"'");
		for(SceneWord sceneWord:list){
			List<ScenePage> scenePageList=scenePageService.getScenePages(sceneWord.getSceneWordId());
			sceneWord.setScenePageList(scenePageList);
		}
		return list;
	}
	
	//获得指定场景words
	public List<SceneWord> getWordsBySceneId(Integer sceneId){
		List<SceneWord> list=new ArrayList<SceneWord>();
		list=this.dao.query("from com.jt.scene.bean.SceneWord where sceneId='"+sceneId+"'");
		for(SceneWord sceneWord:list){
			List<ScenePage> scenePageList=scenePageService.getScenePages(sceneWord.getSceneWordId());
			sceneWord.setScenePageList(scenePageList);
		}
		return list;
	}
}
