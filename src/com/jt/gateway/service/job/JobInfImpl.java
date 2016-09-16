package com.jt.gateway.service.job;


import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.base.page.Param;
import com.jt.bean.gateway.JobInf;
import com.jt.gateway.service.BasicServicveImpl;
import com.jt.gateway.service.JobInfService;


public class JobInfImpl extends BasicServicveImpl  implements  JobInfService{

	/**
	 * 获得所有 任务
	 */
	public List<JobInf> getAllJobs(){
		List<JobInf> list=new ArrayList<JobInf>();
		list=this.dao.query("from com.jt.bean.gateway.JobInf");
		return list;
	}
	
	
	/**
	 * 添加任务
	 */
	public void addTask(JobInf jobInf) {
		try{
		Date create = new Date();
		Timestamp time = new Timestamp(create.getTime());
		jobInf.setCreateTime(time);
		jobInf.setUpdateTime(time);
		jobInf.setJobStatus(1);
		this.dao.save(jobInf);}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 删除任务
	 */
	public void deleteTask(Long id) {
		stopJob( id);
		String hql = "update com.jt.bean.gateway.JobInf set jobStatus = ? where jobId = ?";
		List<Param>  paramList=new  ArrayList<Param>();
		paramList.add(new Param(Types.INTEGER,-1));
		paramList.add(new Param(Types.BIGINT,id));
		this.dao.update(hql, paramList);
		
	}
	/**
	 * 更新任务
	 */
	public void updateTask(JobInf jobInf) {
		stopJob(jobInf.getJobId());
		List<Param>  paramList=new  ArrayList<Param>();
		Date change = new Date();
		Timestamp time = new Timestamp(change.getTime());
		jobInf.setUpdateTime(time);
		String hql = "update com.jt.bean.gateway.JobInf set jobName = ? , jobGroup = ? , jobStatus = ? , cronExpression = ? , beanClass  = ?" +
				" ,  description  = ?  , triggerName  = ?  , triggerGroupName  = ? , updateTime  = ? " +
				"where jobId = ?";
		paramList.add(new Param(Types.VARCHAR,jobInf.getJobName()));
		paramList.add(new Param(Types.VARCHAR,jobInf.getJobGroup()));
		paramList.add(new Param(Types.INTEGER,jobInf.getJobStatus()));
		paramList.add(new Param(Types.VARCHAR,jobInf.getCronExpression()));
		paramList.add(new Param(Types.VARCHAR,jobInf.getBeanClass()));
		paramList.add(new Param(Types.VARCHAR,jobInf.getDescription()));
		paramList.add(new Param(Types.VARCHAR,jobInf.getTriggerName()));
		paramList.add(new Param(Types.VARCHAR,jobInf.getTriggerGroupName()));
		paramList.add(new Param(Types.TIMESTAMP,time));
		paramList.add(new Param(Types.BIGINT,jobInf.getJobId()));
		this.dao.update(hql, paramList);
		}

	public void startAllJob() {
		String hql = "from com.jt.bean.gateway.JobInf where jobStatus = 1";
		@SuppressWarnings("unchecked")
		List<JobInf> list = this.dao.query(hql);
		for (int i = 0; i < list.size(); i++) {
			QuartzManager.addJob(list.get(i));
		}
		QuartzManager.startJobs();
	}
	/**
	 * 停止所有任务
	 */
	public void stopAllJob() {
		String hql = "from com.jt.bean.gateway.JobInf where jobStatus = 2";
		@SuppressWarnings("unchecked")
		List<JobInf> list = this.dao.query(hql);
		for (int i = 0; i < list.size(); i++) {
			JobInf inf=list.get(i);
			QuartzManager.removeJob(inf.getJobName(), inf.getJobGroup(), inf.getTriggerName(), inf.getTriggerGroupName());
		}
		QuartzManager.shutdownJobs();
	}
	/**
	 * 停止单个任务
	 */
	public void stopJob(Long id) {
		
		String hql = " from com.jt.bean.gateway.JobInf where jobId = ?";
		List<Param>  paramList=new  ArrayList<Param>();
		paramList.add(new Param(Types.BIGINT,id));
		List<JobInf> list = this.dao.query(hql, paramList);
		String triggerName = list.get(0).getTriggerName();
		String triggerGroupName = list.get(0).getTriggerGroupName();
		String jobName = list.get(0).getJobName();
		String jobGroupName = list.get(0).getJobGroup();
		try {
			QuartzManager.removeJob(jobName, jobGroupName, triggerName, triggerGroupName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 查询名字重复
	 */
	@SuppressWarnings("unchecked")
	public int repeatName(String name) {
		String hql = "from com.jt.bean.gateway.JobInf where jobName = ?";
		List<Param>  paramList=new  ArrayList<Param>();
		paramList.add(new Param(Types.VARCHAR,name));
		List<JobInf> list = this.dao.query(hql, paramList);
		return list.size();
	}
	/**
	 * 查询触发器名重复
	 */
	@SuppressWarnings("unchecked")
	public int repeatTrigger(String trigger) {
		String hql = "from com.jt.bean.gateway.JobInf where  triggerName = ?";
		List<Param>  paramList=new  ArrayList<Param>();
		paramList.add(new Param(Types.VARCHAR,trigger));
		List<JobInf> list = this.dao.query(hql, paramList);
		return list.size();
	}
	public void startSimJob(Long id) {
		JobInf inf=(JobInf) this.dao.queryById(JobInf.class, id);
		if(inf.getJobStatus()!=1)
			return;
		QuartzManager.removeJob(inf.getJobName(), inf.getJobGroup(), inf.getTriggerName(), inf.getTriggerGroupName());
		QuartzManager.addJob(inf);
		QuartzManager.startJobs();
	}
	
	
}
