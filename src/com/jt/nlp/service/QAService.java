/**
 * 对外提供QA功能的主服务，由此服务调用nlp和Lucene功能
 */
package com.jt.nlp.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


import com.jt.lucene.Article;
import com.jt.lucene.IndexDao;

public class QAService {
	private NlpService nlpService;
	private LuceneSearchService searchService;
	//本地测试使用
	public QAService(String a){
		String indexPath="/indexpath";
		nlpService=new NlpService();
		searchService=new LuceneSearchService();
		try {
			searchService.setDao(new IndexDao(indexPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public QAService(){
		nlpService=new NlpService();
	}
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
		return QASearch(question,0,size);
	 }
	 
	 public List<Article> QASearch(String question,int begin,int end){
		 List<Article> list=new ArrayList<Article>();
		 list=presentScene();
		 //未进入预设场景
		 if(list==null){
			 Set<String> questionSet=nlpService.getSearchWords(question);
			 String[] searchWord=new String[questionSet.size()];
			 questionSet.toArray(searchWord);
			 return searchService.searchArticle(searchWord, Article.getMapedFieldName("title"), begin, end);
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
	 public static void main(String[] args) {
		QAService qa=new QAService("");
//		LuceneSearchService search=qa.getSearchService();
//		String[] arr={"中国"};
		List<Article>list=qa.QASearch("电影《晚上》的导演张一毛，是我们值得信任的导演", 10);
//		List<Article>list=search.searchArticle(arr, Article.getMapedFieldName("title"), 0, 10);
		System.out.println("检索list长度="+list.size());
//		List<Article> list=search.searchArticle(arr, Occur.SHOULD, Article.getMapedFieldName("title"),null,null, false, 0, -1);
		for(Article doc:list){
			System.out.println(doc);
		}
	}
}
