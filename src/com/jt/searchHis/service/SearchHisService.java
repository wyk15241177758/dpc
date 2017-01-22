package com.jt.searchHis.service;

import java.util.List;

import com.jt.searchHis.bean.SearchHis;

public interface SearchHisService {
	public void addSearchHis(SearchHis searchHis) throws InterruptedException;
	public void deleteSearchHis(SearchHis searchHis);
	public void updateSearchHis(SearchHis searchHis);
	public SearchHis getSearchHisById(Long id);
	public List<SearchHis> query(final int firstResult,	final int maxResults);
	public List<SearchHis> queryByPage(int pageIndex,int pageSize,List paramList,String order);
	public long getTotalCount();
	public List<SearchHis> query(final int firstResult,	final int maxResults,List paramList,String order);
}
