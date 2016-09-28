package com.jt.gateway.service.job;


import com.jt.bean.gateway.JobLog;

public class JobStatusImpl {
	//停止状态为2，正在执行为1
	private int jobStatus=2;
	private JobLogImpl logSerivce;
	public void startJob(){
		jobStatus=1;
	}
	
	public void endJob(JobLog log){
		jobStatus=2;
		if(logSerivce.getLog(log.getJobId())==null){
			logSerivce.save(log);
		}else{
			logSerivce.update(log);
		}
	}
	public int getJobStatus() {
		return jobStatus;
	}
	public void setJobStatus(int jobStatus) {
		this.jobStatus = jobStatus;
	}
	public JobLogImpl getLogSerivce() {
		return logSerivce;
	}
	public void setLogSerivce(JobLogImpl logSerivce) {
		this.logSerivce = logSerivce;
	}
}
