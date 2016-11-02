package com.jt.lucene;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;


/**
 * @项目名称：lucene
 * @类名称：DocumentUtils
 * @类描述：文章实体类和Document的转换工具
 * @创建人：YangChao
 * @创建时间：2016年8月31日 上午10:15:22
 * @version 1.0.0
 */
public class DocumentUtils {
	public static String getColumnIgnoreCase(Document doc,String cloumn){
		if(doc.get(cloumn)!=null){
			return doc.get(cloumn);
		}else if(doc.get(StringUtils.lowerCase(cloumn))!=null){
			return doc.get(StringUtils.lowerCase(cloumn));
		}else{
			return doc.get(StringUtils.upperCase(cloumn));
		}
	}
	public static Article document2Ariticle(Document doc) {
		Article article = new Article();
		try {
			article.setId(Integer.parseInt(getColumnIgnoreCase(doc,article.getMapedFieldName("id"))));
		} catch (NumberFormatException e) {
			article.setId(0);
		}
		article.setTitle(getColumnIgnoreCase(doc,article.getMapedFieldName("title")));
		article.setUrl(getColumnIgnoreCase(doc,article.getMapedFieldName("url")));
		String time=getColumnIgnoreCase(doc,article.getMapedFieldName("date"));
		if(time==null){
			article.setDate(null);
		}else{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				time=time.substring(0, time.indexOf("."));
				article.setDate(sdf.parse(time));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		article.setCategory(getColumnIgnoreCase(doc, article.getMapedFieldName("category")));
		article.setChannel(getColumnIgnoreCase(doc, article.getMapedFieldName("channel")));
		article.setSite(getColumnIgnoreCase(doc, article.getMapedFieldName("site")));
		return article;
	}
	public static Document article2Document(Article article){
		Document doc  = new Document();
		doc.add(new Field(article.getMapedFieldName("id"),article.getId()+"",TextField.TYPE_STORED));
		doc.add(new Field(article.getMapedFieldName("title"),article.getTitle(),TextField.TYPE_STORED));
		doc.add(new Field(article.getMapedFieldName("url"),article.getUrl(),TextField.TYPE_STORED));
		doc.add(new Field(article.getMapedFieldName("channel"),article.getChannel(),TextField.TYPE_STORED));
		doc.add(new Field(article.getMapedFieldName("site"),article.getSite(),TextField.TYPE_STORED));
		doc.add(new Field(article.getMapedFieldName("category"),article.getCategory(),TextField.TYPE_STORED));
		SimpleDateFormat sdf=new SimpleDateFormat();
		doc.add(new Field(article.getMapedFieldName("date"),sdf.format(article.getDate()),TextField.TYPE_STORED));
		return doc;
	}
}