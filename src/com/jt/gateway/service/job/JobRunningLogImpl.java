package com.jt.gateway.service.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 内存级别日志，记录指定任务的一次执行的日志，任务执行结束之后清空其内存日志
 * @author zhengxiaobin
 *
 */
public class JobRunningLogImpl implements JobRunningLogService{
	private Map<Long,List<String>> logMap;
	public JobRunningLogImpl(){
		logMap=new HashMap<Long,List<String>>();
	}
	
	public Map<Long, List<String>> getLogMap() {
		return logMap;
	}

	public void setLogMap(Map<Long, List<String>> logMap) {
		this.logMap = logMap;
	}

	public void clearRunningLog(long jobId){
		if(logMap.get(jobId)!=null){
			logMap.remove(jobId);
		}
	}
	
	public List<String> getRunningLog(long jobId){
		return logMap.get(jobId);
	}
	
	public void addRunningLog(long jobId,String str){
		List runningLog=logMap.get(jobId);
		if(runningLog==null){
			runningLog=new ArrayList<String>();
		}
		runningLog.add(str);
		logMap.put(jobId, runningLog);
	}
}
