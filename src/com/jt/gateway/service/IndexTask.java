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
 * ���ڳ�ȡָ����ϵ�����ݿ��е����ݣ���ָ����ȫ�ļ���·����
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
	
	//ִ�����񣬲����������߳�ͬʱ���ô˷���
	public synchronized void createIndex() throws Exception {
		IndexStatus status=IndexStatus.getStatus();
		Long timeWait=0l;
		String sql="select max("+config.getIdName()+") as maxid from "+config.getDbName();
		Map<String,Object> rsMap=null;
		long maxId;
		long minId;
		long temp=0l;
		List<DataField> list=config.getList();
		
		logger.info("��ʼ�����µ������ļ�");
		//ɾ������
		File file=new File(newIndexPath);
		if(file.exists()){
			if(FileUtil.deleteDir(file)){
				file.mkdir();
			}else{
				logger.error("ɾ���ļ�"+file.getAbsolutePath()+"��������");
				throw new Exception("ɾ���ļ�"+file.getName()+"��������");
			}
		}
		//�������������������batchSize����д������
		rsMap=JdbcDao.executeQueryForMap(sql);
		maxId=(long)rsMap.get("maxid");
		
		//�����Сֵ
		sql="select min("+config.getIdName()+") as minid from "+config.getDbName();
		rsMap=JdbcDao.executeQueryForMap(sql);
		minId=(long)rsMap.get("minid");
		logger.debug("minId=["+minId+"]");
		logger.info("ID����Ϊ["+minId+"-"+maxId+"]");
		//��һ�����͵Ľ�βID
		temp=minId+batchSize;
		//ƴ��sql
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
			logger.info("��ʼ����IDС��"+temp+"������");
			String curSql=sql+" from "+config.getDbName()+" where "+config.getIdName()+"<"+temp;
			logger.debug("curSql=["+curSql+"]");
			List<Map<String,Object>> rsList=JdbcDao.executeQueryForList(curSql);
			for(Map<String,Object> map:rsList){
				Document doc=null;
				for(DataField df:list){
					//���ݿ���ĳЩ�ֶ�Ϊ��������
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
			logger.info("��������IDС��"+temp+"������");
			
			temp+=batchSize;
		}while(temp<(maxId+batchSize));
		
		logger.info("�����������ļ����������������滻ԭ�����ļ�");
		//��������Ϊfalse
		status.setSearchEnable(false);
		//�жϼ����߳��������Ϊ0��ֱ���޸�����Ŀ¼���ƣ�����ѭ���ȴ�5���ӣ���ʱ����
		while(timeWait<(5*60*1000)){
			if(status.getSearchThread()!=0){
				logger.info("�����߳�����Ϊ0���ѵȴ�"+(timeWait/1000*60)+"���ӣ������ȴ�1����");
				Thread.sleep(60000);
				timeWait+=60000;
			}else{
				logger.info("����ԭ�����ļ��ɹ�");
				File indexPath=new File(config.getIndexPath());
				indexPath.delete();
				file.renameTo(indexPath);
				break;
			}
		}
		
		//�����Ƿ�ɹ������ſ���������ɾ����ʱ����Ŀ¼
		status.setSearchEnable(true);
		file=new File(newIndexPath);
		FileUtil.deleteDir(file);
		if(timeWait>=(5*60*1000)){
			logger.info("�ȴ�����5���ӣ��׳��쳣");
			throw new Exception("�ȴ�����5���ӣ��׳��쳣");
		}else{
			logger.info("���ͳɹ�");
		}
		
	}
	public static void main(String[] args) {
			try {
				IndexTask gate=new IndexTask("�����ʴ����ݳ�ȡ");
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
