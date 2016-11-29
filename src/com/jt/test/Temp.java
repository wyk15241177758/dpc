package com.jt.test;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Temp {
public static void main(String[] args) throws Exception {
	String a="2016-11-15 22:47:44.0";
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
	System.out.println(sdf.parse(a));
}
}
