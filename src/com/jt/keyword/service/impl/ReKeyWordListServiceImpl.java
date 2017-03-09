package com.jt.keyword.service.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause.Occur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.jt.keyword.bean.CountResult;
import com.jt.keyword.bean.QueryResutList;
import com.jt.keyword.bean.XqResult;
import com.jt.keyword.service.RekeyWordListService;
import com.jt.keyword.task.KeyWordAutoJob;
import com.jt.keyword.task.ParamUtil;
import com.jt.keyword.task.ParseWord;
import com.jt.keyword.util.DataSourceUtil;
import com.jt.lucene.Article;
import com.jt.lucene.IndexDao;
import com.jt.lucene.LuceneUtilsGw;
@Service
public class ReKeyWordListServiceImpl implements  RekeyWordListService{
	private static final Logger LOG = LoggerFactory.getLogger(ReKeyWordListServiceImpl.class);

	private  IndexDao dao;

	public IndexDao getDao() {
		return dao;
	}

	@Resource(name="indexDao") 
	public void setDao(IndexDao dao) {
		this.dao = dao;
	}
	@Override
	public   List<CountResult>  flcountResults(String keyword){
		List<CountResult> list=new ArrayList<CountResult>();
    	String sql="SELECT COUNT(*) AS NUM,SJFL FROM `crawler_xq` where KEY_WORD LIKE  ?  GROUP BY  SJFL  " ;
    	Connection conn = DataSourceUtil.getConnection();
		PreparedStatement stmt=null;
		ResultSet st=null;
		try {
			stmt=conn.prepareStatement(sql);
			stmt.setString(1, "%"+keyword+"%");
			st=stmt.executeQuery();
			while (st.next()) {
				CountResult countResult=new CountResult();
				countResult.setWordValue(st.getString("SJFL"));
				countResult.setNum(st.getInt("NUM"));
				list.add(countResult);
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
		return list;
    }
	@Override
	public   List<CountResult>  flcountResults(String keyword,String source){
		List<CountResult> list=new ArrayList<CountResult>();
    	String sql="SELECT COUNT(*) AS NUM,SJFL FROM `crawler_xq` where ZD_NAME=? AND KEY_WORD LIKE  ?  GROUP BY  SJFL  " ;
    	Connection conn = DataSourceUtil.getConnection();
		PreparedStatement stmt=null;
		ResultSet st=null;
		try {
			stmt=conn.prepareStatement(sql);
			stmt.setString(1, source);

			stmt.setString(2, "%"+keyword+"%");
			st=stmt.executeQuery();
			while (st.next()) {
				CountResult countResult=new CountResult();
				countResult.setWordValue(st.getString("SJFL"));
				countResult.setNum(st.getInt("NUM"));
				list.add(countResult);
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
		return list;
    }
	@Override
	public   List<CountResult>  sourcecountResults(String keyword){
		List<CountResult> list=new ArrayList<CountResult>();
    	String sql="SELECT COUNT(*) AS NUM,ZD_NAME FROM `crawler_xq` where KEY_WORD LIKE  ?  GROUP BY  SJFL  " ;
    	Connection conn = DataSourceUtil.getConnection();
		PreparedStatement stmt=null;
		ResultSet st=null;
		try {
			stmt=conn.prepareStatement(sql);
			stmt.setString(1, "%"+keyword+"%");
			st=stmt.executeQuery();
			while (st.next()) {
				CountResult countResult=new CountResult();
				countResult.setWordValue(st.getString("ZD_NAME"));
				countResult.setNum(st.getInt("NUM"));
				list.add(countResult);
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
		return list;
    }
	@Override
	public   List<CountResult>  sourcecountResults(String keyword,String fl){
		List<CountResult> list=new ArrayList<CountResult>();
    	String sql="SELECT COUNT(*) AS NUM,ZD_NAME FROM `crawler_xq` where SJFL=? AND  KEY_WORD LIKE  ?  GROUP BY  SJFL  " ;
    	Connection conn = DataSourceUtil.getConnection();
		PreparedStatement stmt=null;
		ResultSet st=null;
		try {
			stmt=conn.prepareStatement(sql);
			stmt.setString(1, fl);
			stmt.setString(2, "%"+keyword+"%");
			st=stmt.executeQuery();
			while (st.next()) {
				CountResult countResult=new CountResult();
				countResult.setWordValue(st.getString("ZD_NAME"));
				countResult.setNum(st.getInt("NUM"));
				list.add(countResult);
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
		return list;
    }
	@Override
	public   List<CountResult>  countResults(String keyword,String fl,String source){
		List<CountResult> list=new ArrayList<CountResult>();
    	String sql="SELECT COUNT(*) AS NUM,ZD_NAME FROM `crawler_xq` where SJFL=? AND ZD_NAME=? KEY_WORD LIKE  ?  GROUP BY  SJFL  " ;
    	Connection conn = DataSourceUtil.getConnection();
		PreparedStatement stmt=null;
		ResultSet st=null;
		try {
			stmt=conn.prepareStatement(sql);
			stmt.setString(1, fl);
			stmt.setString(2, "%"+keyword+"%");
			st=stmt.executeQuery();
			while (st.next()) {
				CountResult countResult=new CountResult();
				countResult.setWordValue(st.getString("ZD_NAME"));
				countResult.setNum(st.getInt("NUM"));
				list.add(countResult);
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
		return list;
    }
	
	
	
	
	
	
	
	

	@Override
	public Map<String ,List<CountResult> > countResultsLC(String keyword) {
		Map<String, Integer>  fl=new HashMap<String, Integer>();
		Map<String, Integer>  source=new HashMap<String, Integer>();
		
     	String[] queryStr={"\""+keyword+"\""};
   		String [] searchField=new String[queryStr.length];
   		Occur[] occurs = new Occur[queryStr.length]; 
   		for(int i=0;i<searchField.length;i++){
   			searchField[i]="KEY_WORD";
   			occurs[i]=Occur.MUST;
   		}
   			
   		//排序参数
   		String[] sortField= {Article.getMapedFieldName("date")};
   		SortField.Type[] sortFieldType={SortField.Type.LONG};
   		boolean[] reverse={true};
   		boolean isRelevancy = true;
   		int i=0;
   		while (true) {
   			List<Document> list=dao.search(queryStr, occurs, searchField, sortField, sortFieldType, reverse, isRelevancy, i*1000,1000);
   	   		if(list==null||list.size()==0)
   	   			break;
   			for(Document a:list){
   				String flStr = a.get("SJFL");
   				String sourceStr =a.get("ZD_NAME");
   				if (fl.containsKey(flStr)) {
					fl.put(flStr, fl.get(flStr)+1);
				}else{
					fl.put(flStr, 1);
				}
   				if (source.containsKey(sourceStr)) {
   					source.put(sourceStr, source.get(sourceStr)+1);
				}else{
					source.put(sourceStr, 1);
				}
   	   		}
   	   		i++;
		}
   		List<CountResult>  fls=new ArrayList<CountResult>();
   		       for (Map.Entry<String, Integer> entry : fl.entrySet()) {
   		    	LOG.info("key= " + entry.getKey() + " and value= "+ entry.getValue());
   		    	fls.add(new CountResult(entry.getKey(),entry.getValue()));
   		       }
   		List<CountResult>  sources=new ArrayList<CountResult>();
   	      for (Map.Entry<String, Integer> entry : source.entrySet()) {
		    	LOG.info("key= " + entry.getKey() + " and value= "+ entry.getValue());
		    	sources.add(new CountResult(entry.getKey(),entry.getValue()));
		       }

		Map<String ,List<CountResult> > counts=new HashMap<String ,List<CountResult> >();
		counts.put("fls", fls);
	    counts.put("sources", sources);

		return counts;
	}

	@Override
	public 		Map<String ,List<CountResult> > flcountResultsLC(String keyword, String source) {
		Map<String, Integer>  fl=new HashMap<String, Integer>();
		Map<String, Integer>  zd=new HashMap<String, Integer>();
     	String[] queryStr={"\""+keyword+"\"","\""+source+"\""};
   		String [] searchField=new String[queryStr.length];
   		Occur[] occurs = new Occur[queryStr.length]; 
   		searchField[0]="KEY_WORD";
   		occurs[0]=Occur.MUST;
   	
   			searchField[1]="ZD_NAME";
   			occurs[1]=Occur.MUST;
   		//排序参数
   		String[] sortField= {Article.getMapedFieldName("date")};
   		SortField.Type[] sortFieldType={SortField.Type.LONG};
   		boolean[] reverse={true};
   		boolean isRelevancy = true;
   		int i=0;
   		while (true) {
   			List<Document> list=dao.search(queryStr, occurs, searchField, sortField, sortFieldType, reverse, isRelevancy, i*1000,1000);
   	   		if(list==null||list.size()==0)
   	   			break;
   			for(Document a:list){
   				String flStr = a.get("SJFL");
   				String sourceStr =a.get("ZD_NAME");
   				if (fl.containsKey(flStr)) {
					fl.put(flStr, fl.get(flStr)+1);
				}else{
					fl.put(flStr, 1);
				}
   				if (zd.containsKey(sourceStr)) {
   					zd.put(sourceStr, zd.get(sourceStr)+1);
				}else{
					zd.put(sourceStr, 1);
				}
   	   		}
   	   		i++;
		}
   		List<CountResult>  fls=new ArrayList<CountResult>();
   		       for (Map.Entry<String, Integer> entry : fl.entrySet()) {
   		    	LOG.info("key= " + entry.getKey() + " and value= "+ entry.getValue());
   		    	fls.add(new CountResult(entry.getKey(),entry.getValue()));
   		       }
   		List<CountResult>  sources=new ArrayList<CountResult>();
   	      for (Map.Entry<String, Integer> entry : zd.entrySet()) {
		    	LOG.info("key= " + entry.getKey() + " and value= "+ entry.getValue());
		    	sources.add(new CountResult(entry.getKey(),entry.getValue()));
		       }

		Map<String ,List<CountResult> > counts=new HashMap<String ,List<CountResult> >();
		counts.put("fls", fls);
	    counts.put("sources", sources);
		
		return counts;
	}



	@Override
	public Map<String ,List<CountResult> > sourcecountResultsLC(String keyword, String fl) {
		Map<String, Integer>  sjfl=new HashMap<String, Integer>();
		Map<String, Integer>  zd=new HashMap<String, Integer>();
     	String[] queryStr={"\""+keyword+"\"","\""+fl+"\""};
   		String [] searchField=new String[queryStr.length];
   		Occur[] occurs = new Occur[queryStr.length]; 
   		searchField[0]="KEY_WORD";
   		occurs[0]=Occur.MUST;
   		searchField[1]="SJFL";
   		occurs[1]=Occur.MUST;
   		//排序参数
   		String[] sortField= {Article.getMapedFieldName("date")};
   		SortField.Type[] sortFieldType={SortField.Type.LONG};
   		boolean[] reverse={true};
   		boolean isRelevancy = true;
   		int i=0;
   		while (true) {
   			List<Document> list=dao.search(queryStr, occurs, searchField, sortField, sortFieldType, reverse, isRelevancy, i*1000,1000);
   	   		if(list==null||list.size()==0)
   	   			break;
   			for(Document a:list){
   				String flStr = a.get("SJFL");
   				String sourceStr =a.get("ZD_NAME");
   				if (sjfl.containsKey(flStr)) {
   					sjfl.put(flStr, sjfl.get(flStr)+1);
				}else{
					sjfl.put(flStr, 1);
				}
   				if (zd.containsKey(sourceStr)) {
   					zd.put(sourceStr, zd.get(sourceStr)+1);
				}else{
					zd.put(sourceStr, 1);
				}
   	   		}
   	   		i++;
		}
   		List<CountResult>  fls=new ArrayList<CountResult>();
   		       for (Map.Entry<String, Integer> entry : sjfl.entrySet()) {
   		    	LOG.info("key= " + entry.getKey() + " and value= "+ entry.getValue());
   		    	fls.add(new CountResult(entry.getKey(),entry.getValue()));
   		       }
   		List<CountResult>  sources=new ArrayList<CountResult>();
   	      for (Map.Entry<String, Integer> entry : zd.entrySet()) {
		    	LOG.info("key= " + entry.getKey() + " and value= "+ entry.getValue());
		    	sources.add(new CountResult(entry.getKey(),entry.getValue()));
		       }
		Map<String ,List<CountResult> > counts=new HashMap<String ,List<CountResult> >();
		counts.put("fls", fls);
	    counts.put("sources", sources);
		
		return counts;	
		}

	@Override
	public Map<String ,List<CountResult> > countResultsLC(String keyword, String fl,
			String source) {
		Map<String, Integer>  sjfl=new HashMap<String, Integer>();
		Map<String, Integer>  zd=new HashMap<String, Integer>();
     	String[] queryStr={"\""+keyword+"\"","\""+source+"\"","\""+fl+"\""};
   		String [] searchField=new String[queryStr.length];
   		Occur[] occurs = new Occur[queryStr.length]; 
   		searchField[0]="KEY_WORD";
   		occurs[0]=Occur.MUST;
   		searchField[1]="ZD_NAME";
   		occurs[1]=Occur.MUST;
   		searchField[2]="SJFL";
   		occurs[2]=Occur.MUST;
   		//排序参数
   		String[] sortField= {Article.getMapedFieldName("date")};
   		SortField.Type[] sortFieldType={SortField.Type.LONG};
   		boolean[] reverse={true};
   		boolean isRelevancy = true;
   		int i=0;
   		while (true) {
   			List<Document> list=dao.search(queryStr, occurs, searchField, sortField, sortFieldType, reverse, isRelevancy, i*1000,1000);
   	   		if(list==null||list.size()==0)
   	   			break;
   			for(Document a:list){
   				String flStr = a.get("SJFL");
   				String sourceStr =a.get("ZD_NAME");
   				if (sjfl.containsKey(flStr)) {
   					sjfl.put(flStr, sjfl.get(flStr)+1);
				}else{
					sjfl.put(flStr, 1);
				}
   				if (zd.containsKey(sourceStr)) {
   					zd.put(sourceStr, zd.get(sourceStr)+1);
				}else{
					zd.put(sourceStr, 1);
				}
   	   		}
   	   		i++;
		}
   		List<CountResult>  fls=new ArrayList<CountResult>();
   		       for (Map.Entry<String, Integer> entry : sjfl.entrySet()) {
   		    	LOG.info("key= " + entry.getKey() + " and value= "+ entry.getValue());
   		    	fls.add(new CountResult(entry.getKey(),entry.getValue()));
   		       }
   		List<CountResult>  sources=new ArrayList<CountResult>();
   	      for (Map.Entry<String, Integer> entry : zd.entrySet()) {
		    	LOG.info("key= " + entry.getKey() + " and value= "+ entry.getValue());
		    	sources.add(new CountResult(entry.getKey(),entry.getValue()));
		       }

		Map<String ,List<CountResult> > counts=new HashMap<String ,List<CountResult> >();
		counts.put("fls", fls);
	    counts.put("sources", sources);
		
		return counts;
	}
	@Override
	public   QueryResutList  queryByKeyWord(String keyword,int pageNum,int pageSize){
		QueryResutList queryResutList=new QueryResutList();
		queryResutList.setPage(0);
		queryResutList.setPageNum(pageNum);
		queryResutList.setPageSize(pageSize);

     	String[] queryStr={"\""+keyword+"\""};
   		String [] searchField=new String[queryStr.length];
   		Occur[] occurs = new Occur[queryStr.length]; 
   		searchField[0]="KEY_WORD";
   		occurs[0]=Occur.MUST;
   	//排序参数
   		String[] sortField= {Article.getMapedFieldName("date")};
   		SortField.Type[] sortFieldType={SortField.Type.LONG};
   		boolean[] reverse={true};
   		boolean isRelevancy = true;
   		Map<String, Object> result = dao.searchRs(queryStr, occurs, searchField, sortField, sortFieldType, reverse, isRelevancy, (pageNum-1)*pageSize, pageSize);
		Map<String, List<CountResult>> map = countResultsLC(keyword);

   		if(result==null){
			queryResutList.setTotalNUm(0);
			return queryResutList;
		}
		int size=Integer.valueOf(result.get("size").toString());
		if(size==0){
			queryResutList.setTotalNUm(0);
			return queryResutList;
		}
		if(result.get("documents")==null){
			queryResutList.setTotalNUm(0);
			return queryResutList;
		}
		queryResutList.setTotalNUm(size);
		queryResutList.setPage((size+pageSize-1)/pageSize);
		List<Document> documents=(List<Document>) result.get("documents");
		List<XqResult> xqResults=parseDocs(documents);
		queryResutList.setXqResults(xqResults);
		List<CountResult> fls = map.get("fls");
		List<CountResult> sources = map.get("sources");
		queryResutList.setFlcountResults(fls);
		queryResutList.setSourcecountResults(sources);
		
		return queryResutList;
	}
	public  List<XqResult> parseDocs(List<Document> documents){
		List<XqResult>  xqResults=new ArrayList<XqResult>();
		if(documents==null||documents.size()==0){
			return  xqResults;
		}
		for (int i = 0; i < documents.size(); i++) {
			XqResult  xqResult=new XqResult();
			Document document = documents.get(i);
			xqResult.id=Long.valueOf(document.get("XQ_ID"));
			xqResult.title=document.get("XQ_TITLE");
			xqResult.source=document.get("ZD_NAME");
			xqResult.fl=document.get("SJFL");
			xqResult.keywords=parseKeyWords(document.get("KEY_WORD"));
			xqResult.url=document.get("XQ_URL");
			xqResult.crdate=parseDate(document.get("XQ_PUDATE"));
			xqResults.add(xqResult);
		}
		
		return xqResults;
	}
	public String  parseDate(String dateStr){
		if(dateStr==null)
			return "2017-01-01";
		long  dataLong=Long.valueOf(dateStr.trim());
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date  date=new Date(dataLong);

		return  df.format(date);
	}
	public List<String>  parseKeyWords(String keywords){
		if(keywords==null){
			return new ArrayList<String>();
		}
		List<String> keyword= new ArrayList<String>();
		String[] arr = keywords.split(";");
		for (int i = 0; i < arr.length; i++) {
			keyword.add(arr[i].trim());
		}
		return keyword;
		
	}
	@Override
	public   QueryResutList  queryByKS(String keyword,String source,int pageNum,int pageSize){
		QueryResutList queryResutList=new QueryResutList();
		queryResutList.setPage(0);
		queryResutList.setPageNum(pageNum);
		queryResutList.setPageSize(pageSize);

     	String[] queryStr={"\""+keyword+"\"","\""+source+"\""};
   		String [] searchField=new String[queryStr.length];
   		Occur[] occurs = new Occur[queryStr.length]; 
   		searchField[0]="KEY_WORD";
   		occurs[0]=Occur.MUST;
   		searchField[1]="ZD_NAME";
   		occurs[1]=Occur.MUST;
   	//排序参数
   		String[] sortField= {Article.getMapedFieldName("date")};
   		SortField.Type[] sortFieldType={SortField.Type.LONG};
   		boolean[] reverse={true};
   		boolean isRelevancy = true;
   		Map<String, Object> result = dao.searchRs(queryStr, occurs, searchField, sortField, sortFieldType, reverse, isRelevancy, (pageNum-1)*pageSize, pageSize);
		Map<String, List<CountResult>> map = flcountResultsLC(keyword,source);

   		if(result==null){
			queryResutList.setTotalNUm(0);
			return queryResutList;
		}
		int size=Integer.valueOf(result.get("size").toString());
		if(size==0){
			queryResutList.setTotalNUm(0);
			return queryResutList;
		}
		if(result.get("documents")==null){
			queryResutList.setTotalNUm(0);
			return queryResutList;
		}
		queryResutList.setTotalNUm(size);
		queryResutList.setPage((size+pageSize-1)/pageSize);
		List<Document> documents=(List<Document>) result.get("documents");
		List<XqResult> xqResults=parseDocs(documents);
		queryResutList.setXqResults(xqResults);
		List<CountResult> fls = map.get("fls");
		List<CountResult> sources = map.get("sources");
		queryResutList.setFlcountResults(fls);
		queryResutList.setSourcecountResults(sources);
		
		return queryResutList;
		
		
		
		
	}
	@Override
	public   QueryResutList  queryByKF(String keyword,String fl,int pageNum,int pageSize){
		QueryResutList queryResutList=new QueryResutList();
		queryResutList.setPage(0);
		queryResutList.setPageNum(pageNum);
		queryResutList.setPageSize(pageSize);

     	String[] queryStr={"\""+keyword+"\"","\""+fl+"\""};
   		String [] searchField=new String[queryStr.length];
   		Occur[] occurs = new Occur[queryStr.length]; 
   		searchField[0]="KEY_WORD";
   		occurs[0]=Occur.MUST;
   		searchField[1]="SJFL";
   		occurs[1]=Occur.MUST;
   	//排序参数
   		String[] sortField= {Article.getMapedFieldName("date")};
   		SortField.Type[] sortFieldType={SortField.Type.LONG};
   		boolean[] reverse={true};
   		boolean isRelevancy = true;
   		Map<String, Object> result = dao.searchRs(queryStr, occurs, searchField, sortField, sortFieldType, reverse, isRelevancy, (pageNum-1)*pageSize, pageSize);
		Map<String, List<CountResult>> map = sourcecountResultsLC(keyword,fl);

   		if(result==null){
			queryResutList.setTotalNUm(0);
			return queryResutList;
		}
		int size=Integer.valueOf(result.get("size").toString());
		if(size==0){
			queryResutList.setTotalNUm(0);
			return queryResutList;
		}
		if(result.get("documents")==null){
			queryResutList.setTotalNUm(0);
			return queryResutList;
		}
		queryResutList.setTotalNUm(size);
		queryResutList.setPage((size+pageSize-1)/pageSize);
		List<Document> documents=(List<Document>) result.get("documents");
		List<XqResult> xqResults=parseDocs(documents);
		queryResutList.setXqResults(xqResults);
		List<CountResult> fls = map.get("fls");
		List<CountResult> sources = map.get("sources");
		queryResutList.setFlcountResults(fls);
		queryResutList.setSourcecountResults(sources);
		
		return queryResutList;	}
	
	@Override
	public   QueryResutList  queryByKSF(String keyword,String source,String fl,int pageNum,int pageSize){
		QueryResutList queryResutList=new QueryResutList();
		queryResutList.setPage(0);
		queryResutList.setPageNum(pageNum);
		queryResutList.setPageSize(pageSize);

     	String[] queryStr={"\""+keyword+"\"","\""+source+"\"","\""+fl+"\""};
   		String [] searchField=new String[queryStr.length];
   		Occur[] occurs = new Occur[queryStr.length]; 
   		searchField[0]="KEY_WORD";
   		occurs[0]=Occur.MUST;
   		searchField[1]="ZD_NAME";
   		occurs[1]=Occur.MUST;
   		searchField[2]="SJFL";
   		occurs[2]=Occur.MUST;
   	//排序参数
   		String[] sortField= {Article.getMapedFieldName("date")};
   		SortField.Type[] sortFieldType={SortField.Type.LONG};
   		boolean[] reverse={true};
   		boolean isRelevancy = true;
   		Map<String, Object> result = dao.searchRs(queryStr, occurs, searchField, sortField, sortFieldType, reverse, isRelevancy, (pageNum-1)*pageSize, pageSize);
		Map<String, List<CountResult>> map = countResultsLC(keyword, fl, source);

   		if(result==null){
			queryResutList.setTotalNUm(0);
			return queryResutList;
		}
		int size=Integer.valueOf(result.get("size").toString());
		if(size==0){
			queryResutList.setTotalNUm(0);
			return queryResutList;
		}
		if(result.get("documents")==null){
			queryResutList.setTotalNUm(0);
			return queryResutList;
		}
		queryResutList.setTotalNUm(size);
		queryResutList.setPage((size+pageSize-1)/pageSize);
		List<Document> documents=(List<Document>) result.get("documents");
		List<XqResult> xqResults=parseDocs(documents);
		queryResutList.setXqResults(xqResults);
		List<CountResult> fls = map.get("fls");
		List<CountResult> sources = map.get("sources");
		queryResutList.setFlcountResults(fls);
		queryResutList.setSourcecountResults(sources);
		
		return queryResutList;		
	}
	
	
	
	
	
	
	public static void main(String[] args) throws IOException {
		System.out.println(System.currentTimeMillis());

		String indexPath = "D:\\indexpath"; 
   		IndexDao dao=new IndexDao(indexPath);
   ReKeyWordListServiceImpl  impl=new ReKeyWordListServiceImpl();
   impl.setDao(dao);
   
   Gson  gs=new Gson();
   QueryResutList ssds = impl.queryByKF("工作", "军事", 1, 10);
System.out.println(gs.toJson(ssds));
System.out.println(System.currentTimeMillis());

	}
	
	
	
	

}
