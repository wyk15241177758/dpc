package com.jt.test;


import java.sql.Timestamp;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.bean.gateway.JobInf;
import com.jt.gateway.service.job.JobInfService;

@Controller
public class MVCMappingTest {
@RequestMapping(value="hello.do")
public String hello( ModelMap model){
	model.addAttribute("word","word");
	service.addTask(new JobInf(null, "≤‚ ‘»ŒŒÒ", "group", 1, "*", "com.jt.gateway.service.job.QuartzTest", "√Ë ˆ", "trigger", "triggerGroup", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
	return "hello";
}

private   JobInfService  service;
public JobInfService getService() {
	return service;
}
@Resource(name="jobInfImpl") 
public void setService(JobInfService service) {
	this.service = service;
}



}
