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
import com.jt.keyword.service.ParseHotWordService;

public class ParseHotWordServiceImpl  implements  ParseHotWordService{
	public static String dateFormat(Date date, String strFormat) {
		DateFormat df = new SimpleDateFormat(strFormat);
		return df.format(date);
	}	
	protected IDao dao;
	
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
	@SuppressWarnings("rawtypes")
	@Override
	public void truncate() {
         String  hql="TRUNCATE TABLE  parsehotword";
         dao.updateSql(hql, new ArrayList());
	}
	
	
	//---------------SET/GET-------方法------------------
	
	public IDao getDao() {
		return dao;
	}
	public void setDao(IDao dao) {
		this.dao = dao;
	}

	

	
	
	
}
