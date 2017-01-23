package com.jt.searchHis.service.impl;

import java.util.List;


import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.jt.gateway.service.job.BasicServicveImpl;
import com.jt.searchHis.bean.SearchHis;
import com.jt.searchHis.service.SearchHisRtService;
import com.jt.searchHis.service.SearchHisService;


public class SearchHisServiceImpl   extends BasicServicveImpl implements SearchHisService{
	private SearchHisRtService searchHisRtService;
	private int waitTimes=5;
	public SearchHisRtService getSearchHisRtService() {
		return searchHisRtService;
	}
	public void setSearchHisRtService(SearchHisRtService searchHisRtService) {
		this.searchHisRtService = searchHisRtService;
	}

/**
 * isSync为false则不同步到内存，true则同步到内存
 * @param searchHis
 * @param isSync
 * @throws Exception
 */
	public void addSearchHis(SearchHis searchHis,boolean isSync) throws Exception{
		
		if(!isSync){
			//不需要同步到内存
			this.dao.save(searchHis);
		}else{
			if(searchHisRtService==null){
				WebApplicationContext webContext = ContextLoader.getCurrentWebApplicationContext();
				this.searchHisRtService= (SearchHisRtServiceImpl) webContext.getBean("searchHisRtServiceImpl");
			}
			//需要同步到内存
			boolean timeOut=true;
			for(int i=0;i<waitTimes;i++){
				if(searchHisRtService.isLocked()){
					Thread.sleep(2000l);
				}else{
					timeOut=false;
					searchHisRtService.add(searchHis.getSearchContent());
					this.dao.save(searchHis);
					break;
				}
			}
			if(timeOut){
				throw new Exception("检索历史正在同步，保存失败，请检查");
			}			
		}
	}
	
	/**
	 * isSync为false则不同步到内存，true则同步到内存
	 * @param searchHis
	 * @param isSync
	 * @throws Exception
	 */
	public void deleteSearchHis(SearchHis searchHis,boolean isSync) throws Exception{
		if(!isSync){
			//不需要同步到内存
			this.dao.delete(searchHis);
		}else{
			if(searchHisRtService==null){
				WebApplicationContext webContext = ContextLoader.getCurrentWebApplicationContext();
				this.searchHisRtService= (SearchHisRtServiceImpl) webContext.getBean("searchHisRtServiceImpl");
			}
			//需要同步到内存
			boolean timeOut=true;
			for(int i=0;i<waitTimes;i++){
				if(searchHisRtService.isLocked()){
					Thread.sleep(2000l);
				}else{
					timeOut=false;
					searchHisRtService.delete(searchHis.getSearchContent());
					this.dao.delete(searchHis);
					break;
				}
			}
			if(timeOut){
				throw new Exception("检索历史正在同步，删除失败，请检查");
			}
		}
	}
	
	
	public void updateSearchHis(SearchHis searchHis,String oldQuestion,boolean isSync) throws Exception{
		
		if(!isSync){
			//不需要同步到内存
			this.dao.update(searchHis);
		}else{
			if(searchHisRtService==null){
				WebApplicationContext webContext = ContextLoader.getCurrentWebApplicationContext();
				this.searchHisRtService= (SearchHisRtServiceImpl) webContext.getBean("searchHisRtServiceImpl");
			}
			//需要同步到内存
			boolean timeOut=true;
			for(int i=0;i<waitTimes;i++){
				if(searchHisRtService.isLocked()){
					Thread.sleep(2000l);
				}else{
					timeOut=false;
					searchHisRtService.update(oldQuestion, searchHis);
					this.dao.update(searchHis);
					break;
				}
			}
			if(timeOut){
				throw new Exception("检索历史正在同步，修改失败，请检查");
			}
		}
		
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
