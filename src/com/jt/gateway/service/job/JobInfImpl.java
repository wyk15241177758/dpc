package com.jt.gateway.service.job;


import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.jt.base.page.Param;
import com.jt.bean.gateway.JobInf;
import com.jt.gateway.service.quartz.QuartzManager;


public class JobInfImpl extends BasicServicveImpl  implements  JobInfService{
	private static Logger logger= Logger.getLogger(JobInfImpl.class) ;

	public void startImmediateJob(JobInf job) throws Exception{
		QuartzManager.startImmediateJob(job);
	}
	
	
	/**
	 * 获得所有 任务
	 */
	public List<JobInf> getAllJobs(){
		List<JobInf> list=new ArrayList<JobInf>();
		list=this.dao.query("from com.jt.bean.gateway.JobInf");
		return list;
	}
	
	/**
	 * 获得所有 任务，根据指定条件过滤
	 */
	public List<JobInf> getAllJobs(String where){
		List<JobInf> list=new ArrayList<JobInf>();
		list=this.dao.query("from com.jt.bean.gateway.JobInf where "+where);
		return list;
	}
	
	/**
	 * 获得某些任务
	 */
	public List<JobInf> getJobsByIds(String ids){
		List<JobInf> list=new ArrayList<JobInf>();
		list=this.dao.query("from com.jt.bean.gateway.JobInf where jobId in ("+ids+")");
		return list;
	}
	
	/**
	 * 获得某个任务
	 */
	public JobInf getJobByName(String name){
		List<JobInf> list=new ArrayList<JobInf>();
		list=this.dao.query("from com.jt.bean.gateway.JobInf where jobName='"+name+"'");
		if(list.size()!=0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * 获得某个任务
	 */
	public JobInf getJobById(long jobId){
		JobInf inf=null;
		inf=(JobInf)queryById(JobInf.class, jobId);
		return inf;
	}
	
	/**
	 * 添加任务
	 */
	public void addTask(JobInf jobInf) {
		try{
			jobInf.setJobStatus(1);
			this.dao.save(jobInf);
			startSimJob(jobInf.getJobId());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 删除任务
	 */
	public void deleteTask(JobInf job) {
		stopJob( job.getJobId());
		this.dao.delete(job);
		
	}
	/**
	 * 更新任务，更新之后再启动任务
	 */
	public void updateTask(JobInf jobInf) {
		try {
			stopJob(jobInf.getJobId());
		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}
//		List<Param>  paramList=new  ArrayList<Param>();
//		Date change = new Date();
//		Timestamp time = new Timestamp(change.getTime());
//		jobInf.setUpdateTime(time);
//		String hql = "update com.jt.bean.gateway.JobInf set jobName = ? , jobGroup = ? , jobStatus = ? , cronExpression = ? , beanClass  = ?" +
//				" ,  description  = ?  , triggerName  = ?  , triggerGroupName  = ? , updateTime  = ? ,indexPath = ? ,sqlIp = ? ,sqlUser = ? ,sqlPw = ? ,sqlDb = ? ,sqlPort = ? ,sqlTable = ? " +
//				"where jobId = ?";
//		paramList.add(new Param(Types.VARCHAR,jobInf.getJobName()));
//		paramList.add(new Param(Types.VARCHAR,jobInf.getJobGroup()));
//		paramList.add(new Param(Types.INTEGER,jobInf.getJobStatus()));
//		paramList.add(new Param(Types.VARCHAR,jobInf.getCronExpression()));
//		paramList.add(new Param(Types.VARCHAR,jobInf.getBeanClass()));
//		paramList.add(new Param(Types.VARCHAR,jobInf.getDescription()));
//		paramList.add(new Param(Types.VARCHAR,jobInf.getTriggerName()));
//		paramList.add(new Param(Types.VARCHAR,jobInf.getTriggerGroupName()));
//		paramList.add(new Param(Types.TIMESTAMP,time));
//		paramList.add(new Param(Types.BIGINT,jobInf.getJobId()));
//		
//		paramList.add(new Param(Types.VARCHAR,jobInf.getIndexPath()));
//		paramList.add(new Param(Types.VARCHAR,jobInf.getSqlIp()));
//		paramList.add(new Param(Types.VARCHAR,jobInf.getSqlUser()));
//		paramList.add(new Param(Types.VARCHAR,jobInf.getSqlPw()));
//		paramList.add(new Param(Types.VARCHAR,jobInf.getSqlDb()));
//		paramList.add(new Param(Types.INTEGER,jobInf.getSqlPort()));
//		paramList.add(new Param(Types.VARCHAR,jobInf.getSqlTable()));
		
		this.dao.update(jobInf);;
		try {
			startSimJob(jobInf.getJobId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		}

//	public void startAllJob() {
//		String hql = "from com.jt.bean.gateway.JobInf where jobStatus = 1";
//		@SuppressWarnings("unchecked")
//		List<JobInf> list = this.dao.query(hql);
//		for (int i = 0; i < list.size(); i++) {
//			QuartzManager.addJob(list.get(i));
//		}
//		QuartzManager.startJobs();
//	}
	/**
	 * 停止所有任务
	 */
//	public void stopAllJob() {
//		String hql = "from com.jt.bean.gateway.JobInf where jobStatus = 2";
//		@SuppressWarnings("unchecked")
//		List<JobInf> list = this.dao.query(hql);
//		for (int i = 0; i < list.size(); i++) {
//			JobInf inf=list.get(i);
//			QuartzManager.removeJob(inf.getJobName(), inf.getJobGroup(), inf.getTriggerName(), inf.getTriggerGroupName());
//		}
//		QuartzManager.shutdownJobs();
//	}
	/**
	 * 停止单个任务，仅从quartzmanager中移除。修改状态为1
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
		//修改状态
		String hql2 = "update com.jt.bean.gateway.JobInf set jobStatus = ? where jobId = ?";
		paramList=new  ArrayList<Param>();
		paramList.add(new Param(Types.INTEGER,1));
		paramList.add(new Param(Types.BIGINT,id));
		this.dao.update(hql2, paramList);
		
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
	
	//开始时将状态置为1，结束后将状态置为2。在IndexTask中控制
	public void startSimJob(Long id) throws Exception {
		JobInf inf=getJobById(id);
		if(inf.getJobStatus()!=1){
			logger.info("任务正在运行或已删除");
			throw new Exception("任务正在运行或已删除");
		}
		QuartzManager.removeJob(inf.getJobName(), inf.getJobGroup(), inf.getTriggerName(), inf.getTriggerGroupName());
		QuartzManager.addJob(inf);
		QuartzManager.startJobs();
		
//		String hql2 = "update com.jt.bean.gateway.JobInf set jobStatus = ? where jobId = ?";
//		List<Param>  paramList=new  ArrayList<Param>();
//		paramList.add(new Param(Types.INTEGER,2));
//		paramList.add(new Param(Types.BIGINT,id));
//		this.dao.update(hql2, paramList);
	}
	
	public void setJobStatus(Long id,int status){
		JobInf inf=getJobById(id);
		String hql2 = "update com.jt.bean.gateway.JobInf set jobStatus = ? where jobId = ?";
		List<Param>  paramList=new  ArrayList<Param>();
		paramList.add(new Param(Types.INTEGER,status));
		paramList.add(new Param(Types.BIGINT,id));
		this.dao.update(hql2, paramList);
	}
	//获得指定job关联的
	
}
