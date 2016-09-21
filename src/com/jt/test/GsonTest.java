package com.jt.test;


import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import org.jdom.JDOMException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jt.bean.gateway.DataField;
import com.jt.bean.gateway.GwConfig;
import com.jt.gateway.service.operation.GwConfigService;


public class GsonTest {
public static void main(String[] args) throws JDOMException, IOException {
	GwConfigService service=new GwConfigService();
	GwConfig config=service.getConfig("智能问答数据抽取");
	Gson gson=new Gson();
	//System.out.println(gson.toJson(service.getConfig("智能问答数据抽取").getList()));
	DataField df=new DataField("testname", true, "store");
	String a="[{\"isKey\":false,\"type\":1,\"name\":1}]";
	Type type = new TypeToken<List<DataField>>(){}.getType();
	List<DataField> list=gson.fromJson(a, type);
	System.out.println(list);
	//System.out.println(gson.toJson(df));
}
}
