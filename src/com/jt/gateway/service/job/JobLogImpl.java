package com.jt.gateway.service.job;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.jt.bean.gateway.JobLog;

public class JobLogImpl extends BasicServicveImpl implements JobLogService {
	private static Logger logger= Logger.getLogger(JobLogImpl.class) ;
	@Override
	public List<JobLog> getAllLogs() {
		List<JobLog> list=new ArrayList<JobLog>();
		list=this.dao.query("from com.jt.bean.gateway.JobLog");
		return list;
	}
	
	public JobLog getLog(long jobId){
		return (JobLog) queryById(JobLog.class, jobId);
	}
	
	public void saveLog(JobLog log){
		super.saveOrUpdate(log);
	}
	
	
}
