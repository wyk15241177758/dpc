package com.jt.test;

import java.util.HashSet;
import java.util.Set;

public class SetTest {
public static void main(String[] args) {
	Set<String> set=new HashSet<String>();
	set.add("政府");
	set.add("创业");
	set.add("政务知识");
	for(String str:set){
		System.out.println(str);
	}
}
}
