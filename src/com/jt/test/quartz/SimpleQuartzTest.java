package com.jt.test.quartz;

import java.util.Date;

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
	public void test() throws SchedulerException, ClassNotFoundException{
		Scheduler sched = gSchedulerFactory.getScheduler();
		JobDetail jobDetail = new JobDetail("test",JobInf.jobGroup, Class.forName(QuartzJob.class.getName()));// 任务名，任务组，任务执行类
		// 触发器
		SimpleTrigger st=new SimpleTrigger("test", "group", new Date(System.currentTimeMillis()+1000l),
				null, 0, 0L);
//		CronTrigger trigger = new CronTrigger(param.getTriggerName(), JobInf.triggerGroupName);// 触发器名,触发器组
//		trigger.setCronExpression(param.getCronExpression());// 触发器时间设定
		sched.scheduleJob(jobDetail, st);
	}
	public static void main(String[] args) throws Exception {
	        Scheduler scheduler = SimpleQuartzTest.gSchedulerFactory.getScheduler();
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
//	        scheduler.shutdown();
	        
	}
}
