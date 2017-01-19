package com.jt.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class Temp {
	private Integer test;
	
public Integer getTest() {
		return test;
	}

	public void setTest(Integer test) {
		this.test = test;
	}

public static void main(String[] args) throws Exception {
	String a="";
	List<String> b=new ArrayList<String>();
	b=Arrays.asList(a.split(";"));
	System.out.println(b.size());
}
}
