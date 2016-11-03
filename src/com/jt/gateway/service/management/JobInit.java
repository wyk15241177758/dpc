package com.jt.gateway.service.management;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import com.jt.bean.gateway.GwConfig;
import com.jt.bean.gateway.JobInf;
import com.jt.gateway.service.job.JobInfService;
import com.jt.gateway.service.operation.GwConfigService;

/**
 * 系统启动时需要调用的函数，启动jobinf表中所有状态不是-1的任务
 * @author zhengxiaobin
 *
 */
@Service

public class JobInit {
	private static Logger logger =Logger.getLogger(JobInit.class);

	private JobInfService  jobService;
	private GwConfigService configService;
	public JobInit(){
		try {
			configService=new GwConfigService();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public JobInfService getJobService() {
		return jobService;
	}
	
	@Resource(name="jobInfImpl") 
	public void setJobService(JobInfService jobService) {
		this.jobService = jobService;
	}
public void init(){
		
		List<JobInf> list=jobService.getAllJobs();
		GwConfig config=null;
		for(JobInf job:list){
			logger.info("尝试启动任务 jobName=["+job.getJobName()+"] jobid=["+job.getJobId()+"] status=["+job.getJobStatus()+"]");
			config=configService.getConfig(job.getJobName());
//			if(config==null){
//				logger.info("jobName=["+job.getJobName()+"] 没有找到对应的配置信息，删除此任务");
//				jobService.deleteTask(job,false);
//				continue;
//			}
//			
			if(job.getJobStatus()==2){
				logger.info("jobName=["+job.getJobName()+"] 上次未正常结束，修改状态并启动");
				jobService.setJobStatus(job.getJobId(), 2);
				//updatetask会启动任务，因此continue即可
				continue;
			}
			if(job.getJobStatus()==1){
				try {
					logger.info("启动任务：jobName=["+job.getJobName()+"] jobId=["+job.getJobId()+"]");
					jobService.startSimJob(job.getJobId()); 
				} catch (Exception e) {  
					e.printStackTrace(); 
				}
			}
		}
	}
}
