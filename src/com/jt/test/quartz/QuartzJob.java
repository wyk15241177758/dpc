package com.jt.test.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jt.bean.gateway.JobInf;

public class QuartzJob  implements Job{
	public  QuartzJob() {
		// TODO Auto-generated constructor stub
		System.out.println("in contruct");
	}
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("in quartz ");
		
	}

}
