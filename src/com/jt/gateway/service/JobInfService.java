package com.jt.gateway.service;

import java.util.List;

import com.jt.bean.gateway.JobInf;

public interface JobInfService {
	
	public List<JobInf> getAllJobs();
	public void addTask(JobInf jobInf);
	public void deleteTask(Long id);
	
	public void updateTask(JobInf jobInf) ;

	public void startAllJob();
	public void stopAllJob() ;
	public void stopJob(Long id);
	public int repeatName(String name) ;
	public int repeatTrigger(String trigger);
	public void startSimJob(Long id);

}
