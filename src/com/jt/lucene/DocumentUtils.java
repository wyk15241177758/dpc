package com.jt.lucene;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

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
			article.setId(Long.parseLong(getColumnIgnoreCase(doc,article.getMapedFieldName("id"))));
		} catch (NumberFormatException e) {
			article.setId(0l);
		}
		article.setTitle(getColumnIgnoreCase(doc,article.getMapedFieldName("title")));
		article.setUrl(getColumnIgnoreCase(doc,article.getMapedFieldName("url")));
		String time=getColumnIgnoreCase(doc,article.getMapedFieldName("date"));
		if(time==null){
			article.setDate(null);
		}else{
			try {
				article.setDate(new Date(Long.parseLong(time)));
			} catch (NumberFormatException e) {
				System.out.println("转换为long型失败，time=["+time+"]尝试转为Date类型");
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				try {
					article.setDate(sdf.parse(time));
				} catch (ParseException e1) {
					System.out.println("转换Data类型失败，Data赋值为空");
					article.setDate(null);
					e1.printStackTrace();
					
				}
			}
		}
		article.setCategory(getColumnIgnoreCase(doc, article.getMapedFieldName("category")));
		article.setChannel(getColumnIgnoreCase(doc, article.getMapedFieldName("channel")));
		article.setChannelUrl(getColumnIgnoreCase(doc, article.getMapedFieldName("channelUrl")));
		article.setSite(getColumnIgnoreCase(doc, article.getMapedFieldName("site")));
		article.setKeyWord(getColumnIgnoreCase(doc,article.getMapedFieldName("keyWord")));
		return article;
	}
	public static Document article2Document(Article article){
		Document doc  = new Document();
		doc.add(new Field(article.getMapedFieldName("id"),article.getId()+"",TextField.TYPE_STORED));
		doc.add(new Field(article.getMapedFieldName("title"),article.getTitle(),TextField.TYPE_STORED));
		doc.add(new Field(article.getMapedFieldName("url"),article.getUrl(),TextField.TYPE_STORED));
		doc.add(new Field(article.getMapedFieldName("channel"),article.getChannel(),TextField.TYPE_STORED));
		doc.add(new Field(article.getMapedFieldName("channelUrl"),article.getChannelUrl(),TextField.TYPE_STORED));
		doc.add(new Field(article.getMapedFieldName("site"),article.getSite(),TextField.TYPE_STORED));
		doc.add(new Field(article.getMapedFieldName("category"),article.getCategory(),TextField.TYPE_STORED));
		SimpleDateFormat sdf=new SimpleDateFormat();
		doc.add(new Field(article.getMapedFieldName("date"),sdf.format(article.getDate()),TextField.TYPE_STORED));
		doc.add(new Field(article.getMapedFieldName("keyWord"),article.getKeyWord(),TextField.TYPE_STORED));
		return doc;
	}

	/**
	 * 根据集合是否siz为空相互转化
	 * @param docCol
	 * @param artCol
	 */
	public static void transCollection(Collection<Document> docCol,Collection<Article> artCol){
		if(docCol.size()==0){
			for(Article art:artCol){
				docCol.add(article2Document(art));
			}
		}else{
			for(Document doc:docCol){
				artCol.add(document2Ariticle(doc));
			}
		}
	}
	
	//字段映射，方便后续统一修改
		public static String getMapedFieldName(String type,String fieldName) {
			if("searchHis".equalsIgnoreCase(type)){
				if(fieldName.equalsIgnoreCase("id")){
					return "ID";
				}else if(fieldName.equalsIgnoreCase("content")){
					return "SEARCHCONTENT";
				}else if(fieldName.equalsIgnoreCase("times")){
					return "SEARCHTIMES";
				}else if(fieldName.equalsIgnoreCase("date")){
					return "UPDATETIME";
				}
			}else if("article".equalsIgnoreCase(type)){
				return Article.getMapedFieldName(fieldName);
			}
			return null;
		}
}