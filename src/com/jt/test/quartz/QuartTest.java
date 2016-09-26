package com.jt.test.quartz;

import java.util.Date;

import com.jt.bean.gateway.JobInf;
import com.jt.gateway.service.operation.IndexTask;
import com.jt.gateway.service.quartz.QuartzManager;

public class QuartTest {
public static void main(String[] args) {
//	System.out.println(JobParamUtil.getCronExpression("0", "1"));;
	
//	
	Date date=new Date();
	JobInf inf=new JobInf(1l,"测试",1,"0/5 * * * * ?",IndexTask.class.getName(),"",
			date,date);
	QuartzManager.removeJob(inf.getJobName());
	QuartzManager.addJob(inf);
	QuartzManager.startJobs();
	//QuartzManager.modifyJobTime(inf.getJobName(), "0/20 * * * * ?");
//	inf.setCronExpression("0/20 * * * * ?");
//	System.out.println(QuartTest.class.getResource("/").getFile());
}
}
