package com.jt.gateway.service.job;

import java.io.Serializable;
import java.util.List;

import com.jt.bean.gateway.GwField;

public interface GwFieldService  {
	public void delete(Class cla, Serializable[] ids);
	public Object queryById(Class cla, Serializable id) ;
	public void save(Object object);
	public void update(Object object);
	public void saveOrUpdate(Object obj);
	public List<GwField> getFieldListByJobId(Long jobId);
	public GwField getIdField(Long jobId);
}
