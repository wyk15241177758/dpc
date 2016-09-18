package com.jt.gateway.service.management.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jt.bean.gateway.DataField;
import com.jt.bean.gateway.GwConfig;
import com.jt.bean.gateway.PageMsg;
import com.jt.gateway.dao.JdbcDao;
import com.jt.gateway.dao.JdbcDaoImpl;
import com.jt.gateway.util.CMyString;

import java.lang.reflect.Type;
import java.sql.SQLException;


public class JobParamUtil {
	private GwConfig gwConfig;
	private String jobInternal;
	
	public JobParamUtil(GwConfig gwConfig, String jobInternal) {
		this.gwConfig = gwConfig;
		this.jobInternal = jobInternal;
	}
	public JobParamUtil(HttpServletRequest request) {
		gwConfig=new GwConfig();
		gwConfig.setIdName(CMyString.getStrNotNullor0(request.getParameter("idname"), null));
		gwConfig.setIndexPath(CMyString.getStrNotNullor0(request.getParameter("indexpath"), null));
		gwConfig.setSqlDB(CMyString.getStrNotNullor0(request.getParameter("sqldb"), null));
		gwConfig.setSqlIP(CMyString.getStrNotNullor0(request.getParameter("sqlip"), null));
		gwConfig.setSqlPort(CMyString.getStrNotNullor0(request.getParameter("sqlport"), null));
		gwConfig.setSqlPw(CMyString.getStrNotNullor0(request.getParameter("sqlpw"), null));
		gwConfig.setSqlTable(CMyString.getStrNotNullor0(request.getParameter("sqltable"), null));
		gwConfig.setSqlUser(CMyString.getStrNotNullor0(request.getParameter("sqluser"), null));
		gwConfig.setTaskName(CMyString.getStrNotNullor0(request.getParameter("taskname"), null));
		Type type = new TypeToken<List<DataField>>(){}.getType();
		Gson gson=new Gson();
		List<DataField> fieldList=null;
		try {
			fieldList=gson.fromJson(request.getParameter("datafields"), type);
			gwConfig.setList(fieldList);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			//出错则赋值为null，便于之后校验参数为空
			gwConfig.setList(null);
		}catch(Exception e){
			e.printStackTrace();
			//出错则赋值为null，便于之后校验参数为空
			gwConfig.setList(null);
		}
		jobInternal=getCronExpression(request.getParameter("internalhour"),
				request.getParameter("internalmin"));
	}
	//主方法，判断参数是否合法
	/**
	 * 未完成，判断字段是否存在有问题
	 * @return
	 */
	public PageMsg isParamLegal(){
		PageMsg msg=new PageMsg(true, "参数配置校验通过");
		//判断gwconfig的任意字段和jobInternal是否null，为null则非法
		if(gwConfig.getIdName()==null||gwConfig.getIndexPath()==null||gwConfig.getList()==null||
				gwConfig.getSqlDB()==null||gwConfig.getSqlIP()==null||gwConfig.getSqlPort()==null||
				gwConfig.getSqlPw()==null||gwConfig.getSqlTable()==null||gwConfig.getSqlUser()==null
				||gwConfig.getTaskName()==null||jobInternal==null){
			msg=new PageMsg(false,"数据库配置字段为空或间隔时间不合法");
			return msg;
		}else{
			String sql="select 1 from dual";
			//判断数据库配置是否正确
			JdbcDao dao=null;
			dao=new JdbcDaoImpl(gwConfig.getSqlDB(), gwConfig.getSqlIP(), 
					gwConfig.getSqlPort(), gwConfig.getSqlUser(), gwConfig.getSqlPw());
			try {
				int num=dao.executeQueryForCount(sql);
			} catch(Exception e){
				msg=new PageMsg(false,"数据库连接错误，错误信息:["+e.getMessage()+"]");
				return msg;
			}
			//包括是否连接正常、字段是否存在
			sql="select ";
			for(int i=0;i<gwConfig.getList().size();i++){
				DataField df=gwConfig.getList().get(i);
				if(i==0){
					sql+=df.getName();
				}else{
					sql+=","+df.getName();
				}
			}
			sql+=" from "+gwConfig.getSqlTable();
				try {
					System.out.println(sql);
					int num=dao.executeQueryForCount(sql);
				} catch(Exception e){
					msg=new PageMsg(false,"数据库表名或字段错误，错误信息:["+e.getMessage()+"]");
					return msg;
				}
		}
		return msg;
	}
	
	public static String getCronExpression(String sInternalhour,String sInternalmin){
		String jobInternal=null;
		int internalHour=0;
		int internalMin=0;
		
		try {
			internalHour=Integer.parseInt(sInternalhour);
		} catch (NumberFormatException e) {
			internalHour=0;
		}
		try {
			internalMin=Integer.parseInt(sInternalmin);
		} catch (NumberFormatException e) {
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
		DataField df1=new DataField("id", true, "STORE");
		DataField df2=new DataField("11", true, "STORE");
		List <DataField> list=new ArrayList<DataField>();
		list.add(df1);
		list.add(df2);
		GwConfig gwConfig=new GwConfig();
		gwConfig.setIdName(CMyString.getStrNotNullor0("1", null));
		gwConfig.setIndexPath(CMyString.getStrNotNullor0("1", null));
		gwConfig.setSqlDB(CMyString.getStrNotNullor0("qasys", null));
		gwConfig.setSqlIP(CMyString.getStrNotNullor0("localhost", null));
		gwConfig.setSqlPort(CMyString.getStrNotNullor0("3306", null));
		gwConfig.setSqlPw(CMyString.getStrNotNullor0("root", null));
		gwConfig.setSqlUser(CMyString.getStrNotNullor0("root", null));
		gwConfig.setSqlTable(CMyString.getStrNotNullor0("testtable", null));
		gwConfig.setTaskName(CMyString.getStrNotNullor0("1", null));
		gwConfig.setList(list);
		String jobInternal=getCronExpression("1","");
		JobParamUtil util=new JobParamUtil(gwConfig, jobInternal);
		System.out.println(util.isParamLegal());;
	}
	
}
