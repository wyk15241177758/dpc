package com.jt.gateway.service.management.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.jt.bean.gateway.DataField;
import com.jt.bean.gateway.GwConfig;
import java.lang.reflect.Type;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JobParamUtil {
	private GwConfig gwConfig;
	private String jobInternal;
	public JobParamUtil(){
		
	}
	public JobParamUtil(HttpServletRequest request) {
		gwConfig=new GwConfig();
		gwConfig.setIdName(request.getParameter("idname"));
		gwConfig.setIndexPath(request.getParameter("indexpath"));
		gwConfig.setSqlDB(request.getParameter("sqldb"));
		gwConfig.setSqlIP(request.getParameter("sqlip"));
		gwConfig.setSqlPort(request.getParameter("sqlport"));
		gwConfig.setSqlPw(request.getParameter("sqlpw"));
		gwConfig.setSqlTable(request.getParameter("sqltable"));
		gwConfig.setSqlUser(request.getParameter("sqluser"));
		gwConfig.setTaskName(request.getParameter("taskname"));
		jobInternal=getCronExpression(request.getParameter("internalhour"),
				request.getParameter("internalmin"));
	}
	//主方法，判断参数是否合法
	public boolean isParamLegal(){
		boolean legal=false;
		JSONArray a=null;
		JSONObject ob=(JSONObject)(a.get(0));
		
		//
		return legal;
	}
	
	private String getCronExpression(String sInternalhour,String sInternalmin){
		String jobInternal=null;
		int internalHour=0;
		int internalMin=0;
		
		try {
			internalHour=Integer.parseInt(sInternalhour);
			internalMin=Integer.parseInt(sInternalmin);
		} catch (NumberFormatException e) {
			internalHour=0;
			internalMin=0;
		}
		//时间格式: <!-- s m h d m w(?) y(?) -->,   分别对应: 秒>分>小时>日>月>周>年
		if(internalHour==0 && internalMin==0){
			jobInternal=null;
		}else{
			if(internalHour>23||internalHour<0||internalMin>59||internalMin<0){
				jobInternal=null;
			}else{
				jobInternal="0 0";
				if(internalMin==0){
					jobInternal+=" 0/"+internalHour+" * * ?";
				}else{
					if(internalHour==0){
						jobInternal+="/"+internalMin+" 0 * * ?";
					}else{
						jobInternal+="/"+internalMin+" 0/"+internalHour+" * * ?";
					}
				}
			}
		}
		return jobInternal;
	}
	public GwConfig getGwConfig() {
		return gwConfig;
	}
	public void setGwConfig(GwConfig gwConfig) {
		this.gwConfig = gwConfig;
	}
	public String getJobInternal() {
		return jobInternal;
	}
	public void setJobInternal(String jobInternal) {
		this.jobInternal = jobInternal;
	}
	public static void main(String[] args) {
		Gson b=new Gson();
		DataField df1=new DataField();
		df1.setName("测试标题");
		df1.setType("STORE");
		df1.setKey(false);
		DataField df2=new DataField();
		df2.setName("测试标题2");
		df2.setType("STORE");
		df2.setKey(false);
		List<DataField> list=new ArrayList<DataField>();
		list.add(df1);
		list.add(df2);
		System.out.println(b.toJson(list));;
		Type type = new TypeToken<List<DataField>>(){}.getType();

		DataField df=b.fromJson(b.toJson(list), type);
		System.out.println("df=\n"+df);
	}
	
}
