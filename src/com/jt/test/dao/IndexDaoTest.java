package com.jt.test.dao;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;

import com.jt.lucene.Article;
import com.jt.lucene.IndexDao;

public class IndexDaoTest {
  public  void  test() throws IOException{
	  IndexDao indexDao=new IndexDao("D:\\indexpath_test");
	  
	  Document doc=new  Document();
	  doc.add(new Field("xq_id", "1", TextField.TYPE_STORED));
	  doc.add(new Field("xq_title", "李挚到临湘市征求岳阳市第七次党代会主题报告和纪委工作报告修改意见", TextField.TYPE_STORED));
	  doc.add(new Field("xq_url", "www.yueyang.gov.cn/xqdt/lxs/content_591280.html", TextField.TYPE_STORED));
	  
	  indexDao.save(doc);
	  
	  String[] queryStr={"临湘市"};
//		List<Document> list=dao.search(queryStr, Occur.SHOULD, "xq_title",null,null, false, 0, -1);
//		for(Document doc:list){
//			System.out.println(doc.getValues("xq_title")[0]);
//			
//		}
		List<Article> list=indexDao.searchArticle(queryStr, "xq_title", 0, 10);
		for(Article a:list){
			System.out.println(a.getTitle());
		}
  }
  public static void main(String[] args) {
	IndexDaoTest test=new IndexDaoTest();
	try {
		test.test();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}
