package com.jt.gateway.service.management.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jt.bean.gateway.DataField;
import com.jt.bean.gateway.GwConfig;
import com.jt.bean.gateway.JobInf;
import com.jt.bean.gateway.PageMsg;
import com.jt.gateway.dao.JdbcDao;
import com.jt.gateway.dao.impl.JdbcDaoImpl;
import com.jt.gateway.util.CMyString;


public class JobParamUtil {
	private JobInf jobInf;
	private String jobInternal;
	private Long jobId;
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
		Type type = new TypeToken<List<DataField>>(){}.getType();
		Gson gson=new Gson();
		List<DataField> fieldList=null;
		try {
			fieldList=gson.fromJson(request.getParameter("datafields"), type);
			HashSet<String> set=new HashSet<String>();
			//排重
			boolean simFlag=false;
			for(DataField df:fieldList){
				if(set.contains(df.getName())){
					simFlag=true;
					break;
				}else{
					set.add(df.getName());
				}
			}
			if(simFlag){
				jobInf=null;
				return;
			}
			
			jobInf.setList(fieldList);
			for(DataField df:fieldList){
				if(df.isKey()){
					gwConfig.setIdName(df.getName());
				}
			}
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
	 * 
	 * @return
	 */
	public PageMsg isAddParamLegal(HttpServletRequest request){
		PageMsg msg=new PageMsg(true, "参数配置校验通过");
		//初始化参数
		initAddParam(request);
		//判断gwconfig的任意字段和jobInternal是否null，为null则非法
		if(gwConfig==null){
			msg=new PageMsg(false,"字段重复");
			return msg;
		}else if(gwConfig.getIdName()==null){
			msg=new PageMsg(false,"ID字段为空");
			return msg;
		}else if(gwConfig.getIndexPath()==null){
			msg=new PageMsg(false,"索引目录为空");
			return msg;
		}else if(gwConfig.getList()==null){
			msg=new PageMsg(false,"数据库字段为空");
			return msg;
		}else if(gwConfig.getSqlDB()==null){
			msg=new PageMsg(false,"数据库为空");
			return msg;
		}else if(gwConfig.getSqlIP()==null){
			msg=new PageMsg(false,"数据库IP为空");
			return msg;
		}else if(gwConfig.getSqlPort()==null){
			msg=new PageMsg(false,"数据库端口为空");
			return msg;
		}else if(gwConfig.getSqlPw()==null){
			msg=new PageMsg(false,"数据库密码为空");
			return msg;
		}else if(gwConfig.getSqlTable()==null){
			msg=new PageMsg(false,"数据库表名为空");
			return msg;
		}else if(gwConfig.getSqlUser()==null){
			msg=new PageMsg(false,"数据库用户为空");
			return msg;
		}else if(gwConfig.getTaskName()==null){
			msg=new PageMsg(false,"任务名称为空");
			return msg;
		}else if(jobInternal==null){
			msg=new PageMsg(false,"间隔时间不合法");
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
			sql="select count(1) from information_schema.`COLUMNS` where table_name='"+
			gwConfig.getSqlTable()+"' and (";
			for(int i=0;i<gwConfig.getList().size();i++){
				DataField df=gwConfig.getList().get(i);
				if(i==0){
					sql+="column_name='"+df.getName()+"'";
				}else{
					sql+=" or column_name='"+df.getName()+"'";
				}
			}
			sql+=")";
				try {
					int num=dao.executeQueryForCount(sql);
					if(num!=gwConfig.getList().size()){
						msg=new PageMsg(false,"数据库表名错误或某个字段不存在");
						return msg;
					}
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
	public long getJobId() {
		return jobId;
	}
	public void setJobId(long jobId) {
		this.jobId = jobId;
	}
	public static void main(String[] args) {
		DataField df1=new DataField("FL_ID", true, "STORE");
		DataField df2=new DataField("FL_NAME", true, "STORE");
		List <DataField> list=new ArrayList<DataField>();
		list.add(df1);
		list.add(df2);
		GwConfig gwConfig=new GwConfig();
		gwConfig.setIdName(CMyString.getStrNotNullor0("1", null));
		gwConfig.setIndexPath(CMyString.getStrNotNullor0("1", null));
		gwConfig.setSqlDB(CMyString.getStrNotNullor0("jtcrawler", null));
		gwConfig.setSqlIP(CMyString.getStrNotNullor0("localhost", null));
		gwConfig.setSqlPort(CMyString.getStrNotNullor0("3306", null));
		gwConfig.setSqlPw(CMyString.getStrNotNullor0("root", null));
		gwConfig.setSqlUser(CMyString.getStrNotNullor0("root", null));
		gwConfig.setSqlTable(CMyString.getStrNotNullor0("crawler_fl", null));
		gwConfig.setTaskName(CMyString.getStrNotNullor0("1", null));
		gwConfig.setList(list);
		String jobInternal=getCronExpression("1","");
		JobParamUtil util=new JobParamUtil(gwConfig, jobInternal);
	}
	
}
