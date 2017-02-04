package com.jt.searchHis.service.impl;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.jt.common.util.EncryptUtils;
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
		if(paramMap!=null){
			logger.info("++++++++开始执行任务["+paramMap.get("jobName")+"]");
		}else{
			logger.error("获得任务名称失败，继续执行");
		}
		init4Quartz();
		try {
			searchHisService.deleteSearchHis(searchHis, isSync);
			for(Map.Entry<String, SearchHis> curEntry:questionMap.entrySet()){
					SearchHis search=curEntry.getValue();
					//新增
					if(search.getId()==null||search.getId()==0){
						try {
							searchHisService.addSearchHis(search,false);
							successNum++;
						} catch (Exception e) {
							failNum++;
							logger.error("同步新增检索历史["+search.getSearchContent()+"]到数据库失败，错误信息为");
							e.printStackTrace();
						}
					}else{
						//更新，只更新有修改标记的检索历史，更新之后将修改标记置为false
						if(search.isChanged()){
							try {
								searchHisService.updateSearchHis(search,search.getSearchContent(),false);
								search.setChanged(false);
								successNum++;
							} catch (Exception e) {
								failNum++;
								logger.error("修改检索历史Id=["+search.getId()+"]失败 错误信息为：");
								e.printStackTrace();
							}
							
						}else{
							unChangeNum++;
						}
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			//无论是否成功都解锁
			isLocked=false;
		}
			
			executeRs="本次共同步检索历史成功["+successNum+"]条，失败["+failNum+"]条,未修改记录["+unChangeNum+"]条,内存中检索历史共["+questionMap.size()+"]条";
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
	
	//调试使用
	public Map<String ,SearchHis> tempList(){
		return this.questionMap;
	}
}
