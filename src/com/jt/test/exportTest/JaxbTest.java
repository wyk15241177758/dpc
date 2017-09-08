package com.jt.test.exportTest;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.jt.scene.bean.Scene;
import com.jt.scene.bean.ScenePage;
import com.jt.scene.bean.SceneWord;

public class JaxbTest {
public static void main(String[] args) throws JAXBException {
	
	ScenePage scenePage=new ScenePage(1,1,"title11111","link11111","sjfl","html",new Date(),new Date()			);
	ScenePage scenePage2=new ScenePage(1,1,"title33333","link22222","sjfl","html",new Date(),new Date()			);

	List<ScenePage> scenePageList=new ArrayList<ScenePage>();
	scenePageList.add(scenePage);
	scenePageList.add(scenePage2);
	SceneWord sceneWord=new SceneWord(1, 1, "sceneName", "enterWords", "outWords", new Date(),
			new Date(), "sjfl", scenePageList);
	List<SceneWord> sceneWordList=new ArrayList<SceneWord>();
	sceneWordList.add(sceneWord);
	
//	Scene scene=new Scene(1,"test",new Date(),new Date(),sceneWordList);
	
	Marshaller mashaller =  JAXBContext.newInstance(SceneWord.class).createMarshaller();  
	
	mashaller.marshal(sceneWord, System.out);  
}
}
