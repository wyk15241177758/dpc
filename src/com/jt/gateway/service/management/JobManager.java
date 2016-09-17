package com.jt.gateway.service.management;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.gateway.dao.JdbcDao;
import com.jt.gateway.dao.JdbcDaoImpl;
import com.jt.gateway.service.job.JobInfService;

/**
 * 
 * @author zxb
 *
 * 2016年9月17日
 */
@Controller
public class JobManager {
	private   JobInfService  service;
	public JobInfService getService() {
		return service;
	}
	@Resource(name="jobInfImpl") 
	public void setService(JobInfService service) {
		this.service = service;
	}
	
	
	
	@RequestMapping(value="addTask.do")
	public String addTask(ModelMap model,HttpServletRequest request, HttpServletResponse response){
		
		
		//测试数据库是否可以正常连接
//		JdbcDao jdbcDao=new JdbcDaoImpl(sconnStr, user, passwd)
		String msg="";
		return msg;
	}
}
