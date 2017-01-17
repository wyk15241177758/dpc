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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.SortField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jt.lucene.Article;
import com.jt.lucene.IndexDao;
import com.jt.scene.bean.ScenePage;
import com.jt.scene.bean.SceneWord;
import com.jt.scene.service.SceneWordService;

@Service
public class QAService {
	private static final Logger LOG = LoggerFactory.getLogger(QAService.class);
	private boolean isPageContinue;
	private NlpService nlpService;
	private LuceneSearchService searchService;
	private SceneWordService sceneWordService;
	//存储场景的关联分类
	private Set<String> sceneSjflSet;
	//存储检索结果
	private Map<String,List<Article>> qaResultMap;
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
		sceneSjflSet=new HashSet<String>();
		qaResultMap=new HashMap<String,List<Article>>();
	}

	// nlp第一次分析初始化很慢，不知道怎么初始化，直接触发一次检索
	public void initNlp() {
		QASearch("初始化", 0,1);
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
					//将场景关联的分类放入list，此后的检索只遍历此list（如果有值）
					String sceneSjfl=sceneWord.getSjfl();
					if(sceneSjfl!=null&&sceneSjfl.length()>0){
						sceneSjflSet.addAll(Arrays.asList(sceneSjfl.split(";")));
					}
					//是否有关联页面，如果有则按照当前场景的关联分类筛选，不属于当前场景关联页面子类的过滤掉
					List<ScenePage> pageList=sceneWord.getScenePageList();
					if(pageList!=null&&pageList.size()>0){
						for(int i=0;i<pageList.size();i++){
							ScenePage curPage=pageList.get(i);
							if(isContainSjfl(sceneSjfl, curPage.getSjfl())){
								pushQaRsMap(curPage);
							}
						}
					}
				}
			}
		}
		return sceneWordSet;
	}

	//判断这个预设页面的分类是否在场景分类范围之内
	private boolean isContainSjfl(String sceneSjfl,String pageSjfl){
		if(sceneSjfl==null||pageSjfl==null){
			return false;
		}
		for(String str:sceneSjfl.split(";")){
			for(String pSjfl:pageSjfl.split(";")){
				if(str.length()>0&&str.equals(pSjfl)){
					return true;
				}
			}
		}
		return false;
	}
	
	//将预设页面放入检索结果
	private void pushQaRsMap(ScenePage page){
		String[] pageSjfl=page.getSjfl().split(";");
		for(int i=0;i<pageSjfl.length;i++){
			List<Article> articleList=qaResultMap.get(pageSjfl[i]);
			if(articleList==null){
				articleList=new ArrayList<Article>();
			}
			Article article=new Article();
			article.setTitle(page.getPageTitle());
			article.setUrl(page.getPageLink());
			articleList.add(article);
			qaResultMap.put(pageSjfl[i], articleList);
		}
	}
	

	// 根据分类检索
	public Map<String,List<Article>> QASearch(String question, int begin, int end) {
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

		String[] searchWord = { questionStr, "" };
		Occur[] occurs = { Occur.MUST, Occur.MUST };
		String[] fields = { Article.getMapedFieldName("title"), Article.getMapedFieldName("category") };
		
		
		//排序参数
		String[] sortField={Article.getMapedFieldName("date")};
		SortField.Type[] sortFieldType={SortField.Type.LONG};
		boolean[] reverse={true};
		boolean isRelevancy = true;

		//存在预设场景关联分类，则遍历指定的分类，否则遍历系统配置的默认分类
		if(sceneSjflSet.size()>0){
			for(String str:sceneSjflSet){
				//赋值分类value
				searchWord[1]=str;
				List<Article> rsList=qaResultMap.get(str);
				if(rsList==null){
					rsList=new ArrayList<Article>();
				}
				//配置项，如果已有预设页面，是否还继续做检索
				if(isPageContinue){
					if(rsList.size()<end){
						rsList.addAll(searchService.searchArticle(searchWord, occurs, fields, sortField, sortFieldType, reverse, isRelevancy, begin, end-rsList.size()));
					}
				}else{
					if(rsList.size()==0){
						rsList.addAll(searchService.searchArticle(searchWord, occurs, fields, sortField, sortFieldType, reverse, isRelevancy, begin, end));
					}
				}
				qaResultMap.put(str, rsList);
			}
		}
		
		
		return qaResultMap;
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
