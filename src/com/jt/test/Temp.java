package com.jt.test;

import java.util.Date;


public class Temp {
public static void main(String[] args) throws Exception {
	Date date=new Date();
	Thread.sleep(1000l);
	Date date2=new Date();
	System.out.println(date2.getTime()-date.getTime());
}
}
