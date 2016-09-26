package com.jt.test;

import java.io.IOException;
import java.util.List;

import org.jdom.JDOMException;

import com.jt.bean.gateway.DataField;
import com.jt.bean.gateway.GwConfig;
import com.jt.gateway.service.operation.GwConfigService;

public class ConfigTest {
	public static void main(String[] args) throws JDOMException, IOException {
		GwConfigService service=new GwConfigService();
		GwConfig config=service.getConfig("测试");
		List<DataField> list=config.getList();
		for(DataField df:list){
			System.out.println(df.getName());
		}
	}
}
