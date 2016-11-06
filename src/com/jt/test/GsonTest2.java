package com.jt.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jt.bean.gateway.GwField;
import com.jt.bean.gateway.JobInf;
import com.jt.bean.gateway.PageMsg;

public class GsonTest2 {
public static void main(String[] args) {
	Gson gson= new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	PageMsg msg=new PageMsg();
	JobInf job=new JobInf(0l, "jobName", 
			1, "11.11.11", "className", "", "triggerName", new Date(), new Date(), 
			"indexPath", "127.0.0.1","root", "password", "sqlDb",3306,"table");
	GwField field=new GwField();
	field.setFieldId(1);
	field.setJobId(1l);
	field.setKey(true);
	field.setName("name");
	field.setType("type");
	List<Object> list=new ArrayList<Object>();
	list.add(job);
	list.add(field);
	msg.setMsg(list);
	System.out.println(gson.toJson(msg));
}
}
