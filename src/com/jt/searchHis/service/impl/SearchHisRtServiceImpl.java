package com.jt.searchHis.service.impl;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
/**
 * 内存保存检索历史，达到一定数量则存入数据库，避免每次检索都操作数据库
 */

import org.apache.log4j.Logger;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.document.Document;

import com.jt.lucene.DocumentUtils;
import com.jt.nlp.service.LuceneSearchService;
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
	private LuceneSearchService luceneSearchService;
	//达到时间间隔则存入数据库
	private long waitTime;
	
	
	public LuceneSearchService getLuceneSearchService() {
		return luceneSearchService;
	}
	public void setLuceneSearchService(LuceneSearchService luceneSearchService) {
		this.luceneSearchService = luceneSearchService;
	}
	public long getWaitTime() {
		return waitTime;
	}
	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
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
	
	/**
	 * 系统启动时
	 * 将检索结果读入内存
	 */
	private void loadSearchHis(){
		logger.info("开始将检索历史读入内存");
		long begin=System.currentTimeMillis();
		List<SearchHis> list=searchHisService.query(0, (int)searchHisService.getTotalCount());
		for(SearchHis search:list){
			questionMap.put(search.getId(), search);
		}
		long end=System.currentTimeMillis();
		logger.info("结束将检索历史读入内存，耗时:"+(end-begin)+" ms");
	}
	
	/**
	 * 传入question，返回匹配的检索历史
	 * @param question
	 * @return
	 */
	private List<Document> getSearchHisByLucene(String question){
		//检索参数
		String[] queryString={question};
		Occur[] occurs={Occur.MUST};
		String[] fields={DocumentUtils.getMapedFieldName("searchHis", "content")};
		
		//排序参数
		String[] sortField={DocumentUtils.getMapedFieldName("searchHis", "times")};
		SortField.Type[] sortFieldType={SortField.Type.LONG};
		boolean[] reverse={true};
		List<Document> list=luceneSearchService.search(queryString, occurs, fields, sortField,sortFieldType, reverse, true, 0, 10);
		return list;
	}
	
	public void add(String question){
		SearchHis searchHis=null;
		List<Document> list=getSearchHisByLucene(question);
		//全文检索之后在严格判断是否相等，只返回第一条
		if(list!=null){
			for(Document doc:list){
				if(doc.get(DocumentUtils.getMapedFieldName("searchHis", "content")).equals(question)){
					try {
						Long searchHisId=Long.parseLong(doc.get(DocumentUtils.getMapedFieldName("searchHis", "id")));
						searchHis=searchHisService.getSearchHisById(searchHisId);
						break;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		if(searchHis!=null){
			SearchHis temp=questionMap.get(searchHis.getId());
			if(temp!=null){
				temp.setSearchTimes(searchHis.getSearchTimes()+1);
			}else{
				questionMap.put(searchHis.getId(), searchHis);
			}
		}else{
			questionMap.put(searchHis.getId(), searchHis);
		}
		
		
		
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
