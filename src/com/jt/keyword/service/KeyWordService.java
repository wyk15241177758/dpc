package com.jt.keyword.service;

import java.io.Serializable;

public interface KeyWordService   {
  /**
   * 新增关键词
   * @param keyvalue
   * @param pId
   */
  public  void  addKeyword(String keyvalue,Integer  pId);
  /**
   * 修改关键词
   * @param keyvalue
   * @param id
   */
  public  void  updateKeyword(String keyvalue,Integer  id);
  /**
   * 判断关键词是否存在
   * @param keyvalue
   * @return
   */
  public  boolean  isExist(String keyvalue);
  /**
   * 
   * @param keyvalue
   * @param pageNum
   * @param pageSize
   * @param pid
   * @return
   */
  public  String  queryPage(String keyvalue,Integer  pageNum,Integer pageSize,Integer pid);
 /**
  * 查询一级节点
  * @param keyvalue
  * @return
  */
  public  String  queryPage(String keyvalue);
  /**
   * 查询二级节点
   * @param keyvalue
   * @param pageNum
   * @param pageSize
   * @return
   */
  public  String  queryPage(String keyvalue,Integer  pageNum,Integer pageSize);
  
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


}
