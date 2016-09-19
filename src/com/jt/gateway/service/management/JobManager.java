package com.jt.gateway.service.management;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.JDOMException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.bean.gateway.PageMsg;
import com.jt.gateway.service.job.JobInfService;
import com.jt.gateway.service.management.util.JobParamUtil;
import com.jt.gateway.service.operation.GwConfigService;

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
	public String addJob(ModelMap model,HttpServletRequest request, HttpServletResponse response){
		PageMsg msg=new PageMsg();
		JobParamUtil paramUtil=new JobParamUtil(request);
		//参数是否合法
		if(paramUtil.isParamLegal().isSig()){
			try {
				GwConfigService configService=new GwConfigService();
				configService.addConfig(paramUtil.getGwConfig());
				//将任务写入到数据库
			} catch (Exception e) {
				e.printStackTrace();
				msg.setMsg("错误信息为:["+e.getMessage()+"]");
				msg.setSig(false);
			} 
		}
		model.addAttribute(msg);
		return "addTask";
	}
}
