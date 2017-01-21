package com.jt.quartz;

import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;

import com.jt.gateway.service.job.CreateSchedulerFactory;
import com.jt.test.quartz.QuartzJob;
import com.jt.test.quartz.SimpleQuartzTest;

public class QuartzCenter {
	public  static SchedulerFactory gSchedulerFactory = CreateSchedulerFactory.getFactory();
	
	public void addQuartz(){
//		Scheduler scheduler = SimpleQuartzTest.gSchedulerFactory.getScheduler();
//	      // 系统当前时间10秒后
//	      long startTime = System.currentTimeMillis() + 5000L;
//	      SimpleTrigger trigger = new SimpleTrigger("myTrigger", null, new Date(
//	              startTime),  new Date(startTime+5000l), 0, 0L);
//
//	      JobDetail jobDetail = new JobDetail();
//	      jobDetail.setJobClass(QuartzJob.class);
//	      jobDetail.setName("test");
//	      jobDetail.setGroup("A");
//
//	      scheduler.scheduleJob(jobDetail, trigger);
//	      scheduler.start();
	}
	
}
