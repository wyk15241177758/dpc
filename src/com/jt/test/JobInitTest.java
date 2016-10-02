package com.jt.test;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.jt.bean.gateway.JobInf;
import com.jt.gateway.service.job.JobInfService;

/**
 * 系统启动时需要调用的函数，启动jobinf表中所有状态不是-1的任务
 * @author zhengxiaobin
 *
 */
public class JobInitTest {
	private static Logger logger =Logger.getLogger(JobInitTest.class);

	private JobInfService  jobService;
	public JobInfService getJobService() {
		return jobService;
	}
	
	@Resource(name="jobInfImpl") 
	public void setJobService(JobInfService jobService) {
		this.jobService = jobService;
	}
	public void init(){
		System.out.println("!!!!!!!!!!!!!!!!!!service null is "+(jobService==null));
//		List<JobInf> list=jobService.getAllJobs();
//		for(JobInf job:list){
//			if(job.getJobStatus()!=-1){
//				try {
//					logger.info("启动任务：jobName=["+job.getJobName()+"] jobId=["+job.getJobId()+"]");
//					//jobService.startSimJob(job.getJobId());
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
	}
}
