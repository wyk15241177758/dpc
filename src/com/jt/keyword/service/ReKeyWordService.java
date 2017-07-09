package com.jt.keyword.service;

import java.io.Serializable;

import com.jt.keyword.bean.ReKeyWord;

public interface ReKeyWordService   {
  
  /**
	 * 查询一条对象
	 * @return
	 */
	public Object queryById( Class cla ,  Serializable id );
	/**
	 * 保存一个对象
	 * @return
	 */
	public void save( Object object );
	/**
	 * 修改一个对象
	 * @return
	 */
	public void update( Object object );
	/**
	 * 删除多个对象
	 * @return
	 */
	public void delete( Class cla ,  Serializable[] ids );
	
	public void delete(Class cla,String  ids );
	/**
	 * 通过ID查询关联关键词
	 * @param id
	 * @return
	 */
	ReKeyWord queryById(Integer id);
	/**
	 * 通过名称查询关联关键词
	 * @param name
	 * @return
	 */
	ReKeyWord queryByName(String name);
	/**
	 * 修改或保存
	 * @param keyWord
	 * @return
	 */
	ReKeyWord updateOrSave(ReKeyWord keyWord);


}
