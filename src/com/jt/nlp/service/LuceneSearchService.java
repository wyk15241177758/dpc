/**
 * 调用全文检索服务
 */
package com.jt.nlp.service;

import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.BooleanClause.Occur;
import org.springframework.stereotype.Service;

import com.jt.lucene.Article;
import com.jt.lucene.IndexDao;
@Service
public class LuceneSearchService {
	private IndexDao dao;

	/**
	 * 检索词的与、或关系统一定义
	 * 
	 * @param queryString
	 *            支持传入多个检索词
	 * @param occur
	 *            多个检索词之间的关系，Occur.MUST(结果“与”)，
	 *            Occur.MUST_NOT(结果“不包含”)，Occur.SHOULD(结果“或”)
	 * @param field
	 *            当前应用使用title
	 * @param sortField
	 *            排序字段，传入null则按照相关度排序
	 * @param sortFieldType
	 *            排序字段的类型，如long、int等
	 * @param reverse
	 *            排序方式，顺序(false)还是倒序(true)
	 * @param firstResult
	 *            结果从第几个开始
	 * @param maxResult
	 *            结果到第几个结束，可以传入-1，则限制为1000条
	 * @return
	 */
	public List<Document> search(String[] queryString, Occur occur, String field, String sortField,
			SortField.Type sortFieldType, boolean reverse, int firstResult, int maxResult) {
		return dao.search(queryString, occur, field, sortField, sortFieldType, reverse, firstResult, maxResult);
	}

	/**
	 * 支持定义每个检索词的与、或关系
	 * 
	 * @param queryString
	 *            支持传入多个检索词
	 * @param occurs
	 *            多个检索词之间的关系，Occur.MUST(结果“与”)，
	 *            Occur.MUST_NOT(结果“不包含”)，Occur.SHOULD(结果“或”)
	 * @param field
	 *            当前应用使用title
	 * @param sortField
	 *            排序字段，传入null则按照相关度排序
	 * @param sortFieldType
	 *            排序字段的类型，如long、int等
	 * @param reverse
	 *            排序方式，顺序(false)还是倒序(true)
	 * @param firstResult
	 *            结果从第几个开始
	 * @param maxResult
	 *            结果到第几个结束，可以传入-1，则限制为1000条
	 * @return
	 */
	public List<Document> search(String[] queryString, Occur[] occurs, String field, String sortField,
			SortField.Type sortFieldType, boolean reverse, int firstResult, int maxResult) {
		return dao.search(queryString, occurs, field, sortField, sortFieldType, reverse, firstResult, maxResult);
	}

	/**
	 * 检索词的与、或关系统一定义
	 * 
	 * @param queryString
	 *            支持传入多个检索词
	 * @param occur
	 *            多个检索词之间的关系，Occur.MUST(结果“与”)，
	 *            Occur.MUST_NOT(结果“不包含”)，Occur.SHOULD(结果“或”)
	 * @param field
	 *            当前应用使用title
	 * @param sortField
	 *            排序字段，传入null则按照相关度排序
	 * @param sortFieldType
	 *            排序字段的类型，如long、int等
	 * @param reverse
	 *            排序方式，顺序(false)还是倒序(true)
	 * @param firstResult
	 *            结果从第几个开始
	 * @param maxResult
	 *            结果到第几个结束，可以传入-1，则限制为1000条
	 * @return
	 */
	public List<Article> searchArticle(String[] queryString, Occur occur, String field, String sortField,
			SortField.Type sortFieldType, boolean reverse, int firstResult, int maxResult) {
		return dao.searchArticle(queryString, occur, field, sortField, sortFieldType, reverse, firstResult, maxResult);
	}

	/**
	 * 支持定义每个检索词的与、或关系
	 * 
	 * @param queryString
	 *            支持传入多个检索词
	 * @param occurs
	 *            多个检索词之间的关系，Occur.MUST(结果“与”)，
	 *            Occur.MUST_NOT(结果“不包含”)，Occur.SHOULD(结果“或”)
	 * @param field
	 *            当前应用使用title
	 * @param sortField
	 *            排序字段，传入null则按照相关度排序
	 * @param sortFieldType
	 *            排序字段的类型，如long、int等
	 * @param reverse
	 *            排序方式，顺序(false)还是倒序(true)
	 * @param firstResult
	 *            结果从第几个开始
	 * @param maxResult
	 *            结果到第几个结束，可以传入-1，则限制为1000条
	 * @return
	 */
	public List<Article> searchArticle(String[] queryString, Occur[] occurs, String field, String sortField,
			SortField.Type sortFieldType, boolean reverse, int firstResult, int maxResult) {
		return dao.searchArticle(queryString, occurs, field, sortField, sortFieldType, reverse, firstResult, maxResult);
	}

	/**
	 * 简易检索，各个字段之间是or关系，默认按照相关性排序
	 * @param queryString
	 * @param field
	 * @param firstResult
	 * @param maxResult
	 * @return
	 */
	public List<Article> searchArticle(String[] queryString,String field,int firstResult, int maxResult){ 
		System.out.println("检索词为");
		for(String str:queryString){
			System.out.println(str);
		}
			
		return dao.searchArticle(queryString, field, firstResult, maxResult);
	}
	
	public IndexDao getDao() {
		return dao;
	}

	public void setDao(IndexDao dao) {
		this.dao = dao;
	}

}
