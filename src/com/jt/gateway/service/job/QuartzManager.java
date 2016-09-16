/**
 * @Description: 
 *
 * @Title: QuartzManager.java
 * @Package com.joyce.quartz
 * @Copyright: Copyright (c) 2014
 *
 * @author Comsys-LZP
 * @date 2014-6-26 ����03:15:52
 * @version V2.0
 */
package com.jt.gateway.service.job;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;

import com.jt.bean.gateway.JobInf;
/**
 * @Description: ��ʱ���������
 * @ClassName: QuartzManager
 * @Copyright: Copyright (c) 2014
 * @author Comsys-LZP
 * @date 2014-6-26 ����03:15:52
 * @version V2.0
 */
public class QuartzManager {
	private static SchedulerFactory gSchedulerFactory = CreateSchedulerFactory.getFactory();
	private static String JOB_GROUP_NAME = "EXTJWEB_JOBGROUP_NAME";
	private static String TRIGGER_GROUP_NAME = "EXTJWEB_TRIGGERGROUP_NAME";
	private static String PARAM_NAME="param";

	
	
	
	
	
	/**
	 * @Description: ���һ����ʱ����ʹ��Ĭ�ϵ�������������������������������
	 * @param jobName
	 *            ������
	 * @param cls
	 *            ����
	 * @param time
	 *            ʱ�����ã��ο�quartz˵����
	 * @Title: QuartzManager.java
	 * @Copyright: Copyright (c) 2014
	 * @author Comsys-LZP
	 * @date 2014-6-26 ����03:47:44
	 * @version V2.0
	 */
	public static void addJob(String jobName, Class<?> cls, String time) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			JobDetail jobDetail = new JobDetail(jobName, JOB_GROUP_NAME, cls);// �������������飬����ִ����
			// ������
			CronTrigger trigger = new CronTrigger(jobName, TRIGGER_GROUP_NAME);// ��������,��������
			trigger.setCronExpression(time);// ������ʱ���趨
			sched.scheduleJob(jobDetail, trigger);
			// ����
			if (!sched.isShutdown()) {
				sched.start();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @Description: ���һ����ʱ����
	 * 
	 * @param jobName
	 *            ������
	 * @param jobGroupName
	 *            ��������
	 * @param triggerName
	 *            ��������
	 * @param triggerGroupName
	 *            ����������
	 * @param jobClass
	 *            ����
	 * @param time
	 *            ʱ�����ã��ο�quartz˵���ĵ�
	
	 * @Title: QuartzManager.java
	 * @Copyright: Copyright (c) 2014
	 * @author Comsys-LZP
	 * @date 2014-6-26 ����03:48:15
	 * @version V2.0
	 */
	public static void addJob(String jobName, String jobGroupName,
			String triggerName, String triggerGroupName, Class<?> jobClass,
			String time) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			JobDetail jobDetail = new JobDetail(jobName, jobGroupName, jobClass);// �������������飬����ִ����
			
			// ������
			CronTrigger trigger = new CronTrigger(triggerName, triggerGroupName);// ��������,��������
			trigger.setCronExpression(time);// ������ʱ���趨
			sched.scheduleJob(jobDetail, trigger);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * @Description: ���һ����ʱ����
	 * @param param
	 * @author �����
	 * 
	 */
	public static void addJob(JobInf param) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			JobDetail jobDetail = new JobDetail(param.getJobName(), param.getJobGroup(), Class.forName(param.getBeanClass()));// �������������飬����ִ����
			JobDataMap jobMap = jobDetail.getJobDataMap();
			jobMap.put(PARAM_NAME, param);
			// ������
			CronTrigger trigger = new CronTrigger(param.getTriggerName(), param.getTriggerGroupName());// ��������,��������
			trigger.setCronExpression(param.getCronExpression());// ������ʱ���趨
			sched.scheduleJob(jobDetail, trigger);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	
	

	/**
	 * @Description: �޸�һ������Ĵ���ʱ��(ʹ��Ĭ�ϵ�������������������������������)
	 * @param jobName
	 * @param time
	 * @Title: QuartzManager.java
	 * @Copyright: Copyright (c) 2014
	 * @author Comsys-LZP
	 * @date 2014-6-26 ����03:49:21
	 * @version V2.0
	 */
	public static void modifyJobTime(String jobName, String time) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			CronTrigger trigger = (CronTrigger) sched.getTrigger(jobName,TRIGGER_GROUP_NAME);
			if (trigger == null) {
				return;
			}
			String oldTime = trigger.getCronExpression();
			if (!oldTime.equalsIgnoreCase(time)) {
				JobDetail jobDetail = sched.getJobDetail(jobName,JOB_GROUP_NAME);
				Class<?> objJobClass = jobDetail.getJobClass();
				removeJob(jobName);
				addJob(jobName, objJobClass, time);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @Description: �޸�һ������Ĵ���ʱ��
	 * @param triggerName
	 * @param triggerGroupName
	 * @param time
	 * @Title: QuartzManager.java
	 * @Copyright: Copyright (c) 2014
	 * @author Comsys-LZP
	 * @date 2014-6-26 ����03:49:37
	 * @version V2.0
	 */
	public static void modifyJobTime(String triggerName,
			String triggerGroupName, String time) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerName,triggerGroupName);
			if (trigger == null) {
				return;
			}
			String oldTime = trigger.getCronExpression();
			if (!oldTime.equalsIgnoreCase(time)) {
				CronTrigger ct = (CronTrigger) trigger;
				// �޸�ʱ��
				ct.setCronExpression(time);
				// ����������
				sched.resumeTrigger(triggerName, triggerGroupName);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @Description: �Ƴ�һ������(ʹ��Ĭ�ϵ�������������������������������)
	 * 
	 * @param jobName
	 * 
	 * @Title: QuartzManager.java
	 * @Copyright: Copyright (c) 2014
	 * 
	 * @author Comsys-LZP
	 * @date 2014-6-26 ����03:49:51
	 * @version V2.0
	 */
	public static void removeJob(String jobName) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			sched.pauseTrigger(jobName, TRIGGER_GROUP_NAME);// ֹͣ������
			sched.unscheduleJob(jobName, TRIGGER_GROUP_NAME);// �Ƴ�������
			sched.deleteJob(jobName, JOB_GROUP_NAME);// ɾ������
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @Description: �Ƴ�һ������
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
	 * @date 2014-6-26 ����03:50:01
	 * @version V2.0
	 */
	public static void removeJob(String jobName, String jobGroupName,
			String triggerName, String triggerGroupName) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			sched.pauseTrigger(triggerName, triggerGroupName);// ֹͣ������
			sched.unscheduleJob(triggerName, triggerGroupName);// �Ƴ�������
			sched.deleteJob(jobName, jobGroupName);// ɾ������
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @Description:�������ж�ʱ����
	 * @Title: QuartzManager.java
	 * @Copyright: Copyright (c) 2014
	 * 
	 * @author Comsys-LZP
	 * @date 2014-6-26 ����03:50:18
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
	 * @Description:�ر����ж�ʱ����
	 * 
	 * 
	 * @Title: QuartzManager.java
	 * @Copyright: Copyright (c) 2014
	 * 
	 * @author Comsys-LZP
	 * @date 2014-6-26 ����03:50:26
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
