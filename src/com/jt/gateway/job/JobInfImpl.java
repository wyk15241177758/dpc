package com.jt.gateway.job;


import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jt.bean.gateway.JobInf;




public class JobInfImpl {
	
	
	/**
	 * 添加任务
	 */
	public void addTask(JobInf jobInf) {
		Date create = new Date();
		Timestamp time = new Timestamp(create.getTime());
		jobInf.setCreateTime(time);
		jobInf.setUpdateTime(time);
		jobInf.setJobStatus(1);
		this.dao.save(jobInf);
		
		JobAddControl jobAddControl = new JobAddControl();
		jobAddControl.setAddTime(time);
		jobAddControl.setChangeTime(time);
		jobAddControl.setJobInf(jobInf);
		jobAddControl.setState(-1);
		this.dao.save(jobAddControl);
		
		
		JobFpkzNum fpkzNum=new JobFpkzNum();
		fpkzNum.setBeginNum(0L);
		fpkzNum.setBfallNum(0L);
		fpkzNum.setJobInf(jobInf);
		this.dao.save(fpkzNum);
		
		
		
		
	}
	/**
	 * 删除任务
	 */
	@Override
	public void deleteTask(Long id) {
		stopJob( id);
		String hql = "update com.trs.cloud.job.bean.JobInf set jobStatus = ? where jobId = ?";
		List<Param>  paramList=new  ArrayList<Param>();
		paramList.add(new Param(Types.INTEGER,-1));
		paramList.add(new Param(Types.BIGINT,id));
		this.dao.update(hql, paramList);
		
	}
	/**
	 * 更新任务
	 */
	@Override
	public void updateTask(JobInf jobInf) {
		stopJob(jobInf.getJobId());
		List<Param>  paramList=new  ArrayList<Param>();
		Date change = new Date();
		Timestamp time = new Timestamp(change.getTime());
		jobInf.setUpdateTime(time);
		String hql = "update com.trs.cloud.job.bean.JobInf set jobName = ? , jobGroup = ? , jobStatus = ? , cronExpression = ? , beanClass  = ?" +
				" ,  orgname = ?  , description  = ?  , triggerName  = ?  , triggerGroupName  = ?  , type  = ?  , host  = ? " +
				" ,  port = ?  , user  = ?  , passwd  = ?  , savePath  = ? ,  localPath = ?  , prefix  = ?  , desPasswd  = ?  , dataType  = ?" +
				" ,  okPrefix = ?  , updateTime  = ? ,isAuto = ? " +
				"where jobId = ?";
		paramList.add(new Param(Types.VARCHAR,jobInf.getJobName()));
		paramList.add(new Param(Types.VARCHAR,jobInf.getJobGroup()));
		paramList.add(new Param(Types.INTEGER,jobInf.getJobStatus()));
		paramList.add(new Param(Types.VARCHAR,jobInf.getCronExpression()));
		paramList.add(new Param(Types.VARCHAR,jobInf.getBeanClass()));
		paramList.add(new Param(Types.VARCHAR,jobInf.getOrgname()));
		paramList.add(new Param(Types.VARCHAR,jobInf.getDescription()));
		paramList.add(new Param(Types.VARCHAR,jobInf.getTriggerName()));
		paramList.add(new Param(Types.VARCHAR,jobInf.getTriggerGroupName()));
		paramList.add(new Param(Types.INTEGER,jobInf.getType()));
		paramList.add(new Param(Types.VARCHAR,jobInf.getHost()));
		paramList.add(new Param(Types.VARCHAR,jobInf.getPort()));
		paramList.add(new Param(Types.VARCHAR,jobInf.getUser()));
		paramList.add(new Param(Types.VARCHAR,jobInf.getPasswd()));
		paramList.add(new Param(Types.VARCHAR,jobInf.getSavePath()));
		paramList.add(new Param(Types.VARCHAR,jobInf.getLocalPath()));
		paramList.add(new Param(Types.VARCHAR,jobInf.getPrefix()));
		paramList.add(new Param(Types.VARCHAR,jobInf.getDesPasswd()));
		paramList.add(new Param(Types.INTEGER,jobInf.getDataType()));
		paramList.add(new Param(Types.VARCHAR,jobInf.getOkPrefix()));
		paramList.add(new Param(Types.TIMESTAMP,time));
		paramList.add(new Param(Types.INTEGER,jobInf.getIsAuto()));
		paramList.add(new Param(Types.BIGINT,jobInf.getJobId()));
		this.dao.update(hql, paramList);
		}

	@Override
	public void startAllJob() {
		String hql = "from com.trs.cloud.job.bean.JobInf where jobStatus = 1";
		@SuppressWarnings("unchecked")
		List<JobInf> list = this.dao.query(hql);
		for (int i = 0; i < list.size(); i++) {
			QuartzManager.addJob(list.get(i));
			CustJdbcService.updateDsJob(2, list.get(i).getJobId());
		}
		QuartzManager.startJobs();
		Map session = ActionContext.getContext().getSession();	
		SysUser user =(SysUser) session.get("user");
		HttpServletRequest request = (HttpServletRequest)ActionContext.getContext().get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);  
		String ip = getIpAddr(request);
		String loginName = user.getLoginName();
		OperationTaskLog operationTaskLog = new OperationTaskLog();
		operationTaskLog.setIp(ip);
		operationTaskLog.setLoginName(loginName);
		operationTaskLog.setTimes(new Date());
		operationTaskLog.setType(2);//2启动全部任务
		this.dao.save(operationTaskLog);
	}
	/**
	 * 停止所有任务
	 */
	@Override
	public void stopAllJob() {
		String hql = "from com.trs.cloud.job.bean.JobInf where jobStatus = 2";
		@SuppressWarnings("unchecked")
		List<JobInf> list = this.dao.query(hql);
		for (int i = 0; i < list.size(); i++) {
			JobInf inf=list.get(i);
			QuartzManager.removeJob(inf.getJobName(), inf.getJobGroup(), inf.getTriggerName(), inf.getTriggerGroupName());
			CustJdbcService.updateDsJob(1, inf.getJobId());
		}
		
		
		QuartzManager.shutdownJobs();
		Map session = ActionContext.getContext().getSession();	
		SysUser user =(SysUser) session.get("user");
		HttpServletRequest request = (HttpServletRequest)ActionContext.getContext().get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);  
		String ip = getIpAddr(request);
		String loginName = user.getLoginName();
		OperationTaskLog operationTaskLog = new OperationTaskLog();
		operationTaskLog.setIp(ip);
		operationTaskLog.setLoginName(loginName);
		operationTaskLog.setTimes(new Date());
		operationTaskLog.setType(3);//3停止全部任务
		this.dao.save(operationTaskLog);
	}
	/**
	 * 停止单个任务
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void stopJob(Long id) {
		
		String hql = " from com.trs.cloud.job.bean.JobInf where jobId = ?";
		List<Param>  paramList=new  ArrayList<Param>();
		paramList.add(new Param(Types.BIGINT,id));
		List<JobInf> list = this.dao.query(hql, paramList);
		String triggerName = list.get(0).getTriggerName();
		String triggerGroupName = list.get(0).getTriggerGroupName();
		String jobName = list.get(0).getJobName();
		String jobGroupName = list.get(0).getJobGroup();
		try {
			QuartzManager.removeJob(jobName, jobGroupName, triggerName, triggerGroupName);
			CustJdbcService.updateDsJob(1, id);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Map session = ActionContext.getContext().getSession();	
		SysUser user =(SysUser) session.get("user");
		HttpServletRequest request = (HttpServletRequest)ActionContext.getContext().get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);  
		String ip = getIpAddr(request);
		String loginName = user.getLoginName();
		OperationTaskLog operationTaskLog = new OperationTaskLog();
		operationTaskLog.setIp(ip);
		operationTaskLog.setLoginName(loginName);
		operationTaskLog.setTimes(new Date());
		operationTaskLog.setType(0);//1停止单个任务
		JobInf jobInf = new JobInf();
		jobInf.setJobId(id);
		operationTaskLog.setJobInf(jobInf);
		this.dao.save(operationTaskLog);
	}
	/**
	 * 查询名字重复
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int repeatName(String name) {
		String hql = "from com.trs.cloud.job.bean.JobInf where jobName = ?";
		List<Param>  paramList=new  ArrayList<Param>();
		paramList.add(new Param(Types.VARCHAR,name));
		List<JobInf> list = this.dao.query(hql, paramList);
		return list.size();
	}
	/**
	 * 查询触发器名重复
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int repeatTrigger(String trigger) {
		String hql = "from com.trs.cloud.job.bean.JobInf where  triggerName = ?";
		List<Param>  paramList=new  ArrayList<Param>();
		paramList.add(new Param(Types.VARCHAR,trigger));
		List<JobInf> list = this.dao.query(hql, paramList);
		return list.size();
	}
	@Override
	public void startSimJob(Long id) {
		JobInf inf=(JobInf) this.dao.queryById(JobInf.class, id);
		if(inf.getJobStatus()!=1)
			return;
		CustJdbcService.updateDsJob(2, id);
		QuartzManager.removeJob(inf.getJobName(), inf.getJobGroup(), inf.getTriggerName(), inf.getTriggerGroupName());
		QuartzManager.addJob(inf);
		QuartzManager.startJobs();
		
		Map session = ActionContext.getContext().getSession();	
		SysUser user =(SysUser) session.get("user");
		HttpServletRequest request = (HttpServletRequest)ActionContext.getContext().get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);  
		String ip = getIpAddr(request);
		String loginName = user.getLoginName();
		OperationTaskLog operationTaskLog = new OperationTaskLog();
		operationTaskLog.setIp(ip);
		operationTaskLog.setLoginName(loginName);
		operationTaskLog.setTimes(new Date());
		operationTaskLog.setType(1);//1启动单个任务
		JobInf jobInf = new JobInf();
		jobInf.setJobId(id);
		operationTaskLog.setJobInf(jobInf);
		this.dao.save(operationTaskLog);
	}
	public String getIpAddr(HttpServletRequest request) {
		 String ip = request.getHeader("x-forwarded-for");
		    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
		        ip = request.getHeader("Proxy-Client-IP");
		    }
		    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
		        ip = request.getHeader("WL-Proxy-Client-IP");
		    }
		    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
		        ip = request.getRemoteAddr();
		    }
		    return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
		    }
}
