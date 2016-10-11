package com.jt.lucene;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;


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
		article.setId(Integer.parseInt(getColumnIgnoreCase(doc,"XQ_ID")));
		article.setTitle(getColumnIgnoreCase(doc,"XQ_TITLE"));
		article.setUrl(getColumnIgnoreCase(doc,"XQ_URL"));
		String time=getColumnIgnoreCase(doc,"LOAD_TIME");
		SimpleDateFormat sdf=new SimpleDateFormat();
		try {
			article.setDate(sdf.parse(time));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		article.setCategory(getColumnIgnoreCase(doc, "SJFL"));
		article.setChannel(getColumnIgnoreCase(doc, "LM_NAME"));
		article.setSite(getColumnIgnoreCase(doc, "ZD_NAME"));
		return article;
	}
}