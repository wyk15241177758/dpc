package com.jt.bean.gateway;

import java.sql.Timestamp;

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
	private String jobGroup;
	private Integer jobStatus;
	private String cronExpression;
	private String beanClass;
	private String description;
	private String triggerName;
	private String triggerGroupName;
	private Timestamp createTime;
	private Timestamp updateTime;
	// Constructors

	/** default constructor */
	public JobInf() {
	}

	

	// Property accessors

	public JobInf(Long jobId, String jobName, String jobGroup, Integer jobStatus, String cronExpression,
			String beanClass, String description, String triggerName, String triggerGroupName, Timestamp createTime,
			Timestamp updateTime) {
		super();
		this.jobId = jobId;
		this.jobName = jobName;
		this.jobGroup = jobGroup;
		this.jobStatus = jobStatus;
		this.cronExpression = cronExpression;
		this.beanClass = beanClass;
		this.description = description;
		this.triggerName = triggerName;
		this.triggerGroupName = triggerGroupName;
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

	public String getJobGroup() {
		return this.jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
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

	public String getTriggerName() {
		return this.triggerName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	public String getTriggerGroupName() {
		return this.triggerGroupName;
	}

	public void setTriggerGroupName(String triggerGroupName) {
		this.triggerGroupName = triggerGroupName;
	}


	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}


}