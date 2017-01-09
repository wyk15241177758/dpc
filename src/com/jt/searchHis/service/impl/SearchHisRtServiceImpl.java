package com.jt.searchHis.service.impl;
/**
 * 内存保存检索历史，达到一定数量则存入数据库，避免每次检索都操作数据库
 */
import java.util.Vector;


import com.jt.searchHis.bean.SearchHis;
import com.jt.searchHis.service.SearchHisRtService;
import com.jt.searchHis.service.SearchHisService;

public class SearchHisRtServiceImpl implements SearchHisRtService{
	private Vector<SearchHis> questionVector;
	private SearchHisService searchHisService;
	//达到上限则存入数据库
	private int vectorSize;
	
	public int getVectorSize() {
		return vectorSize;
	}
	public void setVectorSize(int vectorSize) {
		this.vectorSize = vectorSize;
	}
	public Vector<SearchHis> getQuestionVector() {
		return questionVector;
	}
	public void setQuestionVector(Vector<SearchHis> questionVector) {
		this.questionVector = questionVector;
	}
	
	
	public SearchHisService getSearchHisService() {
		return searchHisService;
	}
	
	public void setSearchHisService(SearchHisService searchHisService) {
		this.searchHisService = searchHisService;
	}
	
	public SearchHisRtServiceImpl(){
		questionVector=new Vector<SearchHis>();
	}
	public void add(String question,long searchHisId){
		SearchHis searchHis=new SearchHis();
		searchHis.setSearchContent(question);
		if(searchHisId!=0){
			searchHis.setId(searchHisId);
		}else{
			searchHis.setId(0l);
		}
		questionVector.add(searchHis);
		if(questionVector.size()>=vectorSize){
			questionVector.forEach(curSearchHis ->{
				if(curSearchHis.getId()!=0){
					searchHisService.updateSearchHis(curSearchHis);
				}else{
					searchHisService.addSearchHis(curSearchHis);
				}
			});
		}
	}
}
