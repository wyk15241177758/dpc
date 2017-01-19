package com.jt.test.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apdplat.word.analysis.Hits;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jt.bean.gateway.PageMsg;
import com.jt.lucene.Article;
import com.jt.lucene.DocumentUtils;
import com.jt.lucene.IndexDao;
import com.jt.lucene.LuceneUtilsGw;

public class LuceneTest {
    private static String indexPath = "D:\\indexpath";    // 索引保存目录
    private static LuceneUtilsGw util=null;
    public static void createIndex(){    // 建立索引
       IndexWriter writer;
       try {
    	   util=new LuceneUtilsGw(indexPath);
    	   IndexWriterConfig config = new IndexWriterConfig(util.getAnalyzer());
            writer = new IndexWriter(util.getDirectory(),config);
            Article article=new Article();
            article.setId(1l);
            article.setTitle("我们为电影《晚上》是一部不错的影片");
            article.setCategory("政务知识");
            article.setChannel("新闻");
            article.setDate(new Date());
            article.setSite("岳阳市政府");
            article.setUrl("http://yueyang.gov.cn/gggs/shts/9546/content_601504.html");
            writer.addDocument(DocumentUtils.article2Document(article));
            article=new Article();
            article.setId(2l);
            article.setTitle("我们今天晚上没有事");
            article.setCategory("政务消息");
            article.setChannel("互动交流");
            article.setDate(new Date());
            article.setSite("岳阳市检察院");
            article.setUrl("http://www.yueyang.gov.cn/webapp/yueyang/email/viewPublic.jsp?id=76276");
            writer.addDocument(DocumentUtils.article2Document(article));
            article=new Article();
            article.setId(3l);
            article.setTitle("今天晚上看电影");
            article.setCategory("政务知识");
            article.setChannel("新闻");
            article.setDate(new Date());
            article.setSite("岳阳市政府");
            article.setUrl("http://yueyang.gov.cn/gggs/shts/9546/content_601504.html");
            writer.addDocument(DocumentUtils.article2Document(article));
            article=new Article();
            article.setId(4l);
            article.setTitle("我们认为电影不错电视也不错");
            article.setCategory("社会常识");
            article.setChannel("新闻");
            article.setDate(new Date());
            article.setSite("岳阳市政府");
            article.setUrl("http://yueyang.gov.cn/gggs/shts/9546/content_601504.html");
            writer.addDocument(DocumentUtils.article2Document(article));
            article=new Article();
            article.setId(5l);
            article.setTitle("政府政策亚克西");
            article.setCategory("市场要闻");
            article.setChannel("新闻");
            article.setDate(new Date());
            article.setSite("岳阳市政府");
            article.setUrl("http://yueyang.gov.cn/gggs/shts/9546/content_601504.html");
            writer.addDocument(DocumentUtils.article2Document(article));
            
//            
//            List<Field> fieldList=new ArrayList<Field>();
//            fieldList.add(new Field("xq_title","我们为电影《晚上》是一部不错的影片。",TextField.TYPE_STORED));
//            fieldList.add(new Field("xq_title","我们今天晚上没有事。",TextField.TYPE_STORED));
//            fieldList.add(new Field("xq_title","今天晚上看电影。",TextField.TYPE_STORED));
//            fieldList.add(new Field("xq_title","我们认为电影不错电视也不错",TextField.TYPE_STORED));
//            fieldList.add(new Field("xq_title","政府政策亚克西",TextField.TYPE_STORED));
//            for(int i=0;i<fieldList.size();i++){
//            	Field f=fieldList.get(i);
//                Document doc1  = new Document();
//                doc1.add(f);
//                doc1.add(new Field("xq_id",i+"",TextField.TYPE_STORED));
//            	writer.addDocument(doc1);
//            }
            writer.close();
       } 
       catch (Exception e) {
           e.printStackTrace();
       } 
    }
    
    public static void TermQueryTest(String searchWord) throws IOException{
    	// 生成hits结果对象，保存返回的检索结果
		Hits hits = null;
		util=new LuceneUtilsGw(indexPath);
		DirectoryReader ireader=null;
		IndexSearcher searcher=null;
		Query query = null;
		ireader = DirectoryReader.open(util.getDirectory());
		// 2、第二步，创建搜索器
		searcher = new IndexSearcher(ireader);
		// 生成检索器

		// 构造一个TermQuery对象
		query = new TermQuery(new Term("xq_title", searchWord));
		// 开始检索，并返回检索结果到hits中
		
		 ScoreDoc[] docs=searcher.search(query,12).scoreDocs;
			
         for(int i=0;i<docs.length;i++){     
        	    
             String querycontent=searcher.doc(docs[i].doc).get("xq_title");
             System.out.println("查询内容: "+querycontent);
             System.out.println(docs[i].score);
              
       }
    }
    
  
    
    public static void SearchAllTest(){
    	  Query query;
          IndexSearcher searcher;
//          createIndex();
          
          try {
   		IndexDao dao=new IndexDao(indexPath);
   		String[] queryStr={"1"};
   		//检索参数
   		String [] searchField=new String[queryStr.length];
   		Occur[] occurs = new Occur[queryStr.length]; 
   		for(int i=0;i<searchField.length;i++){
   			searchField[i]="SEARCHALL";
   			occurs[i]=Occur.MUST;
   		}
   			
   		//排序参数
   		String[] sortField= {Article.getMapedFieldName("date")};
   		SortField.Type[] sortFieldType={SortField.Type.LONG};
   		boolean[] reverse={true};
   		boolean isRelevancy = true;
   		
   		List<Document> list=dao.search(queryStr, occurs, searchField, sortField, sortFieldType, reverse, isRelevancy, 0,100);
   		for(Document a:list){
   			System.out.println(a);
   		}

   		
          } catch (IOException e1) {
   		e1.printStackTrace();
   	}
    }
    
    
    public static void SearchTest(){
    	  Query query;
          IndexSearcher searcher;
//          createIndex();
          
          try {
   		IndexDao dao=new IndexDao(indexPath);
   		String[] queryStr={"拱墅"};
//   		List<Document> list=dao.search(queryStr, Occur.SHOULD, "xq_title",null,null, false, 0, -1);
//   		for(Document doc:list){
//   			System.out.println(doc.getValues("xq_title")[0]);
//   			
//   		}
   		//检索参数
   		String [] searchField=new String[queryStr.length];
   		Occur[] occurs = new Occur[queryStr.length]; 
   		for(int i=0;i<searchField.length;i++){
   			searchField[i]="XQ_TITLE";
   			occurs[i]=Occur.MUST;
   		}
   			
   		//排序参数
   		String[] sortField= {Article.getMapedFieldName("date")};
   		SortField.Type[] sortFieldType={SortField.Type.LONG};
   		boolean[] reverse={true};
   		boolean isRelevancy = true;
   		
   		List<Document> list=dao.search(queryStr, occurs, searchField, sortField, sortFieldType, reverse, isRelevancy, 0,100);
   		for(Document a:list){
   			System.out.println(a);
   		}

   		
          } catch (IOException e1) {
   		e1.printStackTrace();
   	}
    }
    
    public  static void searchHis() throws IOException{
    	IndexDao dao=new IndexDao(indexPath);
    	String question="区长信箱";
		int begin=0;
		int end=5;
		String[] arrQuestion={question};
		List<String> particleQuestion=new ArrayList<String>();
		particleQuestion.add("区长");
		particleQuestion.add("信箱");
		particleQuestion.add("郑晓彬");
		particleQuestion.add("岳阳市");
		arrQuestion=particleQuestion.toArray(arrQuestion);
		//检索参数
		String [] searchField=new String[arrQuestion.length];
		Occur[] occurs = new Occur[arrQuestion.length]; 
		for(int i=0;i<searchField.length;i++){
			searchField[i]="SEARCHCONTENT";
			occurs[i]=Occur.SHOULD;
		}
			
		//排序参数，按照相关度、检索次数、时间排序
		String[] sortField={"SEARCHTIMES","UPDATETIME"};
		SortField.Type[] sortFieldType={SortField.Type.LONG,SortField.Type.LONG};
		boolean[] reverse={true,true};
		boolean isRelevancy = true;
		
		List<Document> list=dao.search(arrQuestion, occurs, searchField, sortField, sortFieldType, reverse, isRelevancy, begin, end);
		
		
		
		//按照SearchHis的字段进行格式化
		List<Map<String,String>> formatList=new ArrayList<Map<String,String>>();
		Map<String,String> columnMap=null;
		for(Document doc:list){
			columnMap=new HashMap<String,String>();
			//ID，SEARCHCONTENT,SEARCHTIMES,CREATETIME,UPDATETIME
			columnMap.put("ID",doc.get("ID"));
			columnMap.put("SEARCHCONTENT",doc.get("SEARCHCONTENT"));
			columnMap.put("SEARCHTIMES",doc.get("SEARCHTIMES"));
			columnMap.put("CREATETIME",doc.get("CREATETIME"));
			columnMap.put("UPDATETIME",doc.get("UPDATETIME"));
			formatList.add(columnMap);
		}
		PageMsg msg=new PageMsg();
		msg.setMsg(formatList);
		msg.setSig(true);
		Gson gson= new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		;
		System.out.println(gson.toJson(msg));
		
    }
    
    public static void main(String[] args) {    //contests字段上查找含有"我们","今晚"这两个字段的Doument
//    	try {
//			searchHis();
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
    	SearchAllTest();
    }
}