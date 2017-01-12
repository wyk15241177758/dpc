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

	//有searchHisId的检索
	private Map<Long ,SearchHis> questionMap;
	//没有searchHisId的检索
	private Map<String,Integer> newMap;
	private SearchHisService searchHisService;
	//达到上限则存入数据库
	private int limitSize;
	

	public int getLimitSize() {
		return limitSize;
	}
	public void setLimitSize(int limitSize) {
		this.limitSize = limitSize;
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
		newMap = new HashMap<String,Integer>();
	}
	public void add(String question,long searchHisId){
		logger.info("存储检索历史进入缓存begin");
		SearchHis searchHis=questionMap.get(searchHisId);
		//新的检索
		if(searchHisId==0l){
			Integer searchTimes=newMap.get(question);
			if(searchTimes==null){
				newMap.put(question, 1);
			}else{
				newMap.put(question,searchTimes+1);
			}
		}else{
			//存在此检索历史
			//但是map中没有
			if(searchHis==null){
				searchHis=new SearchHis();
				searchHis.setId(searchHisId);
				searchHis.setSearchTimes(1);
				questionMap.put(searchHisId, searchHis);
			}else{
				searchHis.setSearchTimes(searchHis.getSearchTimes()+1);
			}
		}
		//达到上限
		if(questionMap.size()>=limitSize||newMap.size()>=limitSize){
			//保存newmap中数据并清空map
			for(Entry<String, Integer>curEntry:newMap.entrySet()){
				SearchHis curSearchHis=new SearchHis(null,curEntry.getKey(),curEntry.getValue(),new Date(),new Date());
				searchHisService.addSearchHis(curSearchHis);
			}
			newMap.clear();
			
			//保存questionMap中数据并清空
			for(Entry<Long, SearchHis>curEntry:questionMap.entrySet()){
				SearchHis curSearchHis=curEntry.getValue();
				SearchHis dbSearchHis=searchHisService.getSearchHisById(curSearchHis.getId());
				dbSearchHis.setSearchTimes(dbSearchHis.getSearchTimes()+curSearchHis.getSearchTimes());
				dbSearchHis.setUpdateTime(new Date());
				searchHisService.updateSearchHis(dbSearchHis);
			}
			//清空缓存
			questionMap.clear();
			logger.info("清空缓存");
		}
		logger.info("存储检索历史进入缓存end");
	}
}
