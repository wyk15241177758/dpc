/**
 * @Description: 
 *
 * @Title: QuartzManager.java
 * @Package com.joyce.quartz
 * @Copyright: Copyright (c) 2014
 *
 * @author Comsys-LZP
 * @date 2014-6-26 下午03:15:52
 * @version V2.0
 */
package com.jt.gateway.service.quartz;
import java.util.Map;

import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;

import com.jt.bean.gateway.JobInf;
import com.jt.gateway.service.job.CreateSchedulerFactory;
import com.jt.gateway.service.operation.IndexTask;
/**
 * @Description: 定时任务管理类
 * @ClassName: QuartzManager
 * @Copyright: Copyright (c) 2014
 * @author Comsys-LZP
 * @date 2014-6-26 下午03:15:52
 * @version V2.0
 */
public class QuartzManager {
	private static SchedulerFactory gSchedulerFactory = CreateSchedulerFactory.getFactory();
	
	
	/**
	 * @Description: 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
	 * @param jobName
	 *            任务名
	 * @param cls
	 *            任务
	 * @param time
	 *            时间设置，参考quartz说明文
	 * @Title: QuartzManager.java
	 * @Copyright: Copyright (c) 2014
	 * @author Comsys-LZP
	 * @date 2014-6-26 下午03:47:44
	 * @version V2.0
	 */
//	public static void addJob(String jobName, Class<?> cls, String time) {
//		try {
//			Scheduler sched = gSchedulerFactory.getScheduler();
//			JobDetail jobDetail = new JobDetail(jobName, JobInf.jobGroup, cls);// 任务名，任务组，任务执行类
//			// 触发器
//			CronTrigger trigger = new CronTrigger(JobInf.triggerName, JobInf.triggerGroupName);// 触发器名,触发器组
//			trigger.setCronExpression(time);// 触发器时间设定
//			sched.scheduleJob(jobDetail, trigger);
//			// 启动
//			if (!sched.isShutdown()) {
//				sched.start();
//			}
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//	public static void addJob(String jobName, Class<?> cls, String time,Map paramMap) {
//		try {
//			Scheduler sched = gSchedulerFactory.getScheduler();
//			JobDetail jobDetail = new JobDetail(jobName, JobInf.jobGroup, cls);// 任务名，任务组，任务执行类
//			//加入参数
//			jobDetail.getJobDataMap().putAll(paramMap);
//			// 触发器
//			CronTrigger trigger = new CronTrigger(JobInf.triggerName, JobInf.triggerGroupName);// 触发器名,触发器组
//			trigger.setCronExpression(time);// 触发器时间设定
//			sched.scheduleJob(jobDetail, trigger);
//			// 启动
//			if (!sched.isShutdown()) {
//				sched.start();
//			}
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
//	
//	/**
//	 * @Description: 添加一个定时任务
//	 * 
//	 * @param jobName
//	 *            任务名
//	 * @param jobGroupName
//	 *            任务组名
//	 * @param triggerName
//	 *            触发器名
//	 * @param triggerGroupName
//	 *            触发器组名
//	 * @param jobClass
//	 *            任务
//	 * @param time
//	 *            时间设置，参考quartz说明文档
//	
//	 * @Title: QuartzManager.java
//	 * @Copyright: Copyright (c) 2014
//	 * @author Comsys-LZP
//	 * @date 2014-6-26 下午03:48:15
//	 * @version V2.0
//	 */
//	public static void addJob(String jobName, String jobGroupName,
//			String triggerName, String triggerGroupName, Class<?> jobClass,
//			String time) {
//		try {
//			Scheduler sched = gSchedulerFactory.getScheduler();
//			JobDetail jobDetail = new JobDetail(jobName, jobGroupName, jobClass);// 任务名，任务组，任务执行类
//			
//			// 触发器
//			CronTrigger trigger = new CronTrigger(triggerName, triggerGroupName);// 触发器名,触发器组
//			trigger.setCronExpression(time);// 触发器时间设定
//			sched.scheduleJob(jobDetail, trigger);
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
//	
	
	/**
	 * @Description: 添加一个定时任务
	 * @param param
	 * @author 邹许红
	 * 
	 */
	public static void addJob(JobInf param) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			JobDetail jobDetail = new JobDetail(param.getJobName(),JobInf.jobGroup, Class.forName(param.getBeanClass()));// 任务名，任务组，任务执行类
			JobDataMap jobMap = jobDetail.getJobDataMap();
			jobMap.put(JobInf.PARAM_NAME, param);
			// 触发器
			CronTrigger trigger = new CronTrigger(param.getTriggerName(), JobInf.triggerGroupName);// 触发器名,触发器组
			trigger.setCronExpression(param.getCronExpression());// 触发器时间设定
			sched.scheduleJob(jobDetail, trigger);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @Description: 添加一个立即执行的任务
	 * @param param
	 * @author 邹许红
	 * @throws Exception 
	 * 
	 */
	public static void startImmediateJob(JobInf param) throws Exception {
		IndexTask task=new IndexTask();
		task.init4Quartz(param.getJobName());
		task.doExecute(param);
		
	}

	
//
//	/**
//	 * @Description: 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
//	 * @param jobName
//	 * @param time
//	 * @Title: QuartzManager.java
//	 * @Copyright: Copyright (c) 2014
//	 * @author Comsys-LZP
//	 * @date 2014-6-26 下午03:49:21
//	 * @version V2.0
//	 */
//	public static void modifyJobTime(String jobName, String time) {
//		try {
//			Scheduler sched = gSchedulerFactory.getScheduler();
//			CronTrigger trigger = (CronTrigger) sched.getTrigger(JobInf.triggerName, JobInf.triggerGroupName);
//			if (trigger == null) {
//				return;
//			}
//			String oldTime = trigger.getCronExpression();
//			if (!oldTime.equalsIgnoreCase(time)) {
//				JobDetail jobDetail = sched.getJobDetail(jobName,JobInf.jobGroup);
//				Class<?> objJobClass = jobDetail.getJobClass();
//				removeJob(jobName);
//				addJob(jobName, objJobClass, time);
//			}
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//	/**
//	 * @Description: 修改一个任务的触发时间
//	 * @param triggerName
//	 * @param triggerGroupName
//	 * @param time
//	 * @Title: QuartzManager.java
//	 * @Copyright: Copyright (c) 2014
//	 * @author Comsys-LZP
//	 * @date 2014-6-26 下午03:49:37
//	 * @version V2.0
//	 */
//	public static void modifyJobTime(String triggerName,
//			String triggerGroupName, String time) {
//		try {
//			Scheduler sched = gSchedulerFactory.getScheduler();
//			CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerName,triggerGroupName);
//			if (trigger == null) {
//				return;
//			}
//			String oldTime = trigger.getCronExpression();
//			if (!oldTime.equalsIgnoreCase(time)) {
//				CronTrigger ct = (CronTrigger) trigger;
//				// 修改时间
//				ct.setCronExpression(time);
//				// 重启触发器
//				sched.resumeTrigger(triggerName, triggerGroupName);
//			}
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}

	/**
	 * @Description: 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
	 * 
	 * @param jobName
	 * 
	 * @Title: QuartzManager.java
	 * @Copyright: Copyright (c) 2014
	 * 
	 * @author Comsys-LZP
	 * @date 2014-6-26 下午03:49:51
	 * @version V2.0
	 */
	public static void removeJob(String jobName) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			sched.pauseTrigger(jobName, JobInf.triggerGroupName);// 停止触发器
			sched.unscheduleJob(jobName, JobInf.triggerGroupName);// 移除触发器
			sched.deleteJob(jobName, JobInf.jobGroup);// 删除任务
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @Description: 移除一个任务
	 * 
	 * @param jobName
	 * @param jobGroupName
	 * @param triggerName
	 * @param triggerGroupName
	 * 
	 * @Title: QuartzManager.java
	 * @Copyright: Copyright (c) 2014
	 * 
	 * @author Comsys-LZP
	 * @date 2014-6-26 下午03:50:01
	 * @version V2.0
	 */
	public static void removeJob(String jobName, String jobGroupName,
			String triggerName, String triggerGroupName) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			sched.pauseTrigger(triggerName, triggerGroupName);// 停止触发器
			sched.unscheduleJob(triggerName, triggerGroupName);// 移除触发器
			sched.deleteJob(jobName, jobGroupName);// 删除任务
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @Description:启动所有定时任务
	 * @Title: QuartzManager.java
	 * @Copyright: Copyright (c) 2014
	 * 
	 * @author Comsys-LZP
	 * @date 2014-6-26 下午03:50:18
	 * @version V2.0
	 * 
	 */
	public static void startJobs() {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			sched.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @Description:关闭所有定时任务
	 * 
	 * 
	 * @Title: QuartzManager.java
	 * @Copyright: Copyright (c) 2014
	 * 
	 * @author Comsys-LZP
	 * @date 2014-6-26 下午03:50:26
	 * @version V2.0
	 */
	public static void shutdownJobs() {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			if (!sched.isShutdown()) {
				sched.shutdown();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
