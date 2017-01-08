/**
 * 对外提供QA功能的主服务，由此服务调用nlp和Lucene功能
 */
package com.jt.nlp.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.search.BooleanClause.Occur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jt.lucene.Article;
import com.jt.lucene.IndexDao;
import com.jt.scene.bean.SceneWord;
import com.jt.scene.service.SceneWordService;

@Service
public class QAService {
	private static final Logger LOG = LoggerFactory.getLogger(QAService.class);

	private NlpService nlpService;
	private LuceneSearchService searchService;
	private SceneWordService sceneWordService;
	
	// 本地测试使用,indexpath为索引所在目录
	public QAService(String indexPath) {
		nlpService = new NlpService();
		searchService = new LuceneSearchService();
		try {
			searchService.setDao(new IndexDao(indexPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public QAService() {
		nlpService = new NlpService();
	}

	// nlp第一次分析初始化很慢，不知道怎么初始化，直接触发一次检索
	public void initNlp() {
		QASearch("初始化", 1);
		// System.out.println("######"+(searchService==null));
		LOG.info("正在初始化NLP");
	}

	/**
	 * 预设场景，留空
	 * 
	 * @param word
	 * @return 查询问题是否包含预设场景的词汇，此方法应该在lucene检索和NLP分析之前
	 */
	private Set<String> presentScene(String question) {
		Set<String> sceneWordSet = new HashSet<String>();
		List<String> questionParticleList = nlpService.getParticle(question);
		List<SceneWord> sceneWordList = sceneWordService.getAllSceneWords();
		// 循环分词之后的问题
		for (String str : questionParticleList) {
			// 循环所有的场景映射词，其入口词与分词之后的问题匹配，简单的indexOf，
			// 用equals太严格。匹配到就把对应的出口词按照分号分割，压入返回的set
			for (SceneWord sceneWord : sceneWordList) {
				if (sceneWord.getEnterWords().indexOf(str) != -1) {
					sceneWordSet.addAll(Arrays.asList(sceneWord.getOutWords().split(";")));
				}
			}
		}
		return sceneWordSet;
	}

	public List<Article> QASearch(String question, int size) {
		return QASearch(question, 0, size);
	}

	public List<Article> QASearch(String question, int begin, int end) {
		// 检索结果
		List<Article> list = new ArrayList<Article>();
		// 检索词
		Set<String> questionSet = null;
		// 预设场景
		Set<String> sceneWordSet = null;
		sceneWordSet = presentScene(question);
		questionSet = nlpService.getSearchWords(question);
		// 未进入预设场景
		if (sceneWordSet != null && sceneWordSet.size() > 0) {
			LOG.info("进入预设场景，获得映射词"+sceneWordSet);
			questionSet.addAll(sceneWordSet);
		} 
		LOG.info("实际检索词:"+questionSet);
		if(questionSet==null||questionSet.size()==0)return null;
		
		String[] searchWord = new String[questionSet.size()];
		questionSet.toArray(searchWord);
		return searchService.searchArticle(searchWord, Article.getMapedFieldName("title"), begin, end);
	}

	// 根据分类检索
	public List<Article> QASearchByCategory(String question, String category, int begin, int end) {
		List<Article> list = new ArrayList<Article>();
		// 检索词
		Set<String> questionSet = null;
		// 预设场景
		Set<String> sceneWordSet = null;
		sceneWordSet = presentScene(question);
		questionSet = nlpService.getSearchWords(question);
		// 未进入预设场景
		if (sceneWordSet != null && sceneWordSet.size() > 0) {
			LOG.info("进入预设场景，获得映射词"+sceneWordSet);
			questionSet.addAll(sceneWordSet);
		} 
		LOG.info("实际检索词:"+questionSet);
		if(questionSet==null||questionSet.size()==0)return null;

		String questionStr = "";
		// 用空格分隔，lucene自动分词，实现or效果
		for (String str : questionSet) {
			questionStr += str + " ";
		}
		// 分类作为必须包含的字段进行检索

		String[] searchWord = { questionStr, category };
		Occur[] occurs = { Occur.MUST, Occur.MUST };
		String[] fields = { Article.getMapedFieldName("title"), Article.getMapedFieldName("category") };
		return searchService.searchArticle(searchWord, occurs, fields, null, null, false, begin, end);
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

	public SceneWordService getSceneWordService() {
		return sceneWordService;
	}

	public void setSceneWordService(SceneWordService sceneWordService) {
		this.sceneWordService = sceneWordService;
	}

	public static void main(String[] args) {
		QAService qa = new QAService("d://indexpath");
		// LuceneSearchService search=qa.getSearchService();
		// String[] arr={"中国"};
		// List<Article>list=qa.QASearch("电影《晚上》的导演张一毛，是我们值得信任的导演", 10);
		// List<Article>list=search.searchArticle(arr,
		// Article.getMapedFieldName("title"), 0, 10);
		List<Article> list = qa.getSearchService().searchAll(100000, "id", false);
		System.out.println("检索list长度=" + list.size());
		// List<Article> list=search.searchArticle(arr, Occur.SHOULD,
		// Article.getMapedFieldName("title"),null,null, false, 0, -1);
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new File("/test.txt"));
			for (Article doc : list) {
				pw.println(doc);
			}
			pw.flush();
			pw.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
}
