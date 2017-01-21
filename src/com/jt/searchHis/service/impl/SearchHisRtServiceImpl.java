package com.jt.searchHis.service.impl;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
/**
 * 内存保存检索历史，定时存入数据库，避免每次检索都操作数据库
 */

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;

import com.jt.common.util.EncryptUtils;
import com.jt.lucene.DocumentUtils;
import com.jt.searchHis.bean.SearchHis;
import com.jt.searchHis.service.SearchHisRtService;
import com.jt.searchHis.service.SearchHisService;


public class SearchHisRtServiceImpl implements SearchHisRtService{
	private static Logger logger = Logger.getLogger(SearchHisRtServiceImpl.class);

	//key:md5,value:searchHis List，不同的string加密之后可能得到同一个MD5值，可能性较小，也一并考虑吧
	//由spring负责new此map
	private Map<String ,List<SearchHis>> questionMap;
	private SearchHisService searchHisService;
	//达到时间间隔则存入数据库
	private long waitTime;
	//定时写入数据库时不再允许写入map，也不允许修改map中的list
	private boolean isLocked;
	
	public long getWaitTime() {
		return waitTime;
	}
	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}

	public Map<String, List<SearchHis>> getQuestionMap() {
		return questionMap;
	}
	public void setQuestionMap(Map<String, List<SearchHis>> questionMap) {
		this.questionMap = questionMap;
	}
	public SearchHisService getSearchHisService() {
		return searchHisService;
	}
	
	public void setSearchHisService(SearchHisService searchHisService) {
		this.searchHisService = searchHisService;
	}
	
	
	/**
	 * 系统启动时
	 * 将检索结果读入内存
	 */
	private void loadSearchHis(){
		logger.info("开始将检索历史读入内存");
	    long m1 = Runtime.getRuntime().freeMemory();//byte
		long begin=System.currentTimeMillis();
		List<SearchHis> list=searchHisService.query(0, (int)searchHisService.getTotalCount());
		for(SearchHis search:list){
			//没有md5则加入此字段value
			String curMd5=search.getContentMd5();
			if(curMd5==null){
				curMd5=EncryptUtils.encodeMD5(search.getSearchContent());
				search.setContentMd5(curMd5);
				searchHisService.updateSearchHis(search);
			}
			List<SearchHis> curList=questionMap.get(curMd5);
			if(curList==null){
				curList=new ArrayList<SearchHis>();
				curList.add(search);
				questionMap.put(curMd5, curList);
			}else{
				//不考虑数据库中重复数据的情况，应该在记录检索历史时避免重复
				curList.add(search);
			}
		}
	    long m2 = Runtime.getRuntime().freeMemory();
		long end=System.currentTimeMillis();
		logger.info("结束将检索历史读入内存，耗时["+(end-begin)+"]ms 占用内存["+(float)(m1-m2)/1024/1024+"]MB 检索历史md5计算之后共["+questionMap.size()+"]条");
	}
	
	public void add(String question){
		if(isLocked){
			logger.info("正在同步到数据库，禁止写入");
			return;
		}
		String qMd5=EncryptUtils.encodeMD5(question);
		List<SearchHis> list=questionMap.get(qMd5);
		//不存在此md5，new list加入map
		if(list==null){
			SearchHis search=new SearchHis(null, question, qMd5, 1, new Date(), new Date());
			list=new ArrayList<SearchHis>();
			list.add(search);
			questionMap.put(qMd5, list);
		}else{
			//存在此md5，遍历list，是否有相同的检索历史，有则检索次数+1，没有则插入list
			boolean exist=false;
			for(SearchHis search:list){
				if(search.getSearchContent().equalsIgnoreCase(question)){
					search.setSearchTimes(search.getSearchTimes()+1);
					exist=true;
					break;
				}
			}
			//不存在相同的检索历史，new检索历史插入list
			if(!exist){
				SearchHis search=new SearchHis(null, question, qMd5, 1, new Date(), new Date());
				list.add(search);
			}
		}
	}
	
	//定时任务
	
}
