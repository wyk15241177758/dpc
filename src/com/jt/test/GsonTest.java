package com.jt.test;


import java.io.IOException;
import java.util.Date;

import org.jdom.JDOMException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jt.bean.gateway.JobLog;
import com.jt.bean.gateway.PageMsg;


public class GsonTest {
public static void main(String[] args) throws JDOMException, IOException {
	Gson gson= new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	JobLog log=new JobLog();
	log.setEnd(new Date());
	log.setExeTime(1000l);
	log.setIndexSize(1000);
	log.setJobId(12l);
	log.setStart(new Date());
	log.setStatus(1);
	PageMsg msg=new PageMsg();
	msg.setMsg(null);
	msg.setSig(true);
	System.out.println(gson.toJson(msg));
}
}
