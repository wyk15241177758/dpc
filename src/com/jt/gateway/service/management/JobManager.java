package com.jt.gateway.service.management;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;
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
import com.jt.gateway.util.CMyString;

/**
 * 
 * @author zxb
 *
 * 2016年9月17日
 */
@Controller
public class JobManager {
	private static Logger logger =Logger.getLogger(JobManager.class);
	private JobInfService  jobService;
	private PageMsg msg;
	private JobParamUtil paramUtil;
	private GwConfigService configService;
	private GwConfig config;
	private Gson gson;
	public JobManager(){
		msg=new PageMsg();
		paramUtil=new JobParamUtil();
		gson=new Gson();
		try {
			configService=new GwConfigService();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public JobInfService getJobService() {
		return jobService;
	}
	
	@Resource(name="jobInfImpl") 
	public void setJobService(JobInfService jobService) {
		this.jobService = jobService;
	}
	
	
	@RequestMapping(value="addJob.do")
	public void  addJob(HttpServletRequest request, HttpServletResponse response){
		PrintWriter pw=null;
		try {
			pw=response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		PageMsg paramMsg=new PageMsg();
		//参数是否合法
		paramMsg=paramUtil.isAddParamLegal(request);
		if(paramMsg.isSig()){
			try {
				config=paramUtil.getGwConfig();
				//该任务是否已经存在
				if(configService.getConfig(config.getTaskName())!=null){
					msg.setMsg("新增任务["+config.getTaskName()+"]失败，错误信息为:[该任务已存在，无法新增]");
					msg.setSig(false);
					pw.print(gson.toJson(msg));
					return;
				}
				configService.addConfig(config);
				//将任务写入到数据库
				JobInf job=new JobInf(null, config.getTaskName(), 1, paramUtil.getJobInternal(), IndexTask.class.getName(), "test", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
				jobService.addTask(job);
				msg.setMsg("新增任务["+config.getTaskName()+"]成功");
				msg.setSig(true);
				pw.print(gson.toJson(msg));
				return;
			} catch (Exception e) {
				e.printStackTrace();
				msg.setMsg("新增任务["+config.getTaskName()+"]失败，错误信息为:["+e.getMessage()+"]");
				msg.setSig(false);
				pw.print(gson.toJson(msg));
				return;
			} 
		}else{
			msg.setMsg("新增任务["+config.getTaskName()+"]失败，错误信息为:["+paramMsg.getMsg()+"]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
		
	}
	
	//删除任务
	@RequestMapping(value="delJob.do")
	public void delJob(HttpServletRequest request, HttpServletResponse response){
		PrintWriter pw=null;
		try {
			pw=response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		String taskName=CMyString.getStrNotNullor0(request.getParameter("taskname"), null);
		if(taskName==null||configService.getConfig(taskName)==null){
			//taskName不为空且存在此任务
			try {
				configService.delConfig(taskName);
			}catch(Exception e){
				msg=new PageMsg();
				msg.setMsg("删除任务["+taskName+"]失败，错误信息:["+e.getMessage()+"]");
				pw.print(gson.toJson(msg));
				return;
			}
			JobInf job=jobService.getJobByName(taskName);
			jobService.deleteTask(job.getJobId());
			msg=new PageMsg();
			msg.setMsg("删除任务["+taskName+"]成功");
			pw.print(gson.toJson(msg));
			return;
		}else{
			msg=new PageMsg();
			msg.setMsg("删除任务["+taskName+"]失败，错误信息:[不存在此任务]");
			pw.print(gson.toJson(msg));
			return;
		}
	}
	
	//修改任务
	@RequestMapping(value="updateJob.do")
	public void updateJob(HttpServletRequest request, HttpServletResponse response){
		PrintWriter pw=null;
		try {
			pw=response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		PageMsg paramMsg=new PageMsg();
		//参数是否合法
		paramMsg=paramUtil.isAddParamLegal(request);
		if(paramMsg.isSig()){
			try {
				config=paramUtil.getGwConfig();
				//该任务不存在则不能修改
				if(configService.getConfig(config.getTaskName())==null){
					msg.setMsg("修改任务["+config.getTaskName()+"]失败，错误信息为:[该任务不存在，无法修改]");
					msg.setSig(false);
					pw.print(gson.toJson(msg));
					return;
				}
				configService.updateConfig(config);
				//将任务写入到数据库
				JobInf job=new JobInf(null, config.getTaskName(), 1, paramUtil.getJobInternal(), IndexTask.class.getName(), "test", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
				JobInf oldJob=jobService.getJobByName(config.getTaskName());
				job.setJobId(oldJob.getJobId());
				job.setCreateTime(oldJob.getCreateTime());
				jobService.updateTask(job);
				msg.setMsg("修改任务["+config.getTaskName()+"]成功");
				msg.setSig(true);
				pw.print(gson.toJson(msg));
				return;
			} catch (Exception e) {
				e.printStackTrace();
				msg.setMsg("修改["+config.getTaskName()+"]任务失败，错误信息为:["+e.getMessage()+"]");
				msg.setSig(false);
				pw.print(gson.toJson(msg));
				return;
			} 
		}else{
			msg.setMsg("修改任务["+config.getTaskName()+"]失败，错误信息为:["+paramMsg.getMsg()+"]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
	}
	
	//查询所有
	@RequestMapping(value="listJobs.do")
	public void listJobs(HttpServletRequest request, HttpServletResponse response){
		PrintWriter pw=null;
		try {
			pw=response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		List<JobInf> list=jobService.getAllJobs();
		pw.print(gson.toJson(list));
	}
	//启动
	@RequestMapping(value="startJobs.do")
	public void startJobs(HttpServletRequest request, HttpServletResponse response){
		PrintWriter pw=null;
		String jobIds=CMyString.getStrNotNullor0(request.getParameter("jobids"), null);
		if(jobIds==null){
			msg.setMsg("启动任务id=["+jobIds+"]失败,错误信息为[获取参数失败]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
		List<JobInf> jobs=jobService.getJobsByIds(jobIds);
		for(JobInf job:jobs){
			if(jobService.getJobById(job.getJobId())!=null){
				jobService.startSimJob(job.getJobId());
			}
		}
		msg.setMsg("启动任务id=["+jobIds+"]成功");
		msg.setSig(true);
		pw.print(gson.toJson(msg));
		return;
	}
	
	//启动
	@RequestMapping(value="startJob.do")
	public void startJob(HttpServletRequest request, HttpServletResponse response){
		PrintWriter pw=null;
		long jobId=0l;
		try {
			jobId=Long.parseLong(CMyString.getStrNotNullor0(request.getParameter("jobid"), "0"));
		} catch (Exception e) {
			e.printStackTrace();
			msg.setMsg("启动任务id=["+jobId+"]失败,错误信息为[转换jobID类型失败]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
		JobInf job=jobService.getJobById(jobId);
		if(jobId!=0&&job!=null){
			jobService.startSimJob(jobId);
			msg.setMsg("启动任务["+job.getJobName()+"]成功");
			msg.setSig(true);
			pw.print(gson.toJson(msg));
			return;
		}else{
			msg.setMsg("启动任务id=["+jobId+"]失败,错误信息为[未获得任务ID或不存在指定的任务]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
	}
	
	//停止
	@RequestMapping(value="stopJobs.do")
	public void stopJobs(HttpServletRequest request, HttpServletResponse response){
		PrintWriter pw=null;
		String jobIds=CMyString.getStrNotNullor0(request.getParameter("jobids"), null);
		if(jobIds==null){
			msg.setMsg("停止任务id=["+jobIds+"]失败,错误信息为[获取参数失败]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
		List<JobInf> jobs=jobService.getJobsByIds(jobIds);
		for(JobInf job:jobs){
			if(jobService.getJobById(job.getJobId())!=null){
				jobService.stopJob(job.getJobId());
			}
		}
		msg.setMsg("停止任务id=["+jobIds+"]成功");
		msg.setSig(true);
		pw.print(gson.toJson(msg));
		return;
	}
	
	//停止
	@RequestMapping(value="stopJob.do")
	public void stopJob(HttpServletRequest request, HttpServletResponse response){
		PrintWriter pw=null;
		long jobId=0l;
		try {
			jobId=Long.parseLong(CMyString.getStrNotNullor0(request.getParameter("jobid"), "0"));
		} catch (Exception e) {
			e.printStackTrace();
			msg.setMsg("停止任务id=["+jobId+"]失败,错误信息为[转换jobID类型失败]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
		JobInf job=jobService.getJobById(jobId);
		if(jobId!=0&&job!=null){
			jobService.stopJob(jobId);
			msg.setMsg("停止任务["+job.getJobName()+"]成功");
			msg.setSig(true);
			pw.print(gson.toJson(msg));
			return;
		}else{
			msg.setMsg("停止任务id=["+jobId+"]失败,错误信息为[未获得任务ID或不存在指定的任务]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
	}	
		
}
