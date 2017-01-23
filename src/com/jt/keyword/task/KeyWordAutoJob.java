package com.jt.keyword.task;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jt.keyword.bean.KeyWord;
import com.jt.keyword.service.KeyWordService;
import com.jt.keyword.util.ConfigManager;
import com.jt.keyword.util.DataSourceUtil;
import com.jt.lucene.Article;
import com.jt.nlp.service.LuceneSearchService;

/**
 * 定时任务
 * @作者 邹许红
 * @日期 2017-01-14
 *
 */
public class KeyWordAutoJob {
	private static final Logger LOG = LoggerFactory.getLogger(KeyWordAutoJob.class);

	private LuceneSearchService  searchService;
	private  KeyWordService keyWordService;
	public   int  page= 2000;
	
	/**
	 * 建立连接
	 * 
	 * @return
	 */
	public Connection createConnection() {
		String  jdbcUrl=ConfigManager.getValueByKey("jdbcUrl");
	    String	driverClass=ConfigManager.getValueByKey("driverClass");
	    String	username=ConfigManager.getValueByKey("username");
		String	passwd=ConfigManager.getValueByKey("passwd");
	  try {
			Class.forName(driverClass);

			return DriverManager.getConnection(jdbcUrl, username, passwd);
		} catch (SQLException e) {
			LOG.error(e.toString()+e);
			return null;
		} catch (ClassNotFoundException e) {
			LOG.error(e.toString()+e);
			return null;
		}

	}
	/**
	 * 更新数据到数据库
	 */
	public  void  updateData(){
		String  updateSql="update crawler_xq  set KEY_WORD=? where XQ_ID=?";
		Connection conn = DataSourceUtil.getConnection();
		PreparedStatement stmt=null;
		ResultSet st=null;
		try {
			stmt=conn.prepareStatement(updateSql);
			if(ParamUtil.parseWords!=null&&ParamUtil.parseWords.size()>0){
				for (int i = 0; i < ParamUtil.parseWords.size(); i++) {
                    stmt.setString(1, ParamUtil.parseWords.get(i).getWords());
                    stmt.setLong(2, ParamUtil.parseWords.get(i).getXqId());
                    stmt.addBatch();
                    if((i+1)%500==0){
                    	stmt.executeBatch();
                    }
					
				}
            	stmt.executeBatch();
				
			}
			
			
			
		} catch (SQLException e) {
			LOG.error(e.toString());
		}finally{
				try {
					if(st!=null)
					st.close();
					if(stmt!=null)
						stmt.close();
					if(conn!=null)
						conn.close();
					ParamUtil.parseWords=new ArrayList<ParseWord>();
				} catch (SQLException e) {
					LOG.error(e.toString());
				}
		}
		
		
		
	}
	
	public  List<Article>  query(int pagenum){
		List<Article> list=new ArrayList<Article>();
		String  sql="select XQ_ID,XQ_TITLE from  crawler_xq  limit ?,?";
		Connection conn =  DataSourceUtil.getConnection();
		PreparedStatement stmt=null;
		ResultSet st=null;
		try {
			stmt=conn.prepareStatement(sql);
			int begin=(pagenum-1)*page;
			stmt.setInt(1, begin);
			stmt.setInt(2, page);
			st=stmt.executeQuery();
			while (st.next()) {
				Article  article=new Article();
				article.setId(st.getLong("XQ_ID"));
				article.setTitle(st.getString("XQ_TITLE"));
				list.add(article);

			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}try {
			if(st!=null)
			st.close();
			if(stmt!=null)
				stmt.close();
			if(conn!=null)
				conn.close();
		} catch (SQLException e) {
			LOG.error(e.toString());
		}

		
		
		return list;
		
	}
	
	
	public   void  autoJobTask(){
		try {
            if(ParamUtil.isWordRun)
            	return;
			ParamUtil.isWordRun=true;

			LOG.info("开始打标任务");
			List<KeyWord> list = keyWordService.queryAll();
			ParamUtil.keywords=new ArrayList<KeyWord>();
			if(list!=null&&list.size()>0){
				ParamUtil.keywords=list;
			}
			int  page=1;
			while (true) {
				List<Article> articles = query(page);
			
			   if(articles==null||articles.size()==0){
				   break;
			   }else{
				   KeyWordTaskUtil.parse(articles);
			   }
				page++;
			}
			LOG.info("开始更新数据任务");
			updateData();
			LOG.info("结束更新数据任务");
			LOG.info("结束打标任务");
		} catch (Exception e) {
		}finally{
			ParamUtil.isWordRun=false;
		}
		
        
		
		
		
		
		
	}
	public LuceneSearchService getSearchService() {
		return searchService;
	}
	public void setSearchService(LuceneSearchService searchService) {
		this.searchService = searchService;
	}
	public KeyWordService getKeyWordService() {
		return keyWordService;
	}
	public void setKeyWordService(KeyWordService keyWordService) {
		this.keyWordService = keyWordService;
	}
	
	public static void main(String[] args) {
		KeyWordAutoJob  autoJob=new KeyWordAutoJob();
		autoJob.autoJobTask();
	}
	
	
	
	
}
