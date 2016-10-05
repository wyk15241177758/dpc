package com.jt.test.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jt.bean.gateway.JobInf;

public class QuartzJob  implements Job{

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobInf job=(JobInf)(context.getJobDetail().getJobDataMap().get("param"));
		System.out.println("in quartz jobname=["+job.getJobName()+"]");
		
	}

}
