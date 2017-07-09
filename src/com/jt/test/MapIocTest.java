package com.jt.test;import java.util.List;
import java.util.Map;

public class MapIocTest {
	private List<Map<String,String>> list;
	public List<Map<String, String>> getList() {
		return list;
	}
	public void setList(List<Map<String, String>> list) {
		this.list = list;
	}
	public void test(){
		for(Map<String,String> map:list){
			for(Map.Entry<String, String> entry:map.entrySet()){
				System.out.println(entry.getKey()+" "+entry.getValue());
			}
		}
	
	}
}
