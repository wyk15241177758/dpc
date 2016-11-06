package com.jt.gateway.service.job;


import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.jt.base.page.Param;
import com.jt.bean.gateway.GwField;

public class GwFieldImpl extends BasicServicveImpl  implements GwFieldService{
	private static Logger logger= Logger.getLogger(GwFieldImpl.class) ;
	public List<GwField> getFieldListByJobId(Long jobId){
		String hql = "from com.jt.bean.gateway.GwField where  jobid = ?";
		List<Param>  paramList=new  ArrayList<Param>();
		paramList.add(new Param(Types.BIGINT,jobId));
		List<GwField> list = this.dao.query(hql, paramList);
		return list;
	}
	public GwField getIdField(Long jobId){
		String hql = "from com.jt.bean.gateway.GwField where  jobid = ? and key =? ";
		List<Param>  paramList=new  ArrayList<Param>();
		paramList.add(new Param(Types.BIGINT,jobId));
		paramList.add(new Param(Types.BOOLEAN,new Boolean(true)));
		List<GwField> list = this.dao.query(hql, paramList);
		if(list!=null&&list.size()!=0){
			return list.get(0);
		}else{
			return null;
		}
	}
}
