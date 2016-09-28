package com.jt.gateway.service.job;

import com.jt.bean.gateway.JobLog;

public interface JobStatusService {
	public void startJob();
	public void endJob(JobLog log);
	public int getJobStatus();

}
