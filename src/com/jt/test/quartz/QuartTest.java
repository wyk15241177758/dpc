package com.jt.test.quartz;

import java.sql.Timestamp;

import com.jt.bean.gateway.JobInf;
import com.jt.gateway.service.management.util.JobParamUtil;
import com.jt.gateway.service.operation.IndexTask;
import com.jt.gateway.service.quartz.QuartzManager;

public class QuartTest {
public static void main(String[] args) {
	System.out.println(JobParamUtil.getCronExpression("0", "1"));;
	
//	
//	JobInf inf=new JobInf(1l,"智能问答数据抽取",1,"0/5 * * * * ?",IndexTask.class.getName(),"0/5 * * * * ?",
//			new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()));
//	QuartzManager.removeJob(inf.getJobName(), inf.getJobGroup(), inf.getTriggerName(), inf.getTriggerGroupName());
//	QuartzManager.addJob(inf);
//	QuartzManager.startJobs();
}
}
