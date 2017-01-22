package com.jt.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;



public class Temp {
	private Integer test;
	
public Integer getTest() {
		return test;
	}

	public void setTest(Integer test) {
		this.test = test;
	}

public static void main(String[] args) throws Exception {
	List<String> list=new ArrayList();
	Map<String,List<String>> map=new HashMap<String,List<String>>();
	Map<String,List<String>> map2=new HashMap<String,List<String>>();
	map.put("1", list);
	map2.put("1", list);
	list.add("1111");
	Set<String> set=map.keySet();
	for(String str:set){
		System.out.println(str+" val= "+map.get(str));
	}
	System.out.println("!!!!!!!!!!!!!!");
	set=map2.keySet();
	for(String str:set){
		System.out.println(str+" val= "+map2.get(str));
	}
}
}
