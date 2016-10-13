package com.jt.test.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

import com.jt.lucene.IndexDao;
import com.jt.lucene.LuceneUtilsGw;

public class LunceneTest {
    private static String indexPath = "/indexpath";    // 索引保存目录
    private static LuceneUtilsGw util=null;
    public static void createIndex(){    // 建立索引
       IndexWriter writer;
       try {
    	   util=new LuceneUtilsGw(indexPath);
    	   IndexWriterConfig config = new IndexWriterConfig(util.getAnalyzer());
            writer = new IndexWriter(util.getDirectory(),config);
            List<Field> fieldList=new ArrayList<Field>();
            fieldList.add(new Field("xq_title","我们为电影《晚上》是一部不错的影片。",TextField.TYPE_STORED));
            fieldList.add(new Field("xq_title","我们今天晚上没有事。",TextField.TYPE_STORED));
            fieldList.add(new Field("xq_title","今天晚上看电影。",TextField.TYPE_STORED));
            fieldList.add(new Field("xq_title","我们认为电影不错电视也不错",TextField.TYPE_STORED));
            for(Field f:fieldList){
                Document doc1  = new Document();
                doc1.add(f);
            	writer.addDocument(doc1);
            }
            writer.close();
       } 
       catch (Exception e) {
           e.printStackTrace();
       } 
    }
    
    public static void main(String[] args) {    //contests字段上查找含有"我们","今晚"这两个字段的Doument
       Query query;
       IndexSearcher searcher;
       try {
    	   createIndex();
		IndexDao dao=new IndexDao(indexPath);
		String[] queryStr={"我们","电影","电视"};
		List<Document> list=dao.search(queryStr, Occur.SHOULD, "xq_title",null,null, false, 0, -1);
		for(Document doc:list){
			System.out.println(doc.get("xq_title"));
		}
       } catch (IOException e1) {
		e1.printStackTrace();
	}
       
//        try {
//            //生成索引
//            createIndex();
////        	util=new LuceneUtilsGw(indexPath);
////            DirectoryReader ireader = DirectoryReader.open(util.getDirectory());
////            searcher = new IndexSearcher(ireader);
////            //要查找的字符串数组
////            String [] stringQuery={"我们","今晚"};
////            //待查找字符串对应的字段
////            String[] fields={"contents","contents"};
////            //Occur.MUST表示对应字段必须有查询值， Occur.MUST_NOT 表示对应字段必须没有查询值
////            Occur[] occ={Occur.SHOULD,Occur.SHOULD};
////            
////            query=MultiFieldQueryParser.parse(stringQuery,fields,occ,new StandardAnalyzer());
////            TopDocs topDocs = searcher.search(query,1);
////            System.out.println(topDocs.totalHits);
////            ScoreDoc[]score=topDocs.scoreDocs;
////            for(int i=0;i<score.length;i++){
////            	System.out.println("score["+i+"].doc=["+score[i].doc+"]");
////            	Document hitDoc=searcher.doc(score[i].doc);
////            	System.out.println(hitDoc.get("contents"));
////            }
//       } 
//       catch (Exception e) {
//    	   e.printStackTrace();
//    	   
//       } 
    }
}