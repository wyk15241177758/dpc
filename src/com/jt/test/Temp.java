package com.jt.test;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Temp {
	private Integer test;
	
public Integer getTest() {
		return test;
	}

	public void setTest(Integer test) {
		this.test = test;
	}

public static void main(String[] args) throws Exception {
	Temp temp=new Temp();
	System.out.println(temp.getTest()==0);
}
}
