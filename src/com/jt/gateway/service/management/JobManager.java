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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jt.bean.gateway.GwField;
import com.jt.bean.gateway.JobInf;
import com.jt.bean.gateway.JobLog;
import com.jt.bean.gateway.PageMsg;
import com.jt.gateway.service.job.GwFieldService;
import com.jt.gateway.service.job.JobInfService;
import com.jt.gateway.service.job.JobLogService;
import com.jt.gateway.service.job.JobRunningLogService;
import com.jt.gateway.service.management.util.JobParamUtil;
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
	private Gson gson;
	private JobRunningLogService jobRunningLogService;
	private JobLogService jobLogSerice;
	private GwFieldService gwFieldService;
	public JobManager(){
		msg=new PageMsg();
		paramUtil=new JobParamUtil();
		gson= new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	}
	public JobInfService getJobService() {
		return jobService;
	}
	
	@Resource(name="jobInfImpl") 
	public void setJobService(JobInfService jobService) {
		this.jobService = jobService;
	}
	
	
	public GwFieldService getGwFieldService() {
		return gwFieldService;
	}
	
	@Resource(name="gwFieldImpl")
	public void setGwFieldService(GwFieldService gwFieldService) {
		this.gwFieldService = gwFieldService;
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
		JobInf job=null;
		PageMsg paramMsg=new PageMsg();
		//参数是否合法
		paramMsg=paramUtil.isAddParamLegal(request);
		if(paramMsg.isSig()){
			String jobName=paramUtil.getJobInf().getJobName();
			JobInf paramJob=paramUtil.getJobInf();
			try {
				//该任务是否已经存在
				job=jobService.getJobByName(jobName);
				if(job!=null){
					msg.setMsg("新增任务["+job.getJobName()+"]失败，错误信息为:[该任务已存在，无法新增]");
					msg.setSig(false);
					pw.print(gson.toJson(msg));
					return;
				}
				//不存在则写入数据库
				Date date=new Date();
				job=new JobInf(null, jobName, 1, paramUtil.getJobInternal(), IndexTask.class.getName(),
						null, "TRIGGER_NAME"+jobName, date, null, paramJob.getIndexPath(), 
						paramJob.getSqlIp(), paramJob.getSqlUser(), paramJob.getSqlPw(),
						paramJob.getSqlDb(), paramJob.getSqlPort(), paramJob.getSqlTable());
				jobService.addTask(job);
				msg.setMsg("新增任务["+jobName+"]成功");
				msg.setSig(true);
				pw.print(gson.toJson(msg));
				return;
			} catch (Exception e) {
				e.printStackTrace();
				msg.setMsg("新增任务["+jobName+"]失败，错误信息为:["+e.getMessage()+"]");
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
	
	/**
	 * 立即执行任务
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="startImmediate.do")
	public void  startImmediate(HttpServletRequest request, HttpServletResponse response){
		response.setCharacterEncoding("utf-8");
		msg=new PageMsg();
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
			msg.setMsg("启动实时任务id=["+jobId+"]失败,错误信息为[转换jobID类型失败]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
		JobInf job=jobService.getJobById(jobId);
		if(jobId!=0&&job!=null){
			try {
				jobService.startImmediateJob(job);
			} catch (Exception e) {
				e.printStackTrace();
				msg.setSig(false);
				msg.setMsg("启动实时任务["+job.getJobName()+"]失败，错误信息为["+e.getMessage()+"]");
				pw.print(gson.toJson(msg));
				return;
			}
			msg.setSig(true);
			msg.setMsg("启动实时任务["+job.getJobName()+"]成功");
			pw.print(gson.toJson(msg));
			return;
		}else{
			msg.setMsg("启动实时任务id=["+jobId+"]失败,错误信息为[未获得任务ID或不存在指定的任务]");
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
		List<GwField> gwFieldList=null;
		if(jobId!=0&&job!=null){
			try {
				//删除job关联的字段信息
				gwFieldList=gwFieldService.getFieldListByJobId(job.getJobId());
				Integer[] gwFieldListIds=new Integer[gwFieldList.size()];
				for(int i=0;i<gwFieldList.size();i++){
					gwFieldListIds[i]=gwFieldList.get(i).getFieldId();
				}
				gwFieldService.delete(GwField.class, gwFieldListIds);
				//删除job信息
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
		
		JobInf job=null;
		List<GwField> gwFieldList=null;
		JobInf paramJob=paramUtil.getJobInf();
		List<GwField> paramGwFieldList=paramUtil.getGwFields();
		PageMsg paramMsg=new PageMsg();
		String jobName="未获得任务名称";
		if(paramJob!=null){
			jobName=paramJob.getJobName();
		}
		//参数是否合法
		paramMsg=paramUtil.isAddParamLegal(request);
		if(paramMsg.isSig()){
			try {
				//该任务不存在则不能修改
				job=jobService.getJobByName(jobName);
				if(job==null){
					msg.setMsg("修改任务["+jobName+"]失败，错误信息为:[该任务不存在，无法修改]");
					msg.setSig(false);
					pw.print(gson.toJson(msg));
					return;
				}
				//任务存在，将任务写入到数据库
				Date date=new Date();
				job.setJobStatus(1);
				job.setCronExpression(paramUtil.getJobInternal());
				job.setIndexPath(paramJob.getIndexPath());
				job.setSqlDb(paramJob.getSqlDb());
				job.setSqlIp(paramJob.getSqlIp());
				job.setSqlPort(paramJob.getSqlPort());
				job.setSqlPw(paramJob.getSqlPw());
				job.setSqlTable(paramJob.getSqlTable());
				job.setSqlUser(paramJob.getSqlUser());
				job.setUpdateTime(new Date());
				jobService.updateTask(job);
				//修改字段信息，只能根据字段名称+jobid查询，存在则修改，不存在则保存
				gwFieldList=gwFieldService.getFieldListByJobId(job.getJobId());
				HashMap<String,GwField> gwFieldMap=new HashMap<String,GwField>();
				for(GwField field:gwFieldList){
					gwFieldMap.put(field.getName(), field);
				}
				for(GwField paramField:paramGwFieldList){
					//原先存在此字段且内容不一致则update
					GwField gwField=gwFieldMap.get(paramField.getName());
					if(gwField!=null&&!gwField.equals(paramField)){
						gwField.setKey(paramField.isKey());
						gwField.setName(paramField.getName());
						gwField.setType(paramField.getType());
						gwFieldService.update(gwField);
					}
					//原先不存在此字段则保存
					if(gwField==null){
						paramField.setJobId(job.getJobId());
						gwFieldService.save(paramField);
					}
				}
				msg.setMsg("修改任务["+jobName+"]成功");
				msg.setSig(true);
				pw.print(gson.toJson(msg));
				return;
			} catch (Exception e) {
				e.printStackTrace();
				msg.setMsg("修改["+jobName+"]任务失败，错误信息为:["+e.getMessage()+"]");
				msg.setSig(false);
				pw.print(gson.toJson(msg));
				return;
			} 
		}else{
			msg.setMsg("修改任务["+jobName+"]失败，错误信息为:["+paramMsg.getMsg()+"]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
	}
	
	//查询所有job，不需要查询对应的字段表
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
	//查询指定任务，及其任务的配置内容：表名、库名等。以及对应的字段表
	@RequestMapping(value="getJobAndFields.do")
	public void getJob(HttpServletRequest request, HttpServletResponse response){
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
			msg.setMsg("获得id=["+jobId+"]任务失败,错误信息为[转换jobID类型失败]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
		JobInf job=jobService.getJobById(jobId);
		List<GwField> gwFieldList=null;
//		JobInf proxyJob=new JobInf(jobId,job.getJobName(),job.getJobStatus(),job.getCronExpression(),job.getBeanClass(),job.getDescription(),job.getTriggerName(),job.getCreateTime(),job.getUpdateTime());
		if(jobId!=0&&job!=null){
			//获得job关联的任务
			gwFieldList=gwFieldService.getFieldListByJobId(jobId);
			HashMap<String,Object>map=new HashMap<String,Object>();
			map.put("jobinf", job);
			map.put("config", gwFieldList);
			msg.setMsg(map);
			msg.setSig(true);
			pw.print(gson.toJson(msg));
		}else{
			msg.setMsg("获得id=["+jobId+"]任务失败,错误信息为[未获得任务ID或不存在指定的任务]");
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
