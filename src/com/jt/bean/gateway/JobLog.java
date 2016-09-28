package com.jt.bean.gateway;

import java.util.Date;

public class JobLog {
	private long jobId;
	private int status;
	private Date exeTime;
	private int indexSize;

	public long getJobId() {
		return jobId;
	}
	public void setJobId(long jobId) {
		this.jobId = jobId;
	}

	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	public Date getExeTime() {
		return exeTime;
	}
	public void setExeTime(Date exeTime) {
		this.exeTime = exeTime;
	}
	public int getIndexSize() {
		return indexSize;
	}
	public void setIndexSize(int indexSize) {
		this.indexSize = indexSize;
	}
	
}
