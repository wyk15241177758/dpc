package com.jt.test.lucene;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
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
import com.jt.nlp.service.LuceneSearchService;

public class LuceneTest {
    private static String indexPath = "D:\\indexpath";    // 索引保存目录
    private static LuceneUtilsGw util=null;
    
	public static void addLongPoint(Document document, String name, long value) {
	    Field field = new LongPoint(name, value);
	    document.add(field);
	    //要排序，必须添加一个同名的NumericDocValuesField
	    field = new NumericDocValuesField(name, value);
	    document.add(field);
	    //要存储值，必须添加一个同名的StoredField
	    field = new StoredField(name, value);
	    document.add(field);
	}
    
    
    public static void createIndex(){    // 建立索引
       IndexWriter writer;
       try {
    	   util=new LuceneUtilsGw(indexPath);
    	   IndexWriterConfig config = new IndexWriterConfig(util.getAnalyzer());
    	   Document doc = new Document();
            writer = new IndexWriter(util.getDirectory(),config);
            doc = new Document();
            doc.add(new Field("XQ_ID", "1",TextField.TYPE_STORED));
            doc.add(new Field("XQ_TITLE", "刘和生参与了新的大会",TextField.TYPE_STORED));
            doc.add(new Field("SEARCHALL", "1",TextField.TYPE_STORED));
			addLongPoint(doc, "LOAD_TIME",System.currentTimeMillis());
            writer.addDocument(doc);
            
            
            doc = new Document();
            doc.add(new Field("XQ_ID", "2",TextField.TYPE_STORED));
            doc.add(new Field("XQ_TITLE", "解放军和生产办公室开会",TextField.TYPE_STORED));
            doc.add(new Field("SEARCHALL", "1",TextField.TYPE_STORED));
			addLongPoint(doc,  "LOAD_TIME",System.currentTimeMillis());
            writer.addDocument(doc);
            
            
            doc = new Document();
            doc.add(new Field("XQ_ID", "3",TextField.TYPE_STORED));
            doc.add(new Field("XQ_TITLE", "今天晚上看电影",TextField.TYPE_STORED));
            doc.add(new Field("SEARCHALL", "1",TextField.TYPE_STORED));
			addLongPoint(doc,  "LOAD_TIME",System.currentTimeMillis());
            writer.addDocument(doc);
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
		query = new TermQuery(new Term("XQ_TITLE", searchWord));
		// 开始检索，并返回检索结果到hits中
		
		 ScoreDoc[] docs=searcher.search(query,12).scoreDocs;
			
         for(int i=0;i<docs.length;i++){     
        	    
             String querycontent=searcher.doc(docs[i].doc).get("XQ_TITLE");
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
   		
   		List<Document> list=dao.search(queryStr, occurs, searchField, null, null, reverse, isRelevancy, 0,100,0);
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
   		String[] queryStr={"\"刘和生\""};
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
   		
   		List<Document> list=dao.search(queryStr, occurs, searchField, sortField, sortFieldType, reverse, isRelevancy, 0,100,0);
   		for(Document a:list){
   			System.out.println(a);
   		}

   		
          } catch (IOException e1) {
   		e1.printStackTrace();
   	}
    }
    
    public static void SearchTest2(){
    	String questionStr="请问怎么办理社保";
    	
    	List<Document> daoSearchList=null;
    	
    	LuceneSearchService searchService= new LuceneSearchService();
		try {
			searchService.setDao(new IndexDao(indexPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
		// 检索词
		Set<String> questionSet = null;
		// 预设场景
		Set<String> sceneWordSet = null;
		
		List<String> sceneSjflList = new ArrayList<String>();
		Map<String,List<Article>> qaResultMap= new LinkedHashMap<String,List<Article>>();

		// 分类作为必须包含的字段进行检索，如下三个变量长度必须相同

		String[] searchWord = new String[2];
		Occur[] occurs = new Occur[searchWord.length];
		String[] fields = new String[searchWord.length];
		searchWord[0]=questionStr;
		occurs[0]=Occur.MUST;
		occurs[1]=Occur.MUST;
		fields[0]=Article.getMapedFieldName("title");
		fields[1]=Article.getMapedFieldName("category");
		
		
		//排序参数
		String[] sortField={Article.getMapedFieldName("date")};
		SortField.Type[] sortFieldType={SortField.Type.LONG};
		boolean[] reverse={true};
		boolean isRelevancy = true;
		
		
		
		String qaSjfl="政策文件";
		if(qaSjfl!=null&&qaSjfl.length()>0){
			String[] sjfl=qaSjfl.split(";");
			for(String str:sjfl){
				//赋值分类value
				searchWord[1]="\""+str+"\"";
				List<Article> rsList=qaResultMap.get(str);
				if(rsList==null){
					rsList=new ArrayList<Article>();
				}
				daoSearchList=searchService.getDao().searchTest(searchWord, occurs, fields, 
						sortField, sortFieldType, reverse, isRelevancy, 0, 5);
			}
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		for (Document doc: daoSearchList) {
			Article article=DocumentUtils.document2Ariticle(doc);
			System.out.println("date=["+sdf.format(article.getDate())+"] article:"+article);
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
		
		List<Document> list=dao.searchTest(arrQuestion, occurs, searchField, sortField, sortFieldType, reverse, isRelevancy, begin, end);
		
		
		
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
//    	createIndex();
//    	SearchAllTest();
    	SearchTest2();
    }
}