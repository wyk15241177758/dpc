package com.jt.scene.service;


import java.util.List;

import com.jt.scene.bean.Scene;

public interface SceneService   {
	public void addScene(Scene scene);
	public void deleteScene(Scene scene);
	public void updateScene(Scene scene);
	public Scene getSceneByName(String sceneName);
	public Scene getSceneById(Integer sceneId);
	public List<Scene> getAllScenes();
}
