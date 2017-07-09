package com.jt.searchHis.service.impl;
import java.util.ArrayList;
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
import com.jt.searchHis.service.SearchHisRtService;
import com.jt.searchHis.service.SearchHisService;


public class SearchHisRtServiceImpl implements SearchHisRtService,Job{
	private static Logger logger = Logger.getLogger(SearchHisRtServiceImpl.class);

	//key:md5,value:searchHis List，不同的string加密之后可能得到同一个MD5值，可能性较小，不考虑!!
	//由spring负责new此map
	private Map<String ,SearchHis> questionMap;
	private SearchHisService searchHisService;
	//定时写入数据库时不再允许写入map，也不允许修改map中的list
	private boolean isLocked;

	public Map<String, SearchHis> getQuestionMap() {
		return questionMap;
	}

	public void setQuestionMap(Map<String, SearchHis> questionMap) {
		this.questionMap = questionMap;
	}

	public SearchHisService getSearchHisService() {
		return searchHisService;
	}
	
	public void setSearchHisService(SearchHisService searchHisService) {
		this.searchHisService = searchHisService;
	}
	
	public SearchHisRtServiceImpl(){
		questionMap=new HashMap<String ,SearchHis>();
	}
	
	public boolean isLocked() {
		return isLocked;
	}
	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}
	/**
	 * 系统启动时
	 * 将检索结果读入内存
	 */
	private void loadSearchHis(){
		logger.info("开始将检索历史读入内存");
	    long m1 = Runtime.getRuntime().freeMemory();//byte
		long begin=System.currentTimeMillis();
		List<SearchHis> list=searchHisService.query(0, (int)searchHisService.getCount(null));
		for(SearchHis search:list){
			//没有md5则加入此字段value
			String curMd5=search.getContentMd5();
			if(curMd5==null||curMd5.length()==0){
				curMd5=EncryptUtils.encodeMD5(search.getSearchContent());
				search.setContentMd5(curMd5);
				try {
					searchHisService.updateSearchHis(search,search.getSearchContent(),false);
				} catch (Exception e) {
					logger.error("修改检索历史Id=["+search.getId()+"]失败 错误信息为：");
					e.printStackTrace();
				}
			}
			SearchHis curSearch=questionMap.get(curMd5);
			if(curSearch==null){
				questionMap.put(curMd5, search);
			}
		}
	    long m2 = Runtime.getRuntime().freeMemory();
		long end=System.currentTimeMillis();
		logger.info("结束将检索历史读入内存，耗时["+(end-begin)+"]ms 占用内存["+(float)(m1-m2)/1024/1024+"]MB 检索历史共["+questionMap.size()+"]条");
	}
	
	public void add(String question){
		if(isLocked){
			logger.info("正在同步到数据库，禁止写入");
			return;
		}
		String qMd5=EncryptUtils.encodeMD5(question);
		//不存在则插入，存在则+1
		SearchHis search=questionMap.get(qMd5);
		if(search==null){
			search=new SearchHis(null, question, qMd5, 1, new Date(), new Date());
			questionMap.put(qMd5, search);
		}else{
			search.setChanged(true);
			search.setSearchTimes(search.getSearchTimes()+1);
		}
	}
	
	public void add(SearchHis searchHis){
		if(isLocked){
			logger.info("正在同步到数据库，禁止写入");
			return;
		}
		String qMd5=searchHis.getContentMd5();
		questionMap.put(qMd5, searchHis);
	}
	
	
	public void delete(String question){
		if(isLocked){
			logger.info("正在同步到数据库，禁止写入");
			return;
		}
		String qMd5=EncryptUtils.encodeMD5(question);
		SearchHis search=questionMap.get(qMd5);
		//不存在此md5，直接返回。存在则remove
		if(search==null){
			return;
		}else{
			synchronized (questionMap) {
				questionMap.remove(qMd5);
			}
		}
	}
	
	
	public void update(String oldQuestion,SearchHis searchHis){
		if(isLocked){
			logger.info("正在同步到数据库，禁止写入");
			return;
		}
		String qMd5=EncryptUtils.encodeMD5(oldQuestion);
		SearchHis search=questionMap.get(qMd5);
		searchHis.setChanged(false);

		//不存在此md5，加入
		if(search==null){
			questionMap.put(searchHis.getContentMd5(),searchHis);
		}else{
			//存在此md5，删除再增加，增加时不考虑是否有重复，在保存时排重
			synchronized (questionMap) {
				questionMap.remove(qMd5);
			}
			questionMap.put(searchHis.getContentMd5(),searchHis);
		}
	}
	
	
	
	private void init4Quartz(){
		WebApplicationContext webContext = ContextLoader.getCurrentWebApplicationContext();
		SearchHisRtServiceImpl searchHisRtServiceImpl = (SearchHisRtServiceImpl) webContext.getBean("searchHisRtServiceImpl");
		questionMap= searchHisRtServiceImpl.getQuestionMap();
		searchHisService =searchHisRtServiceImpl.getSearchHisService();
		isLocked=searchHisRtServiceImpl.isLocked();
	}
	
	
	public String doExecute(Map paramMap){
		String executeRs="";
		int successNum=0;
		int failNum=0;
		int unChangeNum=0;
		if(paramMap!=null){
			logger.info("++++++++开始执行任务["+paramMap.get("jobName")+"]");
		}else{
			logger.error("获得任务名称失败，继续执行");
		}
		init4Quartz();
		if(isLocked){
			logger.info("检索历史正在同步，跳过本次同步");
			executeRs="检索历史正在同步，跳过本次同步";
		}else{
			isLocked=true;
			try {
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
		}
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
