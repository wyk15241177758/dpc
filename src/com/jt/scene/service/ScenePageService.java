package com.jt.scene.service;


import java.util.List;

import com.jt.scene.bean.ScenePage;

public interface ScenePageService   {
	public void addScenePage(ScenePage scenePage);
	public void deleteScenePage(ScenePage scenePage);
	public void updateScenePage(ScenePage scenePage);
	public ScenePage getScenePageById(Integer scenePageId);
	public List<ScenePage> getScenePages(Integer sceneWordId);
	}
