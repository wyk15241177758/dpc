package com.jt.scene.service;


import java.util.List;

import com.jt.scene.bean.SceneWord;

public interface SceneWordService   {
	public void addSceneWord(SceneWord sceneWord);
	public void deleteSceneWord(SceneWord sceneWord);
	public void updateSceneWord(SceneWord sceneWord);
	public List<SceneWord> getAllSceneWords();
	public SceneWord getSceneWordById(Integer sceneWordId);
	public List<SceneWord> getWordsBySceneId(Integer sceneId);
	public List<SceneWord> getWordsBySceneName(String sceneName);
	;
}
