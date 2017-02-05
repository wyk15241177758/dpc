package com.jt.searchHis.service.impl;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.jt.base.page.Param;
import com.jt.searchHis.bean.SearchHis;
import com.jt.searchHis.service.SearchHisService;


public class SearchHisCleanJob implements Job{
	private static Logger logger = Logger.getLogger(SearchHisCleanJob.class);
	//updateTime早于此时间点的检索历史将被删除
	private String expiration;
	private SearchHisService searchHisService;
	
	
	public String getExpiration() {
		return expiration;
	}


	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}


	public SearchHisService getSearchHisService() {
		return searchHisService;
	}


	public void setSearchHisService(SearchHisService searchHisService) {
		this.searchHisService = searchHisService;
	}


	private void init4Quartz(){
		WebApplicationContext webContext = ContextLoader.getCurrentWebApplicationContext();
		SearchHisCleanJob searchHisCleanJob = (SearchHisCleanJob) webContext.getBean("searchHisCleanJob");
		this.expiration=searchHisCleanJob.getExpiration();
		this.searchHisService=searchHisCleanJob.getSearchHisService();
	}
	
	
	public String doExecute(Map paramMap){
		String executeRs="";
		int successNum=0;
		int total=0;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if(paramMap!=null){
			logger.info("++++++++开始执行任务["+paramMap.get("jobName")+"]");
		}else{
			logger.error("获得任务名称失败，继续执行");
		}
		init4Quartz();
		Date expirationDate=null;
		Long lExpiration=null;
		try {
			lExpiration=Long.parseLong(expiration)*24*60*60*1000;
		} catch (Exception e) {
			logger.error("转换超时时间expiration=["+expiration+"]错误，改为默认30天");
			lExpiration=30*24*60*60*1000l;
		}
		expirationDate=new Date(System.currentTimeMillis()-lExpiration);
		
		try {
			String hql="from com.jt.searchHis.bean.SearchHis where updateTime<?";
			List<Param> paramList=new ArrayList<Param>();
			paramList.add(new Param(Types.DATE, expirationDate, null, false));
			List<SearchHis> list=searchHisService.queryByHql(paramList, hql);
			total=list.size();
			for(SearchHis curHis:list){
				searchHisService.deleteSearchHis(curHis, true);
				successNum++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		executeRs="本次清理updatetime早于["+sdf.format(expirationDate)+"]的数据，共查询数据["+total+"]条，成功删除["+successNum+"]条";
		logger.info(executeRs);
		if(paramMap==null){
			logger.info("++++++++结束执行任务");
		}else{
			logger.info("++++++++结束执行任务["+paramMap.get("jobName")+"]");
		}
		return executeRs;
	}
	
	//定时任务
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		Map paramMap=(Map)(context.getJobDetail().getJobDataMap().get("param"));
		doExecute(paramMap);
	}
	
}
