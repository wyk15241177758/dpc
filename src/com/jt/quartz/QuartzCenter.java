package com.jt.quartz;
/**
 * 定时任务，在application.xml中配置使用。
 * 支持多个定时任务，系统启动时自动运行
 * 支持间隔时间和时间表达式两种触发器
 */
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;

import com.jt.gateway.service.job.CreateSchedulerFactory;

public class QuartzCenter {
	private static Logger logger = Logger.getLogger(QuartzCenter.class);
	public  static SchedulerFactory gSchedulerFactory = CreateSchedulerFactory.getFactory();
	private List<Map<String,String>> list;
	
	public List<Map<String, String>> getList() {
		return list;
	}

	public void setList(List<Map<String, String>> list) {
		this.list = list;
	}

	private void registerToQuartz() throws SchedulerException{
		logger.info("开始注册定时任务+++++++++++++");
		Scheduler scheduler = gSchedulerFactory.getScheduler();
		
		for(Map<String,String> map:list){
			String jobName=map.get("jobName");
			String sInterval=map.get("interval");
			String cronExpression=map.get("cronExpression");
			Long interval=null;
			try {
				logger.info("开始注册["+jobName+"]");
				String className=map.get("className");
				Date start = new Date(new Date().getTime()+interval);
				
				SimpleTrigger trigger = null;
				CronTrigger cronTrigger=null;
				//同时支持间隔时间和时间表达式两种定时器，优先间隔时间方式
				if(sInterval!=null&&sInterval.length()!=0){
					interval=Long.parseLong(sInterval);
					trigger=new SimpleTrigger(jobName, jobName, start, null, -1, interval);
				}else{
					cronTrigger=new CronTrigger(jobName, jobName);
					cronTrigger.setCronExpression(cronExpression);
				}
				
				JobDetail jobDetail = new JobDetail();
				jobDetail.setJobClass(Class.forName(className));
				jobDetail.setName(jobName);
				jobDetail.setGroup("quartz");
				JobDataMap jobMap = jobDetail.getJobDataMap();
				jobMap.put("param", map);
				if(trigger!=null){
					scheduler.scheduleJob(jobDetail, trigger);
				}else{
					scheduler.scheduleJob(jobDetail, cronTrigger);
				}
				logger.info("结束注册["+jobName+"]");
			} catch (NumberFormatException e) {
				logger.error(jobName+"间隔时间转换为long失败，跳过此任务");
				continue;
			} catch(Exception e){
				logger.error(jobName+"任务注册失败，报错信息：");
				e.printStackTrace();
				continue;
			}
			
		}
	      
	    scheduler.start();
	  	logger.info("结束注册定时任务+++++++++++++");
	}
	
}
