package com.jt.gateway.service;

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
import com.jt.gateway.dao.JdbcDaoImpl_bak;
import com.jt.lucene.IndexDao;

/**
 * 用于抽取指定关系型数据库中的数据，到指定的全文检索路径下
 * 
 * @author zhengxiaobin
 *
 */
public class IndexTask_bak {

	private static Logger logger = Logger.getLogger(IndexTask_bak.class);

	private GwConfig config;
	private JdbcDaoImpl_bak JdbcDao;
	private IndexDao indexDao;
	private String taskName;
	private long beginId;
	private long endId;

	public long getBeginId() {
		return beginId;
	}

	public void setBeginId(long beginId) {
		this.beginId = beginId;
	}

	public long getEndId() {
		return endId;
	}

	public void setEndId(long endId) {
		this.endId = endId;
	}

	public IndexTask_bak(String taskName) throws IOException, JDOMException {
		config = GwXmlDao.getConfig(taskName);
		JdbcDao = new JdbcDaoImpl_bak();
		indexDao = new IndexDao(config.getIndexPath());
	}

	// 执行推送任务
	public void createIndex() throws ClassNotFoundException, SQLException {
		String sql = "select ";
		List<DataField> list = config.getList();
		// 拼接sql
		for (int i = 0; i < list.size(); i++) {
			DataField df = list.get(i);
			if (i == 0) {
				sql += df.getName();
			} else {
				sql += "," + df.getName();
			}
		}
		sql =sql+ " from " + config.getDbName() + " where " + config.getIdName() + ">" + beginId + " and "
				+ config.getIdName() + " <=" + endId;
		logger.debug("sql=[" + sql + "]");
		List<Map<String, Object>> rsList = JdbcDao.executeQueryForList(sql);
		for (Map<String, Object> map : rsList) {
			Document doc = null;
			for (DataField df : list) {
				// 数据库中某些字段为空则跳过
				if (map.get(df.getName()) == null) {
					continue;
				}
				doc = new Document();
				logger.debug("value=[" + map.get(df.getName()).toString() + "]" + " type= [" + df.getType() + "]");
				doc.add(new Field(df.getName(), map.get(df.getName()).toString(), df.getType()));
			}
			indexDao.save(doc);
		}

	}
	
	public long getMaxId(){
		long maxId=0l;
		String sql="select max("+config.getIdName()+") as maxid from "+config.getDbName();
		Map<String,Object> rsMap=null;
		//获得数据总数，并按照batchSize分批写入索引
		try {
			rsMap=JdbcDao.executeQueryForMap(sql);
			maxId=(long)rsMap.get("maxid");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return maxId;
	}
	
	public void delAllIndex(){
		//删除索引
		indexDao.deleteAll();
	}
	
	public void delIndex(long beginId){
		//删除索引
		indexDao.delAfterId(beginId, config.getIdName());
	}




	public static void main(String[] args) {
		 IndexTask_bak gate;
		try {
			gate = new IndexTask_bak("智能问答数据抽取");
			try {
				gate.createIndex();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
}
