package com.jt.test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jt.bean.gateway.GwField;
import com.jt.bean.gateway.JobInf;
import com.jt.bean.gateway.PageMsg;

public class GsonTest2 {
public static void main(String[] args) {
	List<GwField> gwFields=null;
	Gson gson= new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	Type type = new TypeToken<List<GwField>>(){}.getType();
	gwFields=gson.fromJson("[{\"tableKey\":true,\"type\":\"1\",\"name\":\"xq_id\"},{\"tableKey\":false,\"type\":\"1\",\"name\":\"xq_title\"}]", type);
	System.out.println(gwFields);
	
}
}
