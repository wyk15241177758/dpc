package com.jt.keyword.service.impl;

import java.io.Serializable;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import com.google.gson.Gson;
import com.jt.base.page.Param;
import com.jt.gateway.dao.IDao;
import com.jt.keyword.bean.KeyWord;
import com.jt.keyword.bean.PageResult;
import com.jt.keyword.bean.QueryResut;
import com.jt.keyword.service.KeyWordService;

public class KeyWordServiceImpl  implements  KeyWordService{
	public static String dateFormat(Date date, String strFormat) {
		DateFormat df = new SimpleDateFormat(strFormat);
		return df.format(date);
	}	
	protected IDao dao;
	@Override
	public void addKeyword(String keyvalue, Integer pId) {
		if(StringUtils.isBlank(keyvalue))
			return;
		KeyWord   keyWord=new KeyWord();
		if(pId==null)
		keyWord.setFloor(0);
		else
		keyWord.setFloor(1);
		keyWord.setIdx(max(pId));;
		
		keyWord.setWordvalue(keyvalue);
		if(pId==null)
		keyWord.setParent(null);
		else
	    keyWord.setParent((KeyWord)dao.getById(KeyWord.class, pId));
		Date  date=new Date();
		keyWord.setCreateTime(date);
		keyWord.setUpdateTime(date);
        dao.save(keyWord);
		
	}
    private  int   max(Integer pId){
    	String  sql="SELECT  MAX(idx)  FROM com.jt.keyword.bean.KeyWord";
    	if(pId!=null){
    		 sql="SELECT  MAX(idx)  FROM com.jt.keyword.bean.KeyWord  where parent.id="+pId;
    	}
    	List list = dao.query(sql);
    	if(list!=null&&list.size()>0&&list.get(0)!=null){
    		
    		return   Integer.parseInt(list.get(0).toString());
    	}
		return 0;
    }
	@Override
	public void updateKeyword(String keyvalue, Integer id) {
		if(StringUtils.isBlank(keyvalue))
			return;
		KeyWord   keyWord=new KeyWord();
		if(id==null)
		return;
		keyWord=(KeyWord)dao.getById(KeyWord.class, id);
		if(keyWord==null)
			return;
		keyWord.setWordvalue(keyvalue);
		Date  date=new Date();
		keyWord.setUpdateTime(date);
        dao.update(keyWord);		
	}
	
	

	@Override
	public boolean isExist(String keyvalue) {
		String  hql="  FROM com.jt.keyword.bean.KeyWord  where wordvalue=?";
		
		
		List<Param>  paramList=new ArrayList<Param>();
		paramList.add(new Param(Types.VARCHAR, keyvalue));
		List list = dao.query(hql, paramList);
		if(list!=null&&list.size()>0&&list.get(0)!=null)
			return true;

		return false;
	}
	  private  int   count(String keyvalue,Integer pId){
		  List<Param>  paramList=new ArrayList<Param>();
	    	String  sql="SELECT  COUNT(*)  FROM com.jt.keyword.bean.KeyWord WHERE  1=1";
	    	if(pId!=null){
	    		 sql=sql+"  AND parent.id=?";
	 			paramList.add(new Param(Types.INTEGER, pId));

	    	}
	    	if(StringUtils.isNotBlank(keyvalue)){
	    		 sql=sql+"  AND wordvalue=?";
		 			paramList.add(new Param(Types.VARCHAR, "%"+keyvalue+"%"));	
	    	}
	    	 sql=sql+"  AND floor=?";
	 			paramList.add(new Param(Types.INTEGER, 1));
	    	List list = dao.query(sql,paramList);
	    	if(list!=null&&list.size()>0&&list.get(0)!=null){
	    		
	    		return   Integer.parseInt(list.get(0).toString());
	    	}
			return 0;
	    }
	@Override
	public String queryPage(String keyvalue, Integer pageNum, Integer pageSize,
			Integer pid) {
		int  totalNum=count(keyvalue, pid);
		int  totalPageNum=(totalNum+pageSize-1)/pageSize;
		int  begin=(pageNum-1)*pageSize;
		if(begin<0){
			begin=0;	
		}
		  List<Param>  paramList=new ArrayList<Param>();

    	String  hql=" FROM com.jt.keyword.bean.KeyWord WHERE  1=1";
    	if(pid!=null){
    		hql=hql+"  AND parent.id=?";
			paramList.add(new Param(Types.INTEGER, pid));

   	}
   	if(StringUtils.isNotBlank(keyvalue)){
   		hql=hql+"  AND wordvalue like ?";
	 	  paramList.add(new Param(Types.VARCHAR, "%"+keyvalue+"%"));	
   	}
      	hql=hql+"  AND floor=?";
		paramList.add(new Param(Types.INTEGER, 1));
	     List<KeyWord> list = dao.query(hql, paramList, begin, pageSize)	;
   	  PageResult  pageResult=new PageResult();
   	  
   	pageResult.setPageNum(pageNum);
   	pageResult.setPageSize(pageSize);
   	pageResult.setTotalNum(totalNum);
   	pageResult.setTotalPage(totalPageNum);
   	List<QueryResut>  queryResuts=new ArrayList<QueryResut>();
	     if(list!=null){
	    	 for (int i = 0; i < list.size(); i++) {
	    		 QueryResut  queryResut=new QueryResut();
	    		 KeyWord  keyword= list.get(i);
	    		 queryResut.setId(keyword.id);
	    		 KeyWord pard = keyword.getParent();
	    		 if(pard==null)
	    		 queryResut.setPid(-1);
	    		 else
		        queryResut.setPid(pard.getId());

	    		 queryResut.setWordvalue(keyword.wordvalue);
	    		 queryResut.setAddtime(dateFormat(keyword.getCreateTime(), "yyyy-MM-dd"));

	    		 queryResuts.add(queryResut);
			}
	     }
	     pageResult.setList(queryResuts);
   	     Gson  gson=new Gson();
   	  
		return gson.toJson(pageResult);
	}

	@Override
	public String queryPage(String keyvalue) {
		  List<Param>  paramList=new ArrayList<Param>();

    	String  hql=" FROM com.jt.keyword.bean.KeyWord WHERE  1=1";
      	if(StringUtils.isNotBlank(keyvalue)){
       		hql=hql+"  AND wordvalue like ?";
    	 	  paramList.add(new Param(Types.VARCHAR, "%"+keyvalue+"%"));	
       	}
       	hql=hql+"  AND floor=?";
    		paramList.add(new Param(Types.INTEGER, 0));
      	List<KeyWord> list = dao.query(hql, paramList);
       	List<QueryResut>  queryResuts=new ArrayList<QueryResut>();
	     if(list!=null){
	    	 for (int i = 0; i < list.size(); i++) {
	    		 QueryResut  queryResut=new QueryResut();
	    		 KeyWord  keyword= list.get(i);
	    		 queryResut.setId(keyword.id);
	    		 KeyWord pard = keyword.getParent();
	    		 if(pard==null)
	    		 queryResut.setPid(-1);
	    		 else
		        queryResut.setPid(pard.getId());
	    		 queryResut.setWordvalue(keyword.wordvalue);
	    		 queryResut.setAddtime(dateFormat(keyword.getCreateTime(), "yyyy-MM-dd"));

	    		 queryResuts.add(queryResut);
			}
	     }
	     Gson  gson=new Gson();
			return gson.toJson(queryResuts);

	}

	@Override
	public String queryPage(String keyvalue, Integer pageNum, Integer pageSize) {
		int  totalNum=count(keyvalue, null);
		int  totalPageNum=(totalNum+pageSize-1)/pageSize;
		int  begin=(pageNum-1)*pageSize;
		if(begin<0){
			begin=0;	
		}
		  List<Param>  paramList=new ArrayList<Param>();

    	String  hql=" FROM com.jt.keyword.bean.KeyWord WHERE  1=1";
    
   	if(StringUtils.isNotBlank(keyvalue)){
   		hql=hql+"  AND wordvalue  like ?";
	 	  paramList.add(new Param(Types.VARCHAR, "%"+keyvalue+"%"));	
   	}
   	
   	hql=hql+"  AND floor=?";
		paramList.add(new Param(Types.INTEGER, 1));
	     List<KeyWord> list = dao.query(hql, paramList, begin, pageSize)	;
		  PageResult  pageResult=new PageResult();
	   	  
		   	pageResult.setPageNum(pageNum);
		   	pageResult.setPageSize(pageSize);
		   	pageResult.setTotalNum(totalNum);
		   	pageResult.setTotalPage(totalPageNum);
		   	List<QueryResut>  queryResuts=new ArrayList<QueryResut>();
			     if(list!=null){
			    	 for (int i = 0; i < list.size(); i++) {
			    		 QueryResut  queryResut=new QueryResut();
			    		 KeyWord  keyword= list.get(i);
			    		 queryResut.setId(keyword.id);
			    		 KeyWord pard = keyword.getParent();
			    		 if(pard==null)
			    		 queryResut.setPid(-1);
			    		 else
				         queryResut.setPid(pard.getId());
			    		 queryResut.setWordvalue(keyword.wordvalue);
			    		 queryResut.setAddtime(dateFormat(keyword.getCreateTime(), "yyyy-MM-dd"));
			    		 queryResuts.add(queryResut);
			    		 
					}
			     }
			     pageResult.setList(queryResuts);
		   	     Gson  gson=new Gson();
		   	  
				return gson.toJson(pageResult);
	     
	     
	     
		
	}
	
	
	

	public void delete(Class cla, Serializable[] ids) {
		List list=new ArrayList();
		for (int i = 0; i < ids.length; i++) {
			Object obj = this.queryById(cla, ids[i] );
			list.add(obj);
			//dao.delete(obj);
		}
		dao.delete(list);
	}
	
	public void delete(Class cla, String ids) {
		if(StringUtils.isBlank(ids))
			return;
		String[] lisids = ids.split(",");
		List list=new ArrayList();
		for (int i = 0; i < lisids.length; i++) {
			Object obj = this.queryById(cla,Integer.parseInt(lisids[i] ));
			list.add(obj);
			//dao.delete(obj);
		}
		dao.delete(list);
	}

	public Object queryById(Class cla, Serializable id) {
	
		return dao.getById(cla, id);
	}

	public void save(Object object) {
		dao.save(object);
	}

	public void update(Object object) {
		dao.update(object);
	}
	public void saveOrUpdate(Object obj){
		dao.saveOrUpdate(obj);
	}
	
	
	public IDao getDao() {
		return dao;
	}
	public void setDao(IDao dao) {
		this.dao = dao;
	}

	
	
	
}
