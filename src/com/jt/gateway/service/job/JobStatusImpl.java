package com.jt.gateway.service.job;


import java.util.HashMap;
import java.util.Map;

import com.jt.bean.gateway.JobLog;

public class JobStatusImpl implements JobStatusService{
	private JobLogImpl logSerivce;
	//jobID,jobStatus停止状态为2，正在执行为1
	private Map<Long,Integer> jobStatusMap;
	public JobStatusImpl(){
		jobStatusMap=new HashMap<Long,Integer>();
	}
	public void startJob(long jobId){
		jobStatusMap.put(jobId, 1);
	}
	
	public void endJob(JobLog log){
		jobStatusMap.put(log.getJobId(), 2);
		if(logSerivce.getLog(log.getJobId())==null){
			logSerivce.save(log);
		}else{
			logSerivce.update(log);
		}
	}
	public Integer getJobStatus(long jobId) {
		return jobStatusMap.get(jobId);
	}
	public void setJobStatus(long jobId,int jobStatus) {
		jobStatusMap.put(jobId, jobStatus);
	}
	
	public JobLogImpl getLogSerivce() {
		return logSerivce;
	}
	public void setLogSerivce(JobLogImpl logSerivce) {
		this.logSerivce = logSerivce;
	}
	public Map<Long, Integer> getJobStatusMap() {
		return jobStatusMap;
	}
	public void setJobStatusMap(Map<Long, Integer> jobStatusMap) {
		this.jobStatusMap = jobStatusMap;
	}


	
}
