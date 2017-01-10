package com.jt.gateway.service.operation;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.jt.bean.gateway.GwField;
import com.jt.bean.gateway.JobInf;
import com.jt.bean.gateway.JobLog;
import com.jt.gateway.dao.impl.JdbcDaoImpl;
import com.jt.gateway.service.job.GwFieldService;
import com.jt.gateway.service.job.JobInfService;
import com.jt.gateway.service.job.JobLogService;
import com.jt.gateway.service.job.JobRunningLogService;
import com.jt.gateway.util.FileUtil;
import com.jt.lucene.IndexDao;

/**
 * 用于抽取指定关系型数据库中的数据，到指定的全文检索路径下
 * 
 * @author zhengxiaobin
 *
 */
public class IndexTask extends ApplicationObjectSupport implements Job {

	private Logger logger = Logger.getLogger(IndexTask.class);

	private JdbcDaoImpl JdbcDao;
	private IndexDao indexDao;
	private int batchSize = 1000;
	private String newIndexPath;
	private JobInfService jobService;
	private JobLogService jobLogService;
	private JobInf job;
	private JobRunningLogService jobRunningLogService;
	private GwFieldService gwFieldService;

	public void init4Quartz(JobInf job) throws Exception {
		this.job = job;
		WebApplicationContext webContext = ContextLoader.getCurrentWebApplicationContext();
		jobRunningLogService = (JobRunningLogService) webContext.getBean("jobRunningLogImpl");
		jobService = (JobInfService) webContext.getBean("jobInfImpl");
		jobLogService = (JobLogService) webContext.getBean("jobLogImpl");
		gwFieldService = (GwFieldService) webContext.getBean("gwFieldImpl");
		JdbcDao = new JdbcDaoImpl(job.getSqlDb(), job.getSqlIp(), job.getSqlPort() + "", job.getSqlUser(),
				job.getSqlPw());
		newIndexPath = job.getIndexPath() + "_new";
		indexDao = new IndexDao(newIndexPath);
	}

	// 不依赖spring框架，本地测试使用
	public void init4QuartzTest(String jobName) throws Exception {
		this.job = jobService.getJobByName(jobName);
		JdbcDao = new JdbcDaoImpl(job.getSqlDb(), job.getSqlIp(), job.getSqlPort() + "", job.getSqlUser(),
				job.getSqlPw());
		newIndexPath = job.getIndexPath() + "_new";
		indexDao = new IndexDao(newIndexPath);
	}

	// 执行任务，不允许两个线程同时调用此方法
	public synchronized JobLog createIndex() throws Exception {
		int jobSize = 0;
		JobLog log = new JobLog();
		IndexStatus status = IndexStatus.getStatus();
		Long timeWait = 0l;
		String sql = "select max(" + gwFieldService.getIdField(job.getJobId()).getName() + ") as maxid from "
				+ job.getSqlTable();
		Map<String, Object> rsMap = null;
		//推送的源表最大的ID
		long maxId;
		//推送的源表最小的ID
		long minId;
		//按照batchSize分批次推送，本批次推送的最小ID
		long curMinId;
		//按照batchSize分批次推送，本批次推送的最大ID
		long curMaxId;

		// 初始化日志
		log.setJobId(job.getJobId());
		log.setStart(new Date());
		log.setStatus(0);

		List<GwField> list = gwFieldService.getFieldListByJobId(job.getJobId());
		if (list.size() == 0) {
			throw new Exception("获得同步字段长度为空");
		}

		// 删除索引
		File file = new File(newIndexPath);
		if (file.exists()) {
			if (FileUtil.deleteDir(file)) {
				file.mkdir();
			} else {
				logger.error("删除文件" + file.getAbsolutePath() + "错误，请检查");
				jobRunningLogService.addRunningLog(job.getJobId(), "删除文件" + file.getAbsolutePath() + "错误，请检查");
				throw new Exception("删除文件" + file.getName() + "错误，请检查");
			}
		}
		logger.info("开始在目录[" + file.getAbsolutePath() + "]生成新的索引文件");
		jobRunningLogService.addRunningLog(job.getJobId(), "开始生成新的索引文件");
		// 获得数据总数，并按照batchSize分批写入索引
		rsMap = JdbcDao.executeQueryForMap(sql);
		maxId = Long.parseLong(rsMap.get("MAXID") + "");

		// 获得最小值
		sql = "select min(" + gwFieldService.getIdField(job.getJobId()).getName() + ") as minid from "
				+ job.getSqlTable();
		rsMap = JdbcDao.executeQueryForMap(sql);
		minId = Long.parseLong(rsMap.get("MINID") + "");
		logger.debug("minId=[" + minId + "]");
		logger.info("ID区间为[" + minId + "-" + maxId + "]");
		jobRunningLogService.addRunningLog(job.getJobId(), "ID区间为[" + minId + "-" + maxId + "]");

		// 第一次推送的结尾ID
		curMaxId = minId + batchSize;
		curMinId = minId;
		// 拼接sql
		sql = "select ";
		for (int i = 0; i < list.size(); i++) {
			GwField df = list.get(i);
			if (i == 0) {
				sql += df.getName();
			} else {
				sql += "," + df.getName();
			}
		}
		do {
			logger.info("开始推送ID区间于[" + curMinId + "-"+curMaxId+"]的数据");
			jobRunningLogService.addRunningLog(job.getJobId(), "开始推送ID区间于[" + curMinId + "-"+curMaxId+"]的数据");
			List<Document> batchDocumentList=new ArrayList<Document>();
			
			String curSql = sql + " from " + job.getSqlTable() + " where "
					+ gwFieldService.getIdField(job.getJobId()).getName() + ">=" + curMinId
					+ " and "
					+ gwFieldService.getIdField(job.getJobId()).getName() + "<" + curMaxId;
			logger.info("curSql=[" + curSql + "]");
			List<Map<String, Object>> rsList = JdbcDao.executeQueryForList(curSql);
			
			//获得job指定表的所有字段名称和类型，便于后续排序使用
			Map<String,String> columnTypeMap=getColumnTypeMap(job.getSqlTable());
			
			for (Map<String, Object> map : rsList) {
				Document doc = new Document();
				for (GwField df : list) {
					// 数据库中某些字段为空则跳过，
					if (map.get(df.getName()) == null) {
						logger.info(df.getName()+"字段为空，跳过");
						continue;
					}
					logger.debug("value=[" + map.get(df.getName()).toString() + "]" + " type= ["
							+ df.getType() + "]");
					//判断字段类型，如果是long类型则调用特殊的field，便于后续检索和排序
					if("bigint".equalsIgnoreCase(columnTypeMap.get(df.getName()))||
							"int".equalsIgnoreCase(columnTypeMap.get(df.getName()))){
						try {
							indexDao.addLongPoint(doc, df.getName().toLowerCase(),Long.parseLong( map.get(df.getName()).toString()));
						} catch (Exception e) {
							e.printStackTrace();
							logger.error("转换long类型错误 id=["+map.get(df.getName())+"]");
						}
					}else if("datetime".equalsIgnoreCase(columnTypeMap.get(df.getName()))||
							"date".equalsIgnoreCase(columnTypeMap.get(df.getName()))||
							"timestamp".equalsIgnoreCase(columnTypeMap.get(df.getName()))){
						try {
							SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
							indexDao.addLongPoint(doc, df.getName().toLowerCase(),sdf.parse(map.get(df.getName()).toString()).getTime());
						} catch (Exception e) {
							e.printStackTrace();
							e.printStackTrace();
							logger.error("转换long/date类型错误 id=["+df.getName().toLowerCase()+"]");
						}
					}else{
						doc.add(new Field(df.getName().toLowerCase(), map.get(df.getName()).toString(),
								df.getFieldType()));
					}
				}
				//压入list，后续统一写入索引文件
				batchDocumentList.add(doc);
				logger.debug("保存文档[" + doc.toString() + "]");
			}
			jobSize+=indexDao.batchSave(batchDocumentList);
			logger.info("结束推送ID区间于[" + curMinId + "-"+curMaxId+"]的数据");
			jobRunningLogService.addRunningLog(job.getJobId(), "结束推送ID区间于[" + curMinId + "-"+curMaxId+"]的数据");
			
			curMinId = curMaxId;
			curMaxId += batchSize;
			
		} while (curMaxId < (maxId + batchSize));

		logger.info("生成新索引文件结束，尝试锁定替换原索引文件");
		jobRunningLogService.addRunningLog(job.getJobId(), "生成新索引文件结束，尝试锁定替换原索引文件");

		// 将可用置为false
		status.setSearchEnable(false);
		// 判断检索线程数，如果为0则直接修改索引目录名称，否则循环等待5分钟，超时报错
		while (timeWait < (5 * 60 * 1000)) {
			if (status.getSearchThread() != 0) {
				logger.info("检索线程数不为0，已等待" + (timeWait / 1000 * 60) + "分钟，继续等待1分钟");
				jobRunningLogService.addRunningLog(job.getJobId(),
						"检索线程数不为0，已等待" + (timeWait / 1000 * 60) + "分钟，继续等待1分钟");
				Thread.sleep(60000);
				timeWait += 60000;
			} else {
				File indexPath = new File(job.getIndexPath());
				logger.info("锁定原索引文件[" + indexPath.getAbsolutePath() + "]成功");
				jobRunningLogService.addRunningLog(job.getJobId(), "锁定原索引文件成功");
				// renameto方法不可靠，换FileUtils类
				FileUtils.deleteDirectory(indexPath);
				FileUtils.copyDirectory(file, indexPath);
				FileUtils.deleteDirectory(file);
				// indexPath.delete();
				// file.renameTo(indexPath);
				break;
			}
		}

		// 无论是否成功，均放开检索，并删除临时索引目录
		status.setSearchEnable(true);
		// file=new File(newIndexPath);
		// FileUtil.deleteDir(file);
		if (timeWait >= (5 * 60 * 1000)) {
			logger.info("等待超过5分钟，抛出异常");
			jobRunningLogService.addRunningLog(job.getJobId(), "等待超过5分钟，抛出异常");
			throw new Exception("等待超过5分钟，抛出异常");
		} else {
			logger.info("推送成功");
			jobRunningLogService.addRunningLog(job.getJobId(), "推送成功");
		}
		log.setEnd(new Date());
		log.setExeTime(log.getEnd().getTime() - log.getStart().getTime());
		log.setIndexSize(jobSize);
		// 1为成功，0为失败
		log.setStatus(1);
		return log;
	}

	/**
	 * 本地测试使用 执行任务，不允许两个线程同时调用此方法
	 * 
	 * @param taskName
	 * @throws Exception
	 */
	public synchronized void createIndexTest(String taskName) throws Exception {
		init4QuartzTest(taskName);
		int jobSize = 0;
		IndexStatus status = IndexStatus.getStatus();
		Long timeWait = 0l;
		String sql = "select max(" + gwFieldService.getIdField(job.getJobId()).getName() + ") as maxid from "
				+ job.getSqlTable();
		Map<String, Object> rsMap = null;
		long maxId;
		long minId;
		long temp = 0l;

		List<GwField> list = gwFieldService.getFieldListByJobId(job.getJobId());
		if (list.size() == 0) {
			throw new Exception("获得同步字段长度为空");
		}

		// 删除索引
		File file = new File(newIndexPath);
		if (file.exists()) {
			if (FileUtil.deleteDir(file)) {
				file.mkdir();
			} else {
				logger.error("删除文件" + file.getAbsolutePath() + "错误，请检查");
				throw new Exception("删除文件" + file.getName() + "错误，请检查");
			}
		}
		logger.info("开始在目录[" + file.getAbsolutePath() + "]生成新的索引文件");
		// 获得数据总数，并按照batchSize分批写入索引
		rsMap = JdbcDao.executeQueryForMap(sql);
		maxId = Long.parseLong(rsMap.get("MAXID") + "");

		// 获得最小值
		sql = "select min(" + gwFieldService.getIdField(job.getJobId()).getName() + ") as minid from "
				+ job.getSqlTable();
		rsMap = JdbcDao.executeQueryForMap(sql);
		minId = Long.parseLong(rsMap.get("MINID") + "");
		logger.debug("minId=[" + minId + "]");
		logger.info("ID区间为[" + minId + "-" + maxId + "]");

		// 第一次推送的结尾ID
		temp = minId + batchSize;
		// 拼接sql
		sql = "select ";
		for (int i = 0; i < list.size(); i++) {
			GwField df = list.get(i);
			if (i == 0) {
				sql += df.getName();
			} else {
				sql += "," + df.getName();
			}
		}
		do {
			logger.info("开始推送ID小于" + temp + "的数据");
			String curSql = sql + " from " + job.getSqlTable() + " where "
					+ gwFieldService.getIdField(job.getJobId()).getName() + "<" + temp;
			logger.debug("curSql=[" + curSql + "]");
			List<Map<String, Object>> rsList = JdbcDao.executeQueryForList(curSql);
			for (Map<String, Object> map : rsList) {
				jobSize++;
				// if(jobSize>50)break;
				Document doc = new Document();
				for (GwField df : list) {
					// 数据库中某些字段为空则跳过，
					if (map.get(df.getName()) == null) {
						continue;
					}
					logger.debug("value=[" + map.get(df.getName()).toString() + "]" + " type= ["
							+ df.getType() + "]");
					doc.add(new Field(df.getName().toLowerCase(), map.get(df.getName()).toString(),
							df.getFieldType()));
				}
				
				logger.info("保存文档[" + doc.toString() + "]");
				indexDao.save(doc);
				// 记录推送的总数
			}
			logger.info("结束推送ID小于" + temp + "的数据");

			temp += batchSize;
		} while (temp < (maxId + batchSize));

		logger.info("生成新索引文件结束，尝试锁定替换原索引文件");

		// 将可用置为false
		status.setSearchEnable(false);
		// 判断检索线程数，如果为0则直接修改索引目录名称，否则循环等待5分钟，超时报错
		while (timeWait < (5 * 60 * 1000)) {
			if (status.getSearchThread() != 0) {
				logger.info("检索线程数不为0，已等待" + (timeWait / 1000 * 60) + "分钟，继续等待1分钟");
				Thread.sleep(60000);
				timeWait += 60000;
			} else {
				File indexPath = new File(job.getIndexPath());
				logger.info("锁定原索引文件[" + indexPath.getAbsolutePath() + "]成功");
				// renameto方法不可靠，换FileUtils类
				FileUtils.deleteDirectory(indexPath);
				FileUtils.copyDirectory(file, indexPath);
				FileUtils.deleteDirectory(file);
				// indexPath.delete();
				// file.renameTo(indexPath);
				break;
			}
		}

		// 无论是否成功，均放开检索，并删除临时索引目录
		status.setSearchEnable(true);
		// file=new File(newIndexPath);
		// FileUtil.deleteDir(file);
		if (timeWait >= (5 * 60 * 1000)) {
			logger.info("等待超过5分钟，抛出异常");
			throw new Exception("等待超过5分钟，抛出异常");
		} else {
			logger.info("推送成功");
		}
	}

	public void doExecute() {
		JobLog log = null;
		try {
			init4Quartz(job);
			logger.info("开始执行任务[" + job.getJobName() + "]");
			// 写入内存日志，先清空再写入
			jobRunningLogService.clearRunningLog(job.getJobId());
			jobRunningLogService.addRunningLog(job.getJobId(), "开始执行任务[" + job.getJobName() + "]");

			jobService.setJobStatus(job.getJobId(), 2);
			log = createIndex();
			if (log != null) {
				// 执行后保存本次的运行状况
				if (log.getStatus() != 1) {
					log.setStatus(0);
				}
				jobLogService.saveLog(log);
			} else {
				log = new JobLog();
				log.setJobId(job.getJobId());
				log.setStart(new Date());
				log.setStatus(0);
				jobLogService.saveLog(log);
			}
		} catch (Exception e) {
			if (log == null) {
				log = new JobLog();
				log.setJobId(job.getJobId());
				log.setStart(new Date());
				log.setStatus(0);
				jobLogService.saveLog(log);
			}
			logger.error("任务[" + job.getJobName() + "]执行失败");
			logger.error(e);
			jobRunningLogService.addRunningLog(job.getJobId(), "任务[" + job.getJobName() + "]执行失败");
			jobRunningLogService.addRunningLog(job.getJobId(), e.getMessage());
			e.printStackTrace();
		} finally {
			// 执行结束状态改为1，启动任务时状态已改为2
			// 无论如何失败，都必须将任务状态置为1，否则下次无法进入
			if (jobService != null) {
				jobService.setJobStatus(job.getJobId(), 1);
			}
		}
		logger.info("结束执行任务[" + job.getJobName() + "]");
		jobRunningLogService.addRunningLog(job.getJobId(), "结束执行任务[" + job.getJobName() + "]");
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		this.job = (JobInf) (context.getJobDetail().getJobDataMap().get("param"));
		doExecute();
		// JobLog log=null;
		// try {
		// job=(JobInf)(context.getJobDetail().getJobDataMap().get("param"));
		// init4Quartz(job.getJobName());
		// logger.info("开始执行任务["+config.getTaskName()+"]");
		// //写入内存日志，先清空再写入
		// jobRunningLogService.clearRunningLog(job.getJobId());
		// jobRunningLogService.addRunningLog(job.getJobId(),
		// "开始执行任务["+config.getTaskName()+"]");
		//
		// jobService.setJobStatus(job.getJobId(), 2);
		// log=createIndex();
		// if(log!=null){
		// //执行后保存本次的运行状况
		// if(log.getStatus()!=1){
		// log.setStatus(0);
		// }
		// jobLogService.saveLog(log);
		// }else{
		// log=new JobLog();
		// log.setJobId(job.getJobId());
		// log.setStart(new Date());
		// log.setStatus(0);
		// jobLogService.saveLog(log);
		// }
		// } catch (Exception e) {
		// if(log==null){
		// log=new JobLog();
		// log.setJobId(job.getJobId());
		// log.setStart(new Date());
		// log.setStatus(0);
		// jobLogService.saveLog(log);
		// }
		// logger.error("任务["+config.getTaskName()+"]执行失败");
		// logger.error(e);
		// jobRunningLogService.addRunningLog(job.getJobId(),
		// "任务["+config.getTaskName()+"]执行失败");
		// jobRunningLogService.addRunningLog(job.getJobId(), e.getMessage());
		// e.printStackTrace();
		// }finally{
		// //执行结束状态改为1，启动任务时状态已改为2
		// //无论如何失败，都必须将任务状态置为1，否则下次无法进入
		// if(jobService!=null){
		// jobService.setJobStatus(job.getJobId(), 1);
		// }
		// }
		// logger.info("结束执行任务["+config.getTaskName()+"]");
		// jobRunningLogService.addRunningLog(job.getJobId(),
		// "结束执行任务["+config.getTaskName()+"]");
	}

	public JobInf getJob() {
		return job;
	}

	public void setJob(JobInf job) {
		this.job = job;
	}
	
	//获得指定表的字段类型
	private Map<String,String> getColumnTypeMap(String tableName) throws ClassNotFoundException, SQLException{
		
		String sql_column = "select column_name,data_type from information_schema.`COLUMNS` where"
				+" table_name='"+tableName+"'" ;
		List<Map<String, Object>> rsList_column = JdbcDao.executeQueryForList(sql_column);
		
		Map<String,String> map=new HashMap<String,String>();
		for(Map<String,Object> curRow:rsList_column){
			map.put(curRow.get("COLUMN_NAME").toString().toUpperCase(),curRow.get("DATA_TYPE").toString());
		}
		return map;
	}

	public static void main(String[] args) {
		IndexTask task = new IndexTask();
		try {
			task.createIndexTest("数据推送");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
