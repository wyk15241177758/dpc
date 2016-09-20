package com.jt.gateway.service.management;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.jt.bean.gateway.GwConfig;
import com.jt.bean.gateway.JobInf;
import com.jt.bean.gateway.PageMsg;
import com.jt.gateway.service.job.JobInfService;
import com.jt.gateway.service.management.util.JobParamUtil;
import com.jt.gateway.service.operation.GwConfigService;
import com.jt.gateway.service.operation.IndexTask;

/**
 * 
 * @author zxb
 *
 * 2016年9月17日
 */
@Controller
public class JobManager {
	private   JobInfService  jobService;
	private Logger logger ;
	
	public JobManager(){
		logger=Logger.getLogger(JobManager.class);
	}
	public JobInfService getJobService() {
		return jobService;
	}
	
	@Resource(name="jobInfImpl") 
	public void setJobService(JobInfService jobService) {
		this.jobService = jobService;
	}
	
	
	@RequestMapping(value="addTask.do")
	public void  addJob(HttpServletRequest request, HttpServletResponse response){
		PrintWriter pw=null;
		Gson gson=new Gson();
		try {
			pw=response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		PageMsg msg=new PageMsg();
		PageMsg paramMsg=new PageMsg();
		JobParamUtil paramUtil=new JobParamUtil(request);
		GwConfigService configService=null;
		GwConfig config=null;
		//参数是否合法
		paramMsg=paramUtil.isParamLegal();
		if(paramMsg.isSig()){
			try {
				configService=new GwConfigService();
				config=paramUtil.getGwConfig();
				//该任务是否已经存在
				if(configService.getConfig(config.getTaskName())!=null){
					msg.setMsg("新增任务失败，错误信息为:[该任务已存在，无法新增]");
					msg.setSig(false);
					pw.print(gson.toJson(msg));
					return;
				}
				configService.addConfig(config);
				//将任务写入到数据库
				JobInf job=new JobInf(null, config.getTaskName(), 1, paramUtil.getJobInternal(), IndexTask.class.getName(), "test", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
				jobService.addTask(job);
				msg.setMsg("新增任务成功");
				msg.setSig(true);
				pw.print(gson.toJson(msg));
				return;
			} catch (Exception e) {
				e.printStackTrace();
				msg.setMsg("新增任务失败，错误信息为:["+e.getMessage()+"]");
				msg.setSig(false);
				pw.print(gson.toJson(msg));
				return;
			} 
		}else{
			msg.setMsg("新增任务失败，错误信息为:["+paramMsg.getMsg()+"]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
		
	}
	
	
	
//	@RequestMapping(value="addTask.do")
//	public void  addJob(HttpServletRequest request, HttpServletResponse response){
//		PrintWriter pw=null;
//		Gson gson=new Gson();
//		try {
//			pw=response.getWriter();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//		PageMsg msg=new PageMsg();
//		PageMsg paramMsg=new PageMsg();
//		JobParamUtil paramUtil=new JobParamUtil(request);
//		GwConfigService configService=null;
//		GwConfig config=null;
//		//参数是否合法
//		paramMsg=paramUtil.isParamLegal();
//		if(paramMsg.isSig()){
//			try {
//				configService=new GwConfigService();
//				config=paramUtil.getGwConfig();
//				//该任务是否已经存在
//				if(configService.getConfig(config.getIdName())!=null){
//					msg.setMsg("新增任务失败，错误信息为:[该任务已存在，无法新增]");
//					msg.setSig(false);
//					pw.print(gson.toJson(msg));
//					return;
//				}
//				configService.addConfig(config);
//				//将任务写入到数据库
//				JobInf job=new JobInf(null, config.getTaskName(), 1, paramUtil.getJobInternal(), IndexTask.class.getName(), "test", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
//				jobService.addTask(job);
//				msg.setMsg("新增任务成功");
//				msg.setSig(true);
//				pw.print(gson.toJson(msg));
//				return;
//			} catch (Exception e) {
//				e.printStackTrace();
//				msg.setMsg("新增任务失败，错误信息为:["+e.getMessage()+"]");
//				msg.setSig(false);
//				pw.print(gson.toJson(msg));
//				return;
//			} 
//		}else{
//			msg.setMsg("新增任务失败，错误信息为:["+paramMsg.getMsg()+"]");
//			msg.setSig(false);
//			pw.print(gson.toJson(msg));
//			return;
//		}
//		
//	}
}
