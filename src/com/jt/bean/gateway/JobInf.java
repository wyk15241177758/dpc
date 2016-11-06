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
	private String triggerName;
	//触发器组名
	public static String triggerGroupName="TRIGGERGROUP_NAME";
	
	private Date createTime;
	private Date updateTime;
	
	
	
	private String indexPath;
	private String sqlIp;
	private String sqlUser;
	private String sqlPw;
	private String sqlDb;
	private int sqlPort;
	private String sqlTable;
	
	
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
	
	


	public JobInf(Long jobId, String jobName, Integer jobStatus, String cronExpression, String beanClass,
			String description, String triggerName, Date createTime, Date updateTime, String indexPath, String sqlIp,
			String sqlUser, String sqlPw, String sqlDb, int sqlPort, String sqlTable) {
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
		this.indexPath = indexPath;
		this.sqlIp = sqlIp;
		this.sqlUser = sqlUser;
		this.sqlPw = sqlPw;
		this.sqlDb = sqlDb;
		this.sqlPort = sqlPort;
		this.sqlTable = sqlTable;
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



	public String getIndexPath() {
		return indexPath;
	}



	public void setIndexPath(String indexPath) {
		this.indexPath = indexPath;
	}



	public String getSqlIp() {
		return sqlIp;
	}



	public void setSqlIp(String sqlIp) {
		this.sqlIp = sqlIp;
	}



	public String getSqlUser() {
		return sqlUser;
	}



	public void setSqlUser(String sqlUser) {
		this.sqlUser = sqlUser;
	}



	public String getSqlPw() {
		return sqlPw;
	}



	public void setSqlPw(String sqlPw) {
		this.sqlPw = sqlPw;
	}



	public String getSqlDb() {
		return sqlDb;
	}



	public void setSqlDb(String sqlDb) {
		this.sqlDb = sqlDb;
	}



	public int getSqlPort() {
		return sqlPort;
	}



	public void setSqlPort(int sqlPort) {
		this.sqlPort = sqlPort;
	}



	public String getSqlTable() {
		return sqlTable;
	}



	public void setSqlTable(String sqlTable) {
		this.sqlTable = sqlTable;
	}



}