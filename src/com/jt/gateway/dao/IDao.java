package com.jt.gateway.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface IDao {

	/**
	 * ��ѯ�б�����ҳ��
	 * @param hql���
	 * @return list
	 */
	public List query( String hql );
	 
	public List query(final String  hql,final List  paramList);
	
	
	public List query(final String  hql,final List  paramList,int  firstResult,int maxResults);
	
	
	/**
	 * ��ѯһ��
	 * @param cla Class��
	 * @param id  ����
	 * @return
	 */
	public Object queryById( Class cla , Serializable id);
	/**
	 * ����һ������
	 * @param obj ����
	 * @return
	 */
	public void save( Object obj );
	/**
	 * ����һ������
	 * @param obj ����
	 * @return
	 */
	public void save(  Collection c );
	/**
	 * �޸�һ������
	 * @param obj ����
	 * @return
	 */
	public void update( Object obj );
	/**
	 * ɾ��һ������
	 * @param obj ����
	 * @return
	 */
	public void delete( Object obj );
	
	/**
	 * ɾ����������
	 * @param obj ����
	 * @return
	 */
	public void delete( Collection c);
	/**
	 * ���session�е��ض�ʵ��
	 */
	public void clear(Object obj);
	/**
	 * �޸�һ������
	 * @param obj ����
	 * @return
	 */
	public void merge(Object obj);
	/**
	 * �޸�һ������
	 * @param Sql
	 * @param paramList
	 */
	public void updateSql(final String Sql,final List  paramList);
	/**
	 * �޸�һ������
	 * @param hql
	 * @param paramList
	 */
	public void update(final String hql,final List  paramList);
	/**
	 * ͨ��ID��ѯ  
	 * @param cla
	 * @param id
	 * @return
	 */
	public  Object  getById(Class cla , Serializable id);
}
