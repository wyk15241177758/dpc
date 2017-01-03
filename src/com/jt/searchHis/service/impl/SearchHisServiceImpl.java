package com.jt.searchHis.service.impl;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.jt.base.page.Param;
import com.jt.gateway.service.job.BasicServicveImpl;
import com.jt.searchHis.bean.SearchHis;
import com.jt.searchHis.service.SearchHisService;

public class SearchHisServiceImpl   extends BasicServicveImpl implements SearchHisService{

	public void addSearchHis(SearchHis searchHis){
		this.dao.save(searchHis);
	}
	
	public void deleteSearchHis(SearchHis searchHis){
		this.dao.delete(searchHis);
	}
	
	public void updateSearchHis(SearchHis searchHis){
		this.dao.update(searchHis);
	}
	
	public SearchHis getSearchHisById(Long id){
		return (SearchHis)(this.dao.getById(SearchHis.class, id));
	}
	
	public List<SearchHis> query(final int firstResult,	final int maxResults){
		List<SearchHis> list=null;
		String hql="from com.jt.searchHis.bean.SearchHis";
		List<Param> paramList=new ArrayList<Param>();
		paramList.add(new Param(Types.BIGINT,""));
		this.dao.query(hql, paramList, firstResult, maxResults);
		return list;
	}
	//分页查询，从0开始
	public List<SearchHis> queryByPage(int pageIndex,	int pageSize){
		int firstResult=pageIndex*pageSize;
		int maxResults=pageSize;
		return query(firstResult,maxResults);
	}
}
