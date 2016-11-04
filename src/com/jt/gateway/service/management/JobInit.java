package com.jt.gateway.service.management;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.jt.bean.gateway.JobInf;
import com.jt.gateway.service.job.JobInfService;

/**
 * 系统启动时需要调用的函数，启动jobinf表中所有状态不是-1的任务
 * 
 * @author zhengxiaobin
 *
 */
@Service

public class JobInit {
	private static Logger logger = Logger.getLogger(JobInit.class);
	private JobInfService jobService;

	public JobInfService getJobService() {
		return jobService;
	}

	public void setJobService(JobInfService jobService) {
		this.jobService = jobService;
	}

	public void init() {

		List<JobInf> list = jobService.getAllJobs();
		for (JobInf job : list) {
			logger.info("尝试启动任务 jobName=[" + job.getJobName() + "] jobid=[" + job.getJobId() + "] status=["
					+ job.getJobStatus() + "]");
			if (job.getJobStatus() == 2) {
				logger.info("jobName=[" + job.getJobName() + "] 上次未正常结束，修改状态并启动");
				jobService.setJobStatus(job.getJobId(), 1);
			}
			try {
				logger.info("启动任务：jobName=[" + job.getJobName() + "] jobId=[" + job.getJobId() + "]");
				jobService.startSimJob(job.getJobId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
