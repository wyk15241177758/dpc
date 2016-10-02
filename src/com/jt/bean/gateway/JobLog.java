package com.jt.bean.gateway;

import java.util.Date;

public class JobLog implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long jobId;
	private int status;
	private long exeTime;//执行时长，毫秒
	private Date start;
	private Date end;
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
	
	public long getExeTime() {
		return exeTime;
	}
	public void setExeTime(long exeTime) {
		this.exeTime = exeTime;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public int getIndexSize() {
		return indexSize;
	}
	public void setIndexSize(int indexSize) {
		this.indexSize = indexSize;
	}
	
}
