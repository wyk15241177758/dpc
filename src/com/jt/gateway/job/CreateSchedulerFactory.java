package com.jt.gateway.job;

import java.util.Properties;

import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

public class CreateSchedulerFactory {
	
	public static SchedulerFactory getFactory() {
		SchedulerFactory factory=null;
		   Properties props = new Properties();
		    props.put(StdSchedulerFactory.PROP_THREAD_POOL_CLASS,
		            "org.quartz.simpl.SimpleThreadPool");
		    props.put("org.quartz.threadPool.threadCount", "250");       //任务线程个数
		    try {
				factory=new StdSchedulerFactory(props);
			} catch (SchedulerException e) {
				
				e.printStackTrace();
			}
		return factory;
	}

}
