package com.jt.searchHis.service.impl;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
/**
 * 内存保存检索历史，达到一定数量则存入数据库，避免每次检索都操作数据库
 */

import org.apache.log4j.Logger;

import com.jt.searchHis.bean.SearchHis;
import com.jt.searchHis.service.SearchHisRtService;
import com.jt.searchHis.service.SearchHisService;

public class SearchHisRtServiceImpl implements SearchHisRtService{
	private static Logger logger = Logger.getLogger(SearchHisRtServiceImpl.class);

	
	private Map<Long ,SearchHis> questionMap;
	private SearchHisService searchHisService;
	//达到上限则存入数据库
	private int vectorSize;
	
	public int getVectorSize() {
		return vectorSize;
	}
	public void setVectorSize(int vectorSize) {
		this.vectorSize = vectorSize;
	}
	
	public Map<Long, SearchHis> getQuestionMap() {
		return questionMap;
	}
	public void setQuestionMap(Map<Long, SearchHis> questionMap) {
		this.questionMap = questionMap;
	}
	public SearchHisService getSearchHisService() {
		return searchHisService;
	}
	
	public void setSearchHisService(SearchHisService searchHisService) {
		this.searchHisService = searchHisService;
	}
	
	public SearchHisRtServiceImpl(){
		questionMap=new HashMap<Long,SearchHis>();
	}
	public void add(String question,long searchHisId){
		logger.info("存储检索历史进入缓存begin");
		SearchHis searchHis=questionMap.get(searchHisId);
		if(searchHis==null){
			searchHis=new SearchHis();
		}
		searchHis.setSearchContent(question);
		if(searchHisId!=0){
			searchHis.setId(searchHisId);
			searchHis.setSearchTimes(searchHis.getSearchTimes()+1);
		}else{
			searchHis.setId(0l);
		}
		
		logger.info("ID=["+searchHis.getId()+"] searchTimes=["+searchHis.getSearchTimes()+"] questionMap.size=["+questionMap.size()+"]");
		if(questionMap.size()>=vectorSize){
			for(Entry<Long, SearchHis>curEntry:questionMap.entrySet()){
				SearchHis curSearchHis=curEntry.getValue();
				if(curSearchHis.getId()!=0){
					SearchHis dbSearchHis=searchHisService.getSearchHisById(searchHis.getId());
					dbSearchHis.setSearchTimes(dbSearchHis.getSearchTimes()+searchHis.getSearchTimes());
					dbSearchHis.setUpdateTime(new Date());
					searchHisService.updateSearchHis(dbSearchHis);
					logger.info("update SearchHis content=["+dbSearchHis.getSearchContent()+"] searchTimes=["+dbSearchHis.getSearchTimes()
					+"] updateTime=["+dbSearchHis.getUpdateTime()+"]");
				}else{
					searchHisService.addSearchHis(curSearchHis);
					logger.info("create SearchHis content=["+curSearchHis.getSearchContent()+"] searchTimes=["+curSearchHis.getSearchTimes()
					+"] updateTime=["+curSearchHis.getUpdateTime()+"]");
				}
			}
			//清空缓存
			questionMap=new HashMap<Long,SearchHis>();
			logger.info("清空缓存");
		}
		logger.info("存储检索历史进入缓存end");
	}
}
