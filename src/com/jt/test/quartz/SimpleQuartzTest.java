package com.jt.test.quartz;

import java.util.Date;

import org.junit.Test;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

import com.jt.bean.gateway.JobInf;
import com.jt.gateway.service.job.CreateSchedulerFactory;

public class SimpleQuartzTest {
	public  static SchedulerFactory gSchedulerFactory = CreateSchedulerFactory.getFactory();
	public static void test1() throws SchedulerException{
		   Scheduler scheduler = gSchedulerFactory.getScheduler();
	        // 系统当前时间10秒后
	        long startTime = System.currentTimeMillis() + 5000L;
	        SimpleTrigger trigger = new SimpleTrigger("myTrigger", null, new Date(
	                startTime),  new Date(startTime+5000l), 0, 0L);

	        JobDetail jobDetail = new JobDetail();
	        jobDetail.setJobClass(QuartzJob.class);
	        jobDetail.setName("test");
	        jobDetail.setGroup("A");

	        scheduler.scheduleJob(jobDetail, trigger);
	        scheduler.start();
	}
	
	public  static void  test2() throws SchedulerException, ClassNotFoundException{
		Scheduler scheduler = gSchedulerFactory.getScheduler();
		String jobName="任务名称";
		String sInterval="5000";
		Long interval=null;
		interval=Long.parseLong(sInterval);
		String className=QuartzJob.class.getName();
		SimpleTrigger trigger = new SimpleTrigger(jobName, jobName, new Date(), null, -1, interval);
//				new SimpleTrigger(jobName, null);
//		trigger.setRepeatInterval(interval);
//		 SimpleTrigger trigger = new SimpleTrigger("myTrigger", null, new Date(
//	                startTime),  new Date(startTime+5000l), 0, 0L);
//		trigger.setre
		JobDetail jobDetail = new JobDetail();
		jobDetail.setJobClass(QuartzJob.class);
		jobDetail.setName(jobName);
		jobDetail.setGroup("quartz");
		scheduler.scheduleJob(jobDetail, trigger);
		
		scheduler.start();
		
	}
	public static void main(String[] args) throws Exception {
//		test1();
		test2();
//	        scheduler.shutdown();
	        
	}
}
