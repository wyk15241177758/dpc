/**
 * 对外提供QA功能的主服务，由此服务调用nlp和Lucene功能
 */
package com.jt.nlp.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.lucene.search.BooleanClause.Occur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.keyword.bean.KeyWord;
import com.jt.lucene.Article;
import com.jt.lucene.IndexDao;
@Service
public class QAService {
	private static final Logger LOG = LoggerFactory.getLogger(QAService.class);

	private NlpService nlpService;
	@Autowired
	private LuceneSearchService searchService;
	//本地测试使用,indexpath为索引所在目录
	public QAService(String indexPath){
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
	//nlp第一次分析初始化很慢，不知道怎么初始化，直接触发一次检索
	public void initNlp(){
		QASearch("初始化",1);
//		System.out.println("######"+(searchService==null));
		LOG.info("正在初始化NLP");
	}

	 /**
	  * 预设场景，留空
	  * @param word
	  * @return
	  * 查询问题是否包含预设场景的词汇，此方法应该在lucene检索和NLP分析之前
	  */
	 private List<KeyWord> presentScene(String question){
		 boolean presented=false;
		 //getKeyWords("预设场景")
		 List<KeyWord> keyWordList= null;
		 List<KeyWord> tagList=new ArrayList<KeyWord>();
		 for(KeyWord tag:tagList){
			 //遍历标签，
			 //根据标签获得其对应的关键词
			 keyWordList=null;
		 }
		 return keyWordList;
	 }
	 
	 public List<Article> QASearch(String question,int size){
		return QASearch(question,0,size);
	 }
	 
	 public List<Article> QASearch(String question,int begin,int end){
		 //检索结果
		 List<Article> list=new ArrayList<Article>();
		 //预设场景
		 List<KeyWord> keyWordList=null;
		 keyWordList=presentScene(question);
		 //未进入预设场景
		 if(keyWordList==null||keyWordList.size()==0){
			 Set<String> questionSet=nlpService.getSearchWords(question);
			 String[] searchWord=new String[questionSet.size()];
			 questionSet.toArray(searchWord);
			 return searchService.searchArticle(searchWord, Article.getMapedFieldName("title"), begin, end);
		 }else{
			 
		 }
		 return list;
	 }
	 
	 //根据分类检索
	 public List<Article> QASearchByCategory(String question,String category,int begin,int end){
		 List<Article> list=new ArrayList<Article>();
		 list=null;
		 //未进入预设场景
		 if(list==null){
			 Set<String> questionSet=nlpService.getSearchWords(question);
			 String questionStr="";
			 //用空格分隔，lucene自动分词，实现or效果
			 for(String str:questionSet){
				 questionStr+=str+" ";
			 }
			 //分类作为必须包含的字段进行检索
			 
			 String[] searchWord={questionStr,category};
			 Occur[] occurs={Occur.MUST,Occur.MUST};
			 String[] fields={Article.getMapedFieldName("title"),Article.getMapedFieldName("category")};
			 return searchService.searchArticle(searchWord, occurs, fields, null, null, false, begin, end);
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
		QAService qa=new QAService("d://indexpath");
//		LuceneSearchService search=qa.getSearchService();
//		String[] arr={"中国"};
//		List<Article>list=qa.QASearch("电影《晚上》的导演张一毛，是我们值得信任的导演", 10);
//		List<Article>list=search.searchArticle(arr, Article.getMapedFieldName("title"), 0, 10);
		List<Article> list=qa.getSearchService().searchAll(100000,"id",false);
		System.out.println("检索list长度="+list.size());
//		List<Article> list=search.searchArticle(arr, Occur.SHOULD, Article.getMapedFieldName("title"),null,null, false, 0, -1);
		PrintWriter pw=null;
		try {
			pw=new PrintWriter(new File("/test.txt"));
			for(Article doc:list){
				pw.println(doc);
			}
			pw.flush();
			pw.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
}
