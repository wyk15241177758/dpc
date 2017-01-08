package com.jt.searchHis.service.impl;

import java.util.List;

import com.jt.gateway.service.job.BasicServicveImpl;
import com.jt.searchHis.bean.SearchHis;
import com.jt.searchHis.service.SearchHisService;

import sun.rmi.runtime.Log;

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
	
	public List<SearchHis> query(final int firstResult,	final int maxResults,List paramList,String order){
		List<SearchHis> list=null;
		String hql="";
		if(order!=null){
			hql=" from com.jt.searchHis.bean.SearchHis order by "+order;
		}else{
			hql=" from com.jt.searchHis.bean.SearchHis";
		}
		
		list=this.dao.query(hql, paramList, firstResult, maxResults);
		return list;
	}
	
	public List<SearchHis> query(final int firstResult,	final int maxResults){
		return query(firstResult,maxResults,null,null);
	}
	//分页查询，从0开始
	public List<SearchHis> queryByPage(int pageIndex,int pageSize,List paramList,String order){
		int firstResult=pageIndex*pageSize;
		int maxResults=pageSize;
		return query(firstResult,maxResults,null,order);
	}
	
	public long getTotalCount(){
		long count=-1l;
		List<SearchHis> list=null;
		String hql="select count(*) from com.jt.searchHis.bean.SearchHis";
		list=this.dao.query(hql);
		
		if(list!=null){
			try {
				count=Long.parseLong(list.get(0)+"");
			} catch (NumberFormatException e) {
				System.out.println("获得检索历史总数错误，返回-1");
			}
		}
		return count;
	}
}
