package com.jt.gateway.service.operation;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.jt.bean.gateway.DataField;
import com.jt.bean.gateway.GwConfig;
import com.jt.bean.gateway.JobInf;
import com.jt.bean.gateway.JobLog;
import com.jt.gateway.dao.impl.JdbcDaoImpl;
import com.jt.gateway.service.job.JobInfService;
import com.jt.gateway.service.job.JobLogService;
import com.jt.gateway.service.job.JobRunningLogImpl;
import com.jt.gateway.service.job.JobRunningLogService;
import com.jt.gateway.util.FileUtil;
import com.jt.lucene.IndexDao;
/**
 * 用于抽取指定关系型数据库中的数据，到指定的全文检索路径下
 * @author zhengxiaobin
 *
 */
public class IndexTask extends ApplicationObjectSupport implements Job{
	
	private Logger logger= Logger.getLogger(IndexTask.class);

	private GwConfigService configService;
	private GwConfig config;
	private JdbcDaoImpl JdbcDao ;
	private IndexDao indexDao;
	private int batchSize=5000;
	private String newIndexPath;
	private JobInfService jobService;
	private JobLogService jobLogService;
	private JobInf job;
	private JobRunningLogService jobRunningLogService;
	
	public void init4Quartz(String taskName) throws Exception{
		WebApplicationContext webContext = ContextLoader.getCurrentWebApplicationContext();
		jobRunningLogService=(JobRunningLogService) webContext.getBean("jobRunningLogImpl");
		jobService=(JobInfService) webContext.getBean("jobInfImpl"); 
		jobLogService=(JobLogService) webContext.getBean("jobLogImpl");
		configService=new GwConfigService();
		config=configService.getConfig(taskName);
		if(config==null){
			throw new Exception("config为null");
		}
		JdbcDao = new JdbcDaoImpl(config.getSqlDB(),config.getSqlIP(),config.getSqlPort(),config.getSqlUser(),config.getSqlPw());
		newIndexPath=config.getIndexPath()+"_new";
		indexDao=new IndexDao(newIndexPath);
	}
	//执行任务，不允许两个线程同时调用此方法
	public synchronized JobLog createIndex() throws Exception {
		int jobSize=0;
		JobLog log=new JobLog();
		IndexStatus status=IndexStatus.getStatus();
		Long timeWait=0l;
		String sql="select max("+config.getIdName()+") as maxid from "+config.getSqlTable();
		Map<String,Object> rsMap=null;
		long maxId;
		long minId;
		long temp=0l;
		
		//初始化日志
		log.setJobId(job.getJobId());
		log.setStart(new Date());
		log.setStatus(0);
		
		
		List<DataField> list=config.getList();
		if(list.size()==0){
			throw new Exception("获得同步字段长度为空");
		}
		logger.info("开始生成新的索引文件");
		jobRunningLogService.addRunningLog(job.getJobId(), "开始生成新的索引文件");
		//删除索引
		File file=new File(newIndexPath);
		if(file.exists()){
			if(FileUtil.deleteDir(file)){
				file.mkdir();
			}else{
				logger.error("删除文件"+file.getAbsolutePath()+"错误，请检查");
				jobRunningLogService.addRunningLog(job.getJobId(), "删除文件"+file.getAbsolutePath()+"错误，请检查");
				throw new Exception("删除文件"+file.getName()+"错误，请检查");
			}
		}
		//获得数据总数，并按照batchSize分批写入索引
		rsMap=JdbcDao.executeQueryForMap(sql);
		maxId=Long.parseLong(rsMap.get("maxid")+"");
		
		//获得最小值
		sql="select min("+config.getIdName()+") as minid from "+config.getSqlTable();
		rsMap=JdbcDao.executeQueryForMap(sql);
		minId=Long.parseLong(rsMap.get("minid")+"");
		logger.debug("minId=["+minId+"]");
		logger.info("ID区间为["+minId+"-"+maxId+"]");
		jobRunningLogService.addRunningLog(job.getJobId(), "ID区间为["+minId+"-"+maxId+"]");

		//第一次推送的结尾ID
		temp=minId+batchSize;
		//拼接sql
		sql="select ";
		for(int i=0;i<list.size();i++){
			DataField df=list.get(i);
			if(i==0){
				sql+=df.getName();
			}else{
				sql+=","+df.getName();
			}
		}
		do {
			logger.info("开始推送ID小于"+temp+"的数据");
			jobRunningLogService.addRunningLog(job.getJobId(), "开始推送ID小于"+temp+"的数据");
			String curSql=sql+" from "+config.getSqlTable()+" where "+config.getIdName()+"<"+temp;
			logger.debug("curSql=["+curSql+"]");
			List<Map<String,Object>> rsList=JdbcDao.executeQueryForList(curSql);
			for(Map<String,Object> map:rsList){
				Document doc=new  Document();
				for(DataField df:list){
					//数据库中某些字段为空则跳过，
					if(map.get(df.getName().toUpperCase())==null){
						continue;
					}
					logger.debug("value=["+map.get(df.getName().toUpperCase()).toString()+"]"
							+ " type= ["+df.getType()+"]");
					doc.add(new Field(df.getName(), map.get(df.getName().toUpperCase()).toString(), df.getFieldType()));
				}
				indexDao.save(doc);
				//记录推送的总数
				jobSize++;
			}
			logger.info("结束推送ID小于"+temp+"的数据");
			jobRunningLogService.addRunningLog(job.getJobId(), "结束推送ID小于"+temp+"的数据");

			temp+=batchSize;
		}while(temp<(maxId+batchSize));
		
		logger.info("生成新索引文件结束，尝试锁定替换原索引文件");
		jobRunningLogService.addRunningLog(job.getJobId(), "生成新索引文件结束，尝试锁定替换原索引文件");

		//将可用置为false
		status.setSearchEnable(false);
		//判断检索线程数，如果为0则直接修改索引目录名称，否则循环等待5分钟，超时报错
		while(timeWait<(5*60*1000)){
			if(status.getSearchThread()!=0){
				logger.info("检索线程数不为0，已等待"+(timeWait/1000*60)+"分钟，继续等待1分钟");
				jobRunningLogService.addRunningLog(job.getJobId(), "检索线程数不为0，已等待"+(timeWait/1000*60)+"分钟，继续等待1分钟");
				Thread.sleep(60000);
				timeWait+=60000;
			}else{
				logger.info("锁定原索引文件成功");
				jobRunningLogService.addRunningLog(job.getJobId(), "锁定原索引文件成功");
				File indexPath=new File(config.getIndexPath());
				indexPath.delete();
				file.renameTo(indexPath);
				break;
			}
		}
		
		//无论是否成功，均放开检索，并删除临时索引目录
		status.setSearchEnable(true);
		file=new File(newIndexPath);
		FileUtil.deleteDir(file);
		if(timeWait>=(5*60*1000)){
			logger.info("等待超过5分钟，抛出异常");
			jobRunningLogService.addRunningLog(job.getJobId(), "等待超过5分钟，抛出异常");
			throw new Exception("等待超过5分钟，抛出异常");
		}else{
			logger.info("推送成功");
			jobRunningLogService.addRunningLog(job.getJobId(), "推送成功");
		}
		log.setEnd(new Date());
		log.setExeTime(log.getEnd().getTime()-log.getStart().getTime());
		log.setIndexSize(jobSize);
		//1为成功，0为失败
		log.setStatus(1);
		return log;
	}
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobLog log=null;
		try {
			job=(JobInf)(context.getJobDetail().getJobDataMap().get("param"));
			init4Quartz(job.getJobName());
			logger.info("开始执行任务["+config.getTaskName()+"]");
			//写入内存日志，先清空再写入
			jobRunningLogService.clearRunningLog(job.getJobId());
			jobRunningLogService.addRunningLog(job.getJobId(), "开始执行任务["+config.getTaskName()+"]");
			
			jobService.setJobStatus(job.getJobId(), 2);
			log=createIndex();
			//执行后保存本次的运行状况
			if(log.getStatus()!=1){
				log.setStatus(0);
			}
			jobLogService.saveLog(log);
		} catch (Exception e) {
			if(log==null){
				log.setJobId(job.getJobId());
				log.setStart(new Date());
				log.setStatus(0);
				jobLogService.saveLog(log);
			}
			logger.error("任务["+config.getTaskName()+"]执行失败");
			logger.error(e);
			jobRunningLogService.addRunningLog(job.getJobId(), "任务["+config.getTaskName()+"]执行失败");
			jobRunningLogService.addRunningLog(job.getJobId(), e.getMessage());
			e.printStackTrace();
		}finally{
			//执行结束状态改为1，启动任务时状态已改为2
			//无论如何失败，都必须将任务状态置为1，否则下次无法进入
			if(jobService!=null){
				jobService.setJobStatus(job.getJobId(), 1);
			}
		}
		logger.info("结束执行任务["+config.getTaskName()+"]");
		jobRunningLogService.addRunningLog(job.getJobId(), "结束执行任务["+config.getTaskName()+"]");
	}
}
