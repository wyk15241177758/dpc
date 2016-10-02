package com.jt.gateway.service.job;

import java.util.List;

import com.jt.bean.gateway.JobInf;

public interface JobInfService {
	
	public List<JobInf> getAllJobs();
	public void addTask(JobInf jobInf);
	public void deleteTask(Long id);
	
	public void updateTask(JobInf jobInf) ;

//	public void startAllJob();
//	public void stopAllJob() ;
	public void stopJob(Long id);
	public int repeatName(String name) ;
	public int repeatTrigger(String trigger);
	public void startSimJob(Long id)throws Exception;
	public JobInf getJobByName(String name);
	public JobInf getJobById(long jobId);
	public List<JobInf> getJobsByIds(String ids);
	public void setJobStatus(Long id,int status);
}
