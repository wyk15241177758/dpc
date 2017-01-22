package com.jt.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public abstract class QuartzJob implements Job{
	private Long internal;
	
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
	}
	
	
	
}
