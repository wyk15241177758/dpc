package com.jt.lucene;
import java.util.List;

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
	
	public static Document article2Document(Article article) {
		Document doc = new Document();
		doc.add(new Field("id", article.getId().toString(), TextField.TYPE_STORED));
		doc.add(new Field("title", article.getTitle(), TextField.TYPE_STORED));
		doc.add(new Field("content", article.getContent(), TextField.TYPE_STORED));
		return doc;
	}

	public static Article document2Ariticle(Document doc) {
		Article article = new Article();
		article.setId(Integer.parseInt(doc.get("id")));
		article.setTitle(doc.get("title"));
		article.setContent(doc.get("content"));
		return article;
	}
}