package com.jt.gateway.service.management;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jt.bean.gateway.GwConfig;
import com.jt.bean.gateway.JobInf;
import com.jt.bean.gateway.JobLog;
import com.jt.bean.gateway.PageMsg;
import com.jt.gateway.service.job.JobInfService;
import com.jt.gateway.service.job.JobLogService;
import com.jt.gateway.service.job.JobRunningLogService;
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
	private JobRunningLogService jobRunningLogService;
	private JobLogService jobLogSerice;
	public JobManager(){
		msg=new PageMsg();
		paramUtil=new JobParamUtil();
		gson= new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
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
	
	public JobLogService getJobLogSerice() {
		return jobLogSerice;
	}
	@Resource(name="jobLogImpl") 
	public void setJobLogSerice(JobLogService jobLogSerice) {
		this.jobLogSerice = jobLogSerice;
	}
	public JobRunningLogService getJobRunningLogService() {
		return jobRunningLogService;
	}
	@Resource(name="jobRunningLogImpl") 
	public void setJobRunningLogService(JobRunningLogService jobRunningLogService) {
		this.jobRunningLogService = jobRunningLogService;
	}
	@RequestMapping(value="addJob.do")
	public void  addJob(HttpServletRequest request, HttpServletResponse response){
		response.setCharacterEncoding("utf-8");
		msg=new PageMsg();
		PrintWriter pw=null;
		try {
			pw=response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
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
				Date date=new Date();
				JobInf job=new JobInf(null, config.getTaskName(), 1,
						paramUtil.getJobInternal(), IndexTask.class.getName(), 
						"","TRIGGER_NAME"+config.getTaskName(),date,
						date);
				jobService.addTask(job);
				jobService.startSimJob(job.getJobId());
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
			msg.setMsg("新增任务失败，错误信息为:["+paramMsg.getMsg()+"]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
		
	}
	
	//删除任务
	@RequestMapping(value="delJob.do")
	public void delJob(HttpServletRequest request, HttpServletResponse response){
		msg=new PageMsg();
		response.setCharacterEncoding("utf-8");
		PrintWriter pw=null;
		try {
			pw=response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		
		long jobId=0l;
		try {
			jobId=Long.parseLong(CMyString.getStrNotNullor0(request.getParameter("jobid"), "0"));
		} catch (Exception e) {
			e.printStackTrace();
			msg.setMsg("删除任务id=["+jobId+"]失败,错误信息为[转换jobID类型失败]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
		JobInf job=jobService.getJobById(jobId);
		if(jobId!=0&&job!=null){
			try {
				configService.delConfig(job.getJobName());
			}catch(Exception e){
				msg.setMsg("删除任务["+job.getJobName()+"]失败，错误信息:["+e.getMessage()+"]");
				pw.print(gson.toJson(msg));
				return;
			}
			try {
				jobService.deleteTask(job);
			} catch (Exception e) {
				msg.setMsg("删除任务["+job.getJobName()+"]失败，错误信息:["+e.getMessage()+"]");
				pw.print(gson.toJson(msg));
				return;
			}
			msg.setSig(true);
			msg.setMsg("删除任务["+job.getJobName()+"]成功");
			pw.print(gson.toJson(msg));
			return;
		}else{
			msg.setMsg("删除任务id=["+jobId+"]失败,错误信息为[未获得任务ID或不存在指定的任务]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
	}
	
	//修改任务
	@RequestMapping(value="updateJob.do")
	public void updateJob(HttpServletRequest request, HttpServletResponse response){
		msg=new PageMsg();
		response.setCharacterEncoding("utf-8");
		PrintWriter pw=null;
		try {
			pw=response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
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
				Date date=new Date();
				JobInf job=new JobInf(null, config.getTaskName(), 
						1, paramUtil.getJobInternal(), IndexTask.class.getName(),
						"","TRIGGER_NAME"+config.getTaskName(), date, date);
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
		msg=new PageMsg();
		response.setCharacterEncoding("utf-8");
		PrintWriter pw=null;
		try {
			pw=response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		List<JobInf> list=jobService.getAllJobs("jobStatus>0");
		pw.print(gson.toJson(list));
	}
	//查询指定任务，及其任务的配置内容：表名、库名等
	@RequestMapping(value="listJobConfig.do")
	public void listJobConfig(HttpServletRequest request, HttpServletResponse response){
		msg=new PageMsg();
		response.setCharacterEncoding("utf-8");
		PrintWriter pw=null;
		try {
			pw=response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
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
		JobInf proxyJob=new JobInf(jobId,job.getJobName(),job.getJobStatus(),job.getCronExpression(),job.getBeanClass(),job.getDescription(),job.getTriggerName(),job.getCreateTime(),job.getUpdateTime());
		if(jobId!=0&&job!=null){
			GwConfig config=configService.getConfig(job.getJobName());
			if(config!=null){
				HashMap<String,Object>map=new HashMap<String,Object>();
				map.put("jobinf", proxyJob);
				map.put("config", config);
				msg.setMsg(map);
				msg.setSig(true);
				pw.print(gson.toJson(msg));
			}else{
				msg.setMsg("启动任务id=["+jobId+"]失败,错误信息为[未获得任务ID或不存在指定的任务]");
				msg.setSig(false);
				pw.print(gson.toJson(msg));
			}
		}else{
			msg.setMsg("启动任务id=["+jobId+"]失败,错误信息为[未获得任务ID或不存在指定的任务]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
		}
	}
	//启动
	@RequestMapping(value="startJobs.do")
	public void startJobs(HttpServletRequest request, HttpServletResponse response){
		msg=new PageMsg();
		response.setCharacterEncoding("utf-8");
		PrintWriter pw=null;
		try {
			pw=response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
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
				try {
					jobService.startSimJob(job.getJobId());
					msg.setMsg("启动任务id=["+job.getJobId()+"]成功");
				} catch (Exception e) {
					e.printStackTrace();
					msg.setMsg("启动任务id=["+job.getJobId()+"]失败");

				}
			}
		}
		msg.setSig(true);
		pw.print(gson.toJson(msg));
		return;
	}
	
	//启动
	@RequestMapping(value="startJob.do")
	public void startJob(HttpServletRequest request, HttpServletResponse response){
		msg=new PageMsg();
		response.setCharacterEncoding("utf-8");
		PrintWriter pw=null;
		try {
			pw=response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
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
			try {
				jobService.startSimJob(jobId);
				msg.setMsg("启动任务["+job.getJobName()+"]成功");
				msg.setSig(true);
				pw.print(gson.toJson(msg));
				return;
			} catch (Exception e) {
				e.printStackTrace();
				msg.setMsg("启动任务["+job.getJobName()+"]失败");
				msg.setSig(false);
				pw.print(gson.toJson(msg));
				return;
			}
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
		msg=new PageMsg();
		response.setCharacterEncoding("utf-8");
		PrintWriter pw=null;
		try {
			pw=response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
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
		msg=new PageMsg();
		response.setCharacterEncoding("utf-8");
		PrintWriter pw=null;
		try {
			pw=response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
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
	
	
	//获得运行日志
	@RequestMapping(value="getRunningLog.do")
	public void getRunningLog(HttpServletRequest request, HttpServletResponse response){
		msg=new PageMsg();
		response.setCharacterEncoding("utf-8");
		PrintWriter pw=null;
		try {
			pw=response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		long jobId=0l;
		try {
			jobId=Long.parseLong(CMyString.getStrNotNullor0(request.getParameter("jobid"), "0"));
		} catch (Exception e) {
			e.printStackTrace();
			msg.setMsg("获得任务id=["+jobId+"]的运行日志失败,错误信息为[转换jobID类型失败]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
		JobInf job=jobService.getJobById(jobId);
		if(jobId!=0&&job!=null){
			List<String> runningJob=jobRunningLogService.getRunningLog(jobId);
			msg.setMsg(runningJob);
			msg.setSig(true);
			pw.print(gson.toJson(msg));
			return;
		}else{
			msg.setMsg("获得任务id=["+jobId+"]的运行日志失败,错误信息为[未获得任务ID或不存在指定的任务]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
	}
	//获得任务日志
	@RequestMapping(value="getJobLog.do")
	public void getJobLog(HttpServletRequest request, HttpServletResponse response){
		try {
			Thread.sleep(2000l);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
		msg=new PageMsg();
		response.setCharacterEncoding("utf-8");
		PrintWriter pw=null;
		try {
			pw=response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		long jobId=0l;
		try {
			jobId=Long.parseLong(CMyString.getStrNotNullor0(request.getParameter("jobid"), "0"));
		} catch (Exception e) {
			e.printStackTrace();
			msg.setMsg("获得任务id=["+jobId+"]的日志失败,错误信息为[转换jobID类型失败]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
		JobInf job=jobService.getJobById(jobId);
		if(jobId!=0&&job!=null){
			JobLog jobLog=jobLogSerice.getLog(jobId);
			msg.setMsg(jobLog);
			msg.setSig(true);
			pw.print(gson.toJson(msg));
			return;
		}else{
			msg.setMsg("获得任务id=["+jobId+"]的日志失败,错误信息为[未获得任务ID或不存在指定的任务]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
	}
}
