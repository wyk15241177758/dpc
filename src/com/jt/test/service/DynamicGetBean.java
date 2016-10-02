package com.jt.test.service;

import org.springframework.context.support.ApplicationObjectSupport;

import com.jt.gateway.service.job.JobInfService;

public class DynamicGetBean  extends ApplicationObjectSupport{
	
	public void test(){
		JobInfService jobService=getApplicationContext().getBean("jobInfImpl", JobInfService.class);
		System.out.println(jobService==null);
	}
}
