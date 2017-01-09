package com.jt.test.lucene;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

import com.jt.lucene.Article;
import com.jt.lucene.IndexDao;

//分词测试
public class Particletest {
public static void main(String[] args) {
	String indexPath="D:\\indexPath";
    Query query;
    IndexSearcher searcher;
//    createIndex();
    
    try {
		IndexDao dao=new IndexDao(indexPath);
		String[] queryStr={"岳阳 工业","政务"};
		Occur[] occurs={Occur.MUST,Occur.MUST};
		String[] fieldName={"xq_title","sjfl"};
//		List<Document> list=dao.search(queryStr, Occur.SHOULD, "xq_title",null,null, false, 0, -1);
//		for(Document doc:list){
//			System.out.println(doc.getValues("xq_title")[0]);
//			
//		}
//		List<Article> list=dao.searchArticle(queryStr, occurs, fieldName,
//				null, null, false, 0, 5);
//				//.searchArticle(queryStr, "xq_title", 0, 5);
//		for(Article a:list){
//			System.out.println(a);
//		}
    } catch (IOException e1) {
		e1.printStackTrace();
	}
}
}
