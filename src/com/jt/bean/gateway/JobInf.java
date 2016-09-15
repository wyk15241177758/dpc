package com.jt.bean.gateway;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

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
	private String orgname;
	private String description;
	private String triggerName;
	private String triggerGroupName;
	private Integer type;
	private String savePath;
	private String localPath;
	private Timestamp createTime;
	private Timestamp updateTime;
	private String prefix;
	private Integer dataType;
	private String okPrefix;
	private Set jobAddControls = new HashSet(0);
	private Set jobLogs = new HashSet(0);
	private Set jobFpkzNums = new HashSet(0);
	private Set operationTaskLog = new HashSet(0);
	// Constructors

	/** default constructor */
	public JobInf() {
	}

	/** minimal constructor */
	public JobInf(Long jobId, String jobName, String jobGroup,
			Integer jobStatus, String cronExpression, String beanClass,
			String orgname, String triggerName, String triggerGroupName,
			Integer type, Timestamp createTime, Timestamp updateTime, String prefix,
			Integer dataType) {
		this.jobId = jobId;
		this.jobName = jobName;
		this.jobGroup = jobGroup;
		this.jobStatus = jobStatus;
		this.cronExpression = cronExpression;
		this.beanClass = beanClass;
		this.orgname = orgname;
		this.triggerName = triggerName;
		this.triggerGroupName = triggerGroupName;
		this.type = type;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.prefix = prefix;
		this.dataType = dataType;
	}

	/** full constructor */
	public JobInf(Long jobId, String jobName, String jobGroup,
			Integer jobStatus, String cronExpression, String beanClass,
			String orgname, String description, String triggerName,
			String triggerGroupName, Integer type, String savePath, String localPath,
			Timestamp createTime, Timestamp updateTime, String prefix,
			Integer dataType, String okPrefix) {
		this.jobId = jobId;
		this.jobName = jobName;
		this.jobGroup = jobGroup;
		this.jobStatus = jobStatus;
		this.cronExpression = cronExpression;
		this.beanClass = beanClass;
		this.orgname = orgname;
		this.description = description;
		this.triggerName = triggerName;
		this.triggerGroupName = triggerGroupName;
		this.type = type;
		this.savePath = savePath;
		this.localPath = localPath;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.prefix = prefix;
		this.dataType = dataType;
		this.okPrefix = okPrefix;
	}

	// Property accessors

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

	public String getOrgname() {
		return this.orgname;
	}

	public void setOrgname(String orgname) {
		this.orgname = orgname;
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

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getSavePath() {
		return this.savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public String getLocalPath() {
		return this.localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
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

	public String getPrefix() {
		return this.prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}


	public Integer getDataType() {
		return this.dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	public String getOkPrefix() {
		return this.okPrefix;
	}

	public void setOkPrefix(String okPrefix) {
		this.okPrefix = okPrefix;
	}

	

	public Set getJobAddControls() {
		return jobAddControls;
	}

	public void setJobAddControls(Set jobAddControls) {
		this.jobAddControls = jobAddControls;
	}

	public Set getJobLogs() {
		return jobLogs;
	}

	public void setJobLogs(Set jobLogs) {
		this.jobLogs = jobLogs;
	}

	public Set getJobFpkzNums() {
		return jobFpkzNums;
	}

	public void setJobFpkzNums(Set jobFpkzNums) {
		this.jobFpkzNums = jobFpkzNums;
	}

	public Set getOperationTaskLog() {
		return operationTaskLog;
	}

	public void setOperationTaskLog(Set operationTaskLog) {
		this.operationTaskLog = operationTaskLog;
	}

	




}