package com.jt.test;


import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.search.SortField;

import com.jt.lucene.IndexDao;

public class LuceneTest {

	public void addLongPoint(Document document, String name, long value) {
	    Field field = new LongPoint(name, value);
	    document.add(field);
	    //Ҫ���򣬱������һ��ͬ����NumericDocValuesField
	    field = new NumericDocValuesField(name, value);
	    document.add(field);
	    //Ҫ�洢ֵ���������һ��ͬ����StoredField
	    field = new StoredField(name, value);
	    document.add(field);
	}
	
	public void saveToIndex(IndexDao dao){
		Document doc=new Document();
		addLongPoint(doc, "id", 1l);
		doc.add(new Field("title", "�й�����Ӵ�վ������", TextField.TYPE_STORED));
		dao.save(doc);
		doc=new Document();
		addLongPoint(doc, "id", 2l);
		doc.add(new Field("title", "�й����������", TextField.TYPE_STORED));
		dao.save(doc);
	}
	 
public static void main(String[] args) {
	LuceneTest test=new LuceneTest();
	IndexDao dao=null;
	try { 
		dao=new IndexDao("D:\\luceneindex");
		//test.saveToIndex(dao);
		List<Document> list=dao.search("�й�����", "title", "id",SortField.Type.LONG, false, 0, 10);
		for(Document doc:list){
			System.out.println(doc.get("title"));
		}
	} catch (IOException e) {
		e.printStackTrace();
	}
	
	
	
}
}
