package com.jt.gateway.service.job;

import java.util.Map;

import com.jt.bean.gateway.JobLog;

public interface JobStatusService {
	public void startJob(long jobId);
	public void endJob(JobLog log);
	public Integer getJobStatus(long jobId);
	public void setJobStatus(long jobId,int jobStatus) ;
	public JobLogImpl getLogSerivce();
	public void setLogSerivce(JobLogImpl logSerivce);
	public Map<Long, Integer> getJobStatusMap() ;
	public void setJobStatusMap(Map<Long, Integer> jobStatusMap);
}
