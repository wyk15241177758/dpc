package com.jt.gateway.service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.jdom.JDOMException;

import com.jt.bean.lucene.DataField;
import com.jt.bean.lucene.GwConfig;
import com.jt.gateway.dao.GwXmlDao;
import com.jt.gateway.dao.JdbcDaoImpl;
import com.jt.gateway.util.FileUtil;
import com.jt.lucene.IndexDao;
/**
 * 用于抽取指定关系型数据库中的数据，到指定的全文检索路径下
 * @author zhengxiaobin
 *
 */
public class IndexTask {
	
	private static Logger logger = Logger.getLogger(IndexTask.class);

	
	private GwConfig config;
	private JdbcDaoImpl JdbcDao ;
	private IndexDao indexDao;
	private int batchSize=5000;
	private String taskName;
	private String newIndexPath;
	

	public IndexTask(String taskName) throws IOException, JDOMException{
		config=GwXmlDao.getConfig(taskName);
		JdbcDao = new JdbcDaoImpl();
		newIndexPath=config.getIndexPath()+"_new";
		indexDao=new IndexDao(newIndexPath);
	}
	
	//执行任务，不允许两个线程同时调用此方法
	public synchronized void createIndex() throws Exception {
		IndexStatus status=IndexStatus.getStatus();
		Long timeWait=0l;
		String sql="select max("+config.getIdName()+") as maxid from "+config.getDbName();
		Map<String,Object> rsMap=null;
		long maxId;
		long minId;
		long temp=0l;
		List<DataField> list=config.getList();
		
		logger.info("开始生成新的索引文件");
		//删除索引
		File file=new File(newIndexPath);
		if(file.exists()){
			if(FileUtil.deleteDir(file)){
				file.mkdir();
			}else{
				logger.error("删除文件"+file.getAbsolutePath()+"错误，请检查");
				throw new Exception("删除文件"+file.getName()+"错误，请检查");
			}
		}
		//获得数据总数，并按照batchSize分批写入索引
		rsMap=JdbcDao.executeQueryForMap(sql);
		maxId=(long)rsMap.get("maxid");
		
		//获得最小值
		sql="select min("+config.getIdName()+") as minid from "+config.getDbName();
		rsMap=JdbcDao.executeQueryForMap(sql);
		minId=(long)rsMap.get("minid");
		logger.debug("minId=["+minId+"]");
		logger.info("ID区间为["+minId+"-"+maxId+"]");
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
			String curSql=sql+" from "+config.getDbName()+" where "+config.getIdName()+"<"+temp;
			logger.debug("curSql=["+curSql+"]");
			List<Map<String,Object>> rsList=JdbcDao.executeQueryForList(curSql);
			for(Map<String,Object> map:rsList){
				Document doc=null;
				for(DataField df:list){
					//数据库中某些字段为空则跳过
					if(map.get(df.getName())==null){
						continue;
					}
					doc=new  Document();
					logger.debug("value=["+map.get(df.getName()).toString()+"]"
							+ " type= ["+df.getType()+"]");
					doc.add(new Field(df.getName(), map.get(df.getName()).toString(), df.getType()));
				}
				indexDao.save(doc);
			}
			logger.info("结束推送ID小于"+temp+"的数据");
			
			temp+=batchSize;
		}while(temp<(maxId+batchSize));
		
		logger.info("生成新索引文件结束，尝试锁定替换原索引文件");
		//将可用置为false
		status.setSearchEnable(false);
		//判断检索线程数，如果为0则直接修改索引目录名称，否则循环等待5分钟，超时报错
		while(timeWait<(5*60*1000)){
			if(status.getSearchThread()!=0){
				logger.info("检索线程数不为0，已等待"+(timeWait/1000*60)+"分钟，继续等待1分钟");
				Thread.sleep(60000);
				timeWait+=60000;
			}else{
				logger.info("锁定原索引文件成功");
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
			throw new Exception("等待超过5分钟，抛出异常");
		}else{
			logger.info("推送成功");
		}
		
	}
	public static void main(String[] args) {
			try {
				IndexTask gate=new IndexTask("智能问答数据抽取");
				gate.createIndex();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JDOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		
		
		
//			try {
//				IndexTask gate=new IndexTask();
//				gate.createIndex_full();
//			} catch (ClassNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		
	}
}
