package com.jt.gateway.service;

import java.io.Serializable;


public interface BasicService {
	
	/**
	 * ��ѯһ������
	 * @return
	 */
	public Object queryById( Class cla ,  Serializable id );
	/**
	 * ����һ������
	 * @return
	 */
	public void save( Object object );
	/**
	 * �޸�һ������
	 * @return
	 */
	public void update( Object object );
	/**
	 * ɾ���������
	 * @return
	 */
	public void delete( Class cla ,  Serializable[] ids );
	
	
}
