package com.jt.gateway.service.job;

import java.util.List;

public interface JobRunningLogService {
	public void clearRunningLog(long jobId);
	public void addRunningLog(long jobId,String str);
	public List<String> getRunningLog(long jobId);
}
