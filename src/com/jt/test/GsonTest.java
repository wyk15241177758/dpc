package com.jt.test;


import java.io.IOException;

import org.jdom.JDOMException;

import com.google.gson.Gson;
import com.jt.bean.gateway.PageMsg;
import com.jt.gateway.service.operation.GwConfigService;

public class GsonTest {
public static void main(String[] args) throws JDOMException, IOException {
	GwConfigService service=new GwConfigService();
	PageMsg msg=new PageMsg();
	msg.setMsg("msg content");
	msg.setSig(false);
	Gson gson=new Gson();
	System.out.println(gson.toJson(service.getConfig("智能问答数据抽取").getList()));
	
}
}
