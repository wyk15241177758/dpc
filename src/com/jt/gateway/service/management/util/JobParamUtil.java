package com.jt.gateway.service.management.util;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jt.bean.gateway.GwField;
import com.jt.bean.gateway.JobInf;
import com.jt.bean.gateway.PageMsg;
import com.jt.gateway.dao.JdbcDao;
import com.jt.gateway.dao.impl.JdbcDaoImpl;
import com.jt.gateway.util.CMyString;


public class JobParamUtil {
	private JobInf jobInf;
	private List<GwField> gwFields;
	private String jobInternal;
	private String fieldIdName;
	public JobParamUtil(JobInf jobInf, String jobInternal) {
		this.jobInf = jobInf;
		this.jobInternal = jobInternal;
	}
	public JobParamUtil() {
	}
	
	private void initAddParam(HttpServletRequest request){
		jobInf=new JobInf();
		jobInf.setIndexPath(CMyString.getStrNotNullor0(request.getParameter("indexpath"), null));
		jobInf.setSqlDb(CMyString.getStrNotNullor0(request.getParameter("sqldb"), null));
		jobInf.setSqlIp(CMyString.getStrNotNullor0(request.getParameter("sqlip"), null));
		try {
			jobInf.setSqlPort(Integer.parseInt(CMyString.getStrNotNullor0(request.getParameter("sqlport"), "0")));
		} catch (Exception e1) {
			e1.printStackTrace();
			jobInf.setSqlPort(0);
		}
		jobInf.setSqlPw(CMyString.getStrNotNullor0(request.getParameter("sqlpw"), null));
		jobInf.setSqlTable(CMyString.getStrNotNullor0(request.getParameter("sqltable"), null));
		jobInf.setSqlUser(CMyString.getStrNotNullor0(request.getParameter("sqluser"), null));
		jobInf.setJobName(CMyString.getStrNotNullor0(request.getParameter("taskname"), null));
		Type type = new TypeToken<List<GwField>>(){}.getType();
		Gson gson=new Gson();
		try {
			gwFields=gson.fromJson(request.getParameter("datafields"), type);
			//字段名称改为大写
			for(GwField gf:gwFields){
				gf.setName(gf.getName().toUpperCase());
			}
			
			
			HashSet<String> set=new HashSet<String>();
			//排重
			boolean simFlag=false;
			for(GwField gf:gwFields){
				if(set.contains(gf.getName())){
					simFlag=true;
					break;
				}else{
					set.add(gf.getName());
				}
			}
			if(simFlag){
				jobInf=null;
				gwFields=null;
				return;
			}
			
			for(GwField gf:gwFields){
				if(gf.isTableKey()){
					fieldIdName=gf.getName();
				}
			}
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		jobInternal=getCronExpression(request.getParameter("internalhour"),
				request.getParameter("internalmin"));
	}
	//主方法，判断参数是否合法
	/**
	 * 
	 * @return
	 */
	public PageMsg isAddParamLegal(HttpServletRequest request){
		PageMsg msg=new PageMsg(true, "参数配置校验通过");
		//初始化参数
		initAddParam(request);
		//判断jobinf的任意字段和jobInternal是否null，为null则非法
		if(jobInf==null){
			msg=new PageMsg(false,"字段重复");
			return msg;
		}else if(fieldIdName==null){
			msg=new PageMsg(false,"ID字段为空");
			return msg;
		}else if(jobInf.getIndexPath()==null){
			msg=new PageMsg(false,"索引目录为空");
			return msg;
		}else if(gwFields==null){
			msg=new PageMsg(false,"数据库字段为空");
			return msg;
		}else if(jobInf.getSqlDb()==null){
			msg=new PageMsg(false,"数据库为空");
			return msg;
		}else if(jobInf.getSqlIp()==null){
			msg=new PageMsg(false,"数据库IP为空");
			return msg;
		}else if(jobInf.getSqlPort()==0){
			msg=new PageMsg(false,"数据库端口为空");
			return msg;
		}else if(jobInf.getSqlPw()==null){
			msg=new PageMsg(false,"数据库密码为空");
			return msg;
		}else if(jobInf.getSqlTable()==null){
			msg=new PageMsg(false,"数据库表名为空");
			return msg;
		}else if(jobInf.getSqlUser()==null){
			msg=new PageMsg(false,"数据库用户为空");
			return msg;
		}else if(jobInf.getJobName()==null){
			msg=new PageMsg(false,"任务名称为空");
			return msg;
		}else if(jobInternal==null){
			msg=new PageMsg(false,"间隔时间不合法");
			return msg;
		}else{
			String sql="select 1 from dual";
			//判断数据库配置是否正确
			JdbcDao dao=null;
			dao=new JdbcDaoImpl(jobInf.getSqlDb(), jobInf.getSqlIp(), 
					jobInf.getSqlPort()+"", jobInf.getSqlUser(), jobInf.getSqlPw());
			try {
				int num=dao.executeQueryForCount(sql);
			} catch(Exception e){
				msg=new PageMsg(false,"数据库连接错误，错误信息:["+e.getMessage()+"]");
				return msg;
			}
			//包括是否连接正常、字段是否存在
			sql="select * from "+jobInf.getSqlDb()+"."+jobInf.getSqlTable()+" limit 1,1";
			
			try {
				Map<String,Object> map=dao.executeQueryForMap(sql);
				for(GwField field:gwFields){
					if(!map.containsKey(field.getName())){
						msg=new PageMsg(false,"数据库不包含["+field.getName()+"]字段，请检查配置");
						return msg;
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
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
		//0/5 0/5 * * ?每隔5小时，5分钟运行一次
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
						jobInternal+="/"+internalMin+" * * * ?";
					}else{
						jobInternal+="/"+internalMin+" 0/"+internalHour+" * * ?";
					}
				}
			}
		}
		return jobInternal;
	}
	public String getJobInternal() {
		return jobInternal;
	}
	public void setJobInternal(String jobInternal) {
		this.jobInternal = jobInternal;
	}
	public String getFieldIdName() {
		return fieldIdName;
	}
	public void setFieldIdName(String fieldIdName) {
		this.fieldIdName = fieldIdName;
	}
	public JobInf getJobInf() {
		return jobInf;
	}
	public void setJobInf(JobInf jobInf) {
		this.jobInf = jobInf;
	}
	public List<GwField> getGwFields() {
		return gwFields;
	}
	public void setGwFields(List<GwField> gwFields) {
		this.gwFields = gwFields;
	}
	
	
}
