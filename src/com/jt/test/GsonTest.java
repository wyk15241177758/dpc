package com.jt.test;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jdom.JDOMException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class GsonTest {
public static void main(String[] args) throws JDOMException, IOException {
	Gson gson= new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	String[]str={"1","2"};
	System.out.println(gson.toJson(str));;
}
}
