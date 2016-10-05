package com.jt.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jdom.JDOMException;

import com.jt.bean.gateway.DataField;
import com.jt.bean.gateway.GwConfig;
import com.jt.gateway.service.operation.GwConfigService;

public class ConfigTest {
	public static void main(String[] args) throws JDOMException, IOException {
		File file=new File("D:\\apache-tomcat-8.0.24\\webapps\\QASystem\\WEB-INF\\classes\\gateway.conf");
		GwConfigService service=new GwConfigService(file);
		GwConfig config=service.getConfig("测试任务2");
//		List<DataField> list=config.getList();
//		for(DataField df:list){
//			System.out.println(df.getName());
//		}
		System.out.println(config.getIdName());
	}
}
