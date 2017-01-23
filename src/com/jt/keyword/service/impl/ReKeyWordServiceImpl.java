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
import com.jt.keyword.bean.ReKeyWord;
import com.jt.keyword.service.KeyWordService;
import com.jt.keyword.service.ReKeyWordService;

public class ReKeyWordServiceImpl  implements  ReKeyWordService{
	public static String dateFormat(Date date, String strFormat) {
		DateFormat df = new SimpleDateFormat(strFormat);
		return df.format(date);
	}	
	protected IDao dao;
	

	
	@SuppressWarnings("unchecked")
	@Override
	public   ReKeyWord   queryById(Integer  id){
		ReKeyWord rekeyWord=null;
		String hql=" from com.jt.keyword.bean.ReKeyWord  where  kid=? ";
		List<Param>  paramList=new  ArrayList<Param>();
		paramList.add(new Param(Types.INTEGER, id));
		List<ReKeyWord> list = dao.query(hql, paramList);
		if(list!=null&&list.size()>0&&list.get(0)!=null){
			rekeyWord=list.get(0);
			 List<KeyWord> reKeywords=new ArrayList<KeyWord>();
			if(StringUtils.isNotBlank(rekeyWord.getKids())){
				String[] kids = rekeyWord.getKids().split(";");
				for (int i = 0; i < kids.length; i++) {
					if(StringUtils.isNotBlank(kids[i])){
						KeyWord keyWord = (KeyWord) dao.getById(KeyWord.class, Integer.valueOf(kids[i].trim()));
						if(keyWord!=null&&keyWord.floor==0&&StringUtils.isNotBlank(keyWord.wordvalue)){
							reKeywords.add(keyWord);
						}
					}
				}
			}
			rekeyWord.setReKeywords(reKeywords);
		}
		return rekeyWord;
		
	}
	@SuppressWarnings("unchecked")
	@Override
	public   ReKeyWord   queryByName(String  name){
		String hql=" from com.jt.keyword.bean.KeyWord  where  wordvalue=? ";
		List<Param>  paramList=new  ArrayList<Param>();
		paramList.add(new Param(Types.VARCHAR, name));
		List<KeyWord> keylist = dao.query(hql, paramList);
        if(keylist!=null&&keylist.size()>0&&keylist.get(0)!=null){
        	Integer id = keylist.get(0).getId();
        	return queryById(id);
        }
	return null;
	}
	@Override
    public   ReKeyWord   updateOrSave(ReKeyWord keyWord){
		ReKeyWord word = queryById(keyWord.kid);
		if(word==null){
			dao.save(keyWord);
			return keyWord;
		}else{
			word.setKids(keyWord.getKids());
			dao.update(word);
			return word;

		}
		
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
