package com.jt.test;

import java.io.File;
import java.io.IOException;

import org.jdom.JDOMException;

import com.jt.bean.gateway.GwConfig;
import com.jt.gateway.service.operation.GwConfigService;
import com.jt.gateway.util.FileUtil;


public class Temp {
public static void main(String[] args) throws Exception {
//	GwConfigService service=new GwConfigService(new File("D:\\wd文档\\网脉开发\\eclipse\\gateway.conf"));
//	GwConfig config=service.getConfig("智能问答数据抽取");
//	
//	
//	GwConfigService service2=new GwConfigService(new File("D:\\wd文档\\网脉开发\\eclipse\\gateway2.conf"));
//	service2.addConfig(config);
	File file=new File("D:\\wd文档\\网脉开发\\eclipse\\gateway2.conf");
	System.out.println(FileUtil.isContentNull(file));
}
}
