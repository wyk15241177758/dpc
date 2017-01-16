package com.jt.test;


import java.io.IOException;
import java.util.Date;

import org.jdom.JDOMException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jt.scene.bean.SceneWord;


public class GsonTest {
public static void main(String[] args) throws JDOMException, IOException {
	Gson gson= new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	SceneWord word=new SceneWord(5,2,"场景","好玩","公园",new Date(),new Date(),null,null);
	System.out.println(gson.toJson(word));;
}
}
