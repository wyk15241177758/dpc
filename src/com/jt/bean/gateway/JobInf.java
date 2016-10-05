package com.jt.bean.gateway;

import java.util.Date;

/**
 * JobInf entity. @author MyEclipse Persistence Tools
 */

public class JobInf implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 7123357245448757320L;
	private Long jobId;
	private String jobName;

	//1:未启动
	//2:已启动
	//-1:已删除
	private Integer jobStatus;
	private String cronExpression;
	private String beanClass;
	private String description;
	//任务参数名
	public static String PARAM_NAME="param";
	//任务组名
	public static String jobGroup="JOBGROUP_NAME";
	//触发器名称
	private String triggerName="TRIGGER_NAME";
	//触发器组名
	public static String triggerGroupName="TRIGGERGROUP_NAME";
	
	private Date createTime;
	private Date updateTime;
	@Override
	public String toString() {
		return "JobInf [jobId=" + jobId + ", jobName=" + jobName + ", jobStatus=" + jobStatus + ", cronExpression="
				+ cronExpression + ", beanClass=" + beanClass + ", description=" + description + ", createTime="
				+ createTime + ", updateTime=" + updateTime + "]";
	}

	
	
	// Constructors

	/** default constructor */
	public JobInf() {
	}
	
	

//	public JobInf(Long jobId, String jobName, Integer jobStatus, String cronExpression, String beanClass,
//			String description, Date createTime, Date updateTime) {
//		super();
//		this.jobId = jobId;
//		this.jobName = jobName;
//		this.jobStatus = jobStatus;
//		this.cronExpression = cronExpression;
//		this.beanClass = beanClass;
//		this.description = description;
//		this.createTime = createTime;
//		this.updateTime = updateTime;
//	}
	



	public JobInf(Long jobId, String jobName, Integer jobStatus, String cronExpression, String beanClass,
			String description, String triggerName, Date createTime, Date updateTime) {
		super();
		this.jobId = jobId;
		this.jobName = jobName;
		this.jobStatus = jobStatus;
		this.cronExpression = cronExpression;
		this.beanClass = beanClass;
		this.description = description;
		this.triggerName = triggerName;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}



	public Long getJobId() {
		return this.jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public String getJobName() {
		return this.jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}




	public Integer getJobStatus() {
		return this.jobStatus;
	}

	public void setJobStatus(Integer jobStatus) {
		this.jobStatus = jobStatus;
	}

	public String getCronExpression() {
		return this.cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getBeanClass() {
		return this.beanClass;
	}

	public void setBeanClass(String beanClass) {
		this.beanClass = beanClass;
	}


	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}



	public static String getJobGroup() {
		return jobGroup;
	}



	public static void setJobGroup(String jobGroup) {
		JobInf.jobGroup = jobGroup;
	}






	public String getTriggerName() {
		return triggerName;
	}



	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}



	public static String getTriggerGroupName() {
		return triggerGroupName;
	}



	public static void setTriggerGroupName(String triggerGroupName) {
		JobInf.triggerGroupName = triggerGroupName;
	}



}