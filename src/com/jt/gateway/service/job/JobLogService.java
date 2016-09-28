package com.jt.gateway.service.job;

import java.util.List;

import com.jt.bean.gateway.JobLog;

public interface JobLogService {
	
	public List<JobLog> getAllLogs();
	public JobLog getLog(long jobId);
	

}
