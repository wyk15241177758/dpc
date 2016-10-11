/**
 * 对外提供QA功能的主服务，由此服务调用nlp和Lucene功能
 */
package com.jt.nlp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.lucene.search.BooleanClause.Occur;

import com.jt.lucene.Article;

public class QAService {
	private NlpService nlpService;
	private LuceneSearchService searchService;
	 /**
	  * 预设场景，留空
	  * @param word
	  * @return
	  * 查询问题是否包含预设场景的词汇，此方法应该在lucene检索和NLP分析之前
	  */
	 private List<Article> presentScene(){
		 boolean presented=false;
		 return null;
	 }
	 
	 public List<Article> QASearch(String question,int size){
		 List<Article> list=new ArrayList<Article>();
		 list=presentScene();
		 //未进入预设场景
		 if(list==null){
			 Set<String> questionSet=nlpService.getSearchWords(question);
			 return searchService.searchArticle(questionSet.toArray(), Occur.SHOULD, 
					 "xq_title", null, null, false, 0, size);
		 }
		 return list;
	 }

	public NlpService getNlpService() {
		return nlpService;
	}

	public void setNlpService(NlpService nlpService) {
		this.nlpService = nlpService;
	}

	public LuceneSearchService getSearchService() {
		return searchService;
	}

	public void setSearchService(LuceneSearchService searchService) {
		this.searchService = searchService;
	}
	 
}
