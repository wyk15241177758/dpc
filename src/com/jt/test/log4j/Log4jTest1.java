package com.jt.test.log4j;

import org.apache.log4j.Logger;

import com.jt.gateway.service.job.JobInfImpl;

public class Log4jTest1 {
	private static Logger logger=Logger.getLogger(Log4jTest1.class);
	public void test(){
		System.out.println(logger==null);
	}
	public static void main(String[] args) {
		Log4jTest1 test1=new Log4jTest1();
		test1.test();
	}
}
