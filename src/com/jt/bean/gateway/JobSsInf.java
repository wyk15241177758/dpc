package com.jt.bean.gateway;

import java.sql.Timestamp;

/**
 * JobSsInf entity. @author MyEclipse Persistence Tools
 */

public class JobSsInf implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 3919738432797723573L;
	private Long jobId;
	private String host;
	private String port;
	private String passwd;
	private String path;
	private Integer type;
	private String desPasswd;
	private String prefix;
	private String suffix;
	private String oksuffix;
	private String savedir;
	private Timestamp createTime;
	private Timestamp changeTime;
	private String cronExpression;
	private String beanClass;
	private String description;
	private String triggerName;
	private String triggerGroupName;
	private String jobName;
	private String jobGroup;
	private String user;
	private String history;
	private Long  days;
	private Integer jobStatus;
	private String keysName;
	private String orgName;

	// Constructors


	public String getHistory() {
		return history;
	}

	public Long getDays() {
		return days;
	}

	public void setDays(Long days) {
		this.days = days;
	}

	public void setHistory(String history) {
		this.history = history;
	}

	/** default constructor */
	public JobSsInf() {
	}

	/** minimal constructor */
	public JobSsInf(Long jobId, String host, String port, String passwd,
			String path) {
		this.jobId = jobId;
		this.host = host;
		this.port = port;
		this.passwd = passwd;
		this.path = path;
	}

	/** full constructor */
	public JobSsInf(Long jobId, String host, String port, String passwd,
			String path, Integer type, String desPasswd, String prefix,
			String suffix, String oksuffix, String savedir,
			Timestamp createTime, Timestamp changeTime, String cronExpression,
			String beanClass, String description, String triggerName,
			String triggerGroupName, String jobName, String jobGroup,String user,String history,Long days,Integer jobStatus,String keysName,String orgName) {
		this.jobId = jobId;
		this.host = host;
		this.port = port;
		this.passwd = passwd;
		this.path = path;
		this.type = type;
		this.desPasswd = desPasswd;
		this.prefix = prefix;
		this.suffix = suffix;
		this.oksuffix = oksuffix;
		this.savedir = savedir;
		this.createTime = createTime;
		this.changeTime = changeTime;
		this.cronExpression = cronExpression;
		this.beanClass = beanClass;
		this.description = description;
		this.triggerName = triggerName;
		this.triggerGroupName = triggerGroupName;
		this.jobName = jobName;
		this.jobGroup = jobGroup;
		this.user=user;
		this.history=history;
		this.days=days;
		this.jobStatus = jobStatus;
		this.keysName = keysName;
		this.orgName = orgName;
	}

	// Property accessors

	public Long getJobId() {
		return this.jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return this.port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getPasswd() {
		return this.passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getDesPasswd() {
		return this.desPasswd;
	}

	public void setDesPasswd(String desPasswd) {
		this.desPasswd = desPasswd;
	}

	public String getPrefix() {
		return this.prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return this.suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getOksuffix() {
		return this.oksuffix;
	}

	public void setOksuffix(String oksuffix) {
		this.oksuffix = oksuffix;
	}

	public String getSavedir() {
		return this.savedir;
	}

	public void setSavedir(String savedir) {
		this.savedir = savedir;
	}

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getChangeTime() {
		return this.changeTime;
	}

	public void setChangeTime(Timestamp changeTime) {
		this.changeTime = changeTime;
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

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Integer getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(Integer jobStatus) {
		this.jobStatus = jobStatus;
	}

	public String getKeysName() {
		return keysName;
	}

	public void setKeysName(String keysName) {
		this.keysName = keysName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
		
	
	
	

}