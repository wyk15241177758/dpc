/**
 * 对外提供QA功能的主服务，由此服务调用nlp和Lucene功能
 */
package com.jt.nlp.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.SortField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
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
	private String sPageContinue;
	private NlpService nlpService;
	private LuceneSearchService searchService;
	private SceneWordService sceneWordService;
	//不分词词典
	private Resource noParticipleWordResource;
	private List<String> noParticipleWordList;
	//停用词词典
	private Resource stopWordResource;
	private List<String> stopWordList;
	//存储场景的关联分类
//	private Set<String> sceneSjflSet;
//	private Map<String,List<Article>> qaResultMap;
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
	}

	// nlp第一次分析初始化很慢，不知道怎么初始化，直接触发一次检索
	public void initNlp() {
		//读取不分词的配置文件，替换问题时自动添加双引号
		noParticipleWordList=getDict(noParticipleWordResource,false);
		//读取停用词词典，为了防止分词增加双引号
		stopWordList=getDict(stopWordResource,true);
		LOG.info("不分词词典内容为："+noParticipleWordList+"");
		LOG.info("停用词词典内容为："+stopWordList+"");
		QASearch("初始化", 0,1);
		// System.out.println("######"+(searchService==null));
		LOG.info("正在初始化NLP");
	}

	private List<String> getDict(Resource resource,boolean addQuote){
		List<String> list=new ArrayList<String>();
		BufferedReader br=null;
		 try {
			File file = resource.getFile();
			br=new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			String dict=br.readLine();
			while(dict!=null){
				if(dict.startsWith("#")){
					dict=br.readLine();
					continue;
				}
				if(addQuote){
					list.add("\""+dict+"\"");
				}else{
					list.add(dict);
				}
				dict=br.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(br!=null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		 return list;
	}
	
	
	public List<String> getStopWordList() {
		return stopWordList;
	}

	public void setStopWordList(List<String> stopWordList) {
		this.stopWordList = stopWordList;
	}

	public List<String> getNoParticipleWordList() {
		return noParticipleWordList;
	}

	public void setNoParticipleWordList(List<String> noParticipleWordList) {
		this.noParticipleWordList = noParticipleWordList;
	}



	public Resource getNoParticipleWordResource() {
		return noParticipleWordResource;
	}

	public void setNoParticipleWordResource(Resource noParticipleWordResource) {
		this.noParticipleWordResource = noParticipleWordResource;
	}

	public Resource getStopWordResource() {
		return stopWordResource;
	}

	public void setStopWordResource(Resource stopWordResource) {
		this.stopWordResource = stopWordResource;
	}

	public String getsPageContinue() {
		return sPageContinue;
	}

	public void setsPageContinue(String sPageContinue) {
		this.sPageContinue = sPageContinue;
		if("true".equals(sPageContinue)){
			this.isPageContinue=true;
		}else{
			this.isPageContinue=false;
		}
	}

	/**
	 * 预设场景，留空
	 * 
	 * @param word
	 * @return 查询问题是否包含预设场景的词汇，此方法应该在lucene检索和NLP分析之前
	 */
	private Set<String> presentScene(String question,List<String> sceneSjflList,Map<String,List<Article>> qaResultMap) {
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
						sceneSjflList.addAll(Arrays.asList(sceneSjfl.split(";")));
					}
					//是否有关联页面，如果有则按照当前场景的关联分类筛选，不属于当前场景关联页面子类的过滤掉
					List<ScenePage> pageList=sceneWord.getScenePageList();
					if(pageList!=null&&pageList.size()>0){
						for(int i=0;i<pageList.size();i++){
							ScenePage curPage=pageList.get(i);
							if(isContainSjfl(sceneSjfl, curPage.getSjfl())){
								pushQaRsMap(curPage,qaResultMap);
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
	private Map<String,List<Article>> pushQaRsMap(ScenePage page,Map<String,List<Article>> map){
		String[] pageSjfl=page.getSjfl().split(";");
		for(int i=0;i<pageSjfl.length;i++){
			List<Article> articleList=map.get(pageSjfl[i]);
			if(articleList==null){
				articleList=new ArrayList<Article>();
			}
			Article article=new Article();
			article.setTitle(page.getPageTitle());
			article.setUrl(page.getPageLink());
			article.setHtml(page.getHtml());
			articleList.add(article);
			map.put(pageSjfl[i], articleList);
		}
		return map;
	}
	

	public boolean isPageContinue() {
		return isPageContinue;
	}

	public void setPageContinue(boolean isPageContinue) {
		this.isPageContinue = isPageContinue;
	}

	// 根据分类检索
	public Map<String,List<Article>> QASearch(String question, int begin, int end) {
		// 检索词
		Set<String> questionSet = null;
		// 预设场景
		Set<String> sceneWordSet = null;
		
		List<String> sceneSjflList = new ArrayList<String>();
		Map<String,List<Article>> qaResultMap= new LinkedHashMap<String,List<Article>>();
		sceneWordSet = presentScene(question,sceneSjflList,qaResultMap);
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
			//增加双引号，不再分词
//			for(String noParticiple:noParticipleWordList){
//				if(str.indexOf(noParticiple)!=-1){
//					str=str.replaceAll(noParticiple, "\""+noParticiple+"\"");
//				}
//			}
			questionStr += "\""+str + "\" ";
		}
		// 分类作为必须包含的字段进行检索，如下三个变量长度必须相同

		String[] searchWord = new String[stopWordList.size()+2];
		//{ questionStr, "" };
		Occur[] occurs = new Occur[searchWord.length];
		//{ Occur.MUST, Occur.MUST };
		String[] fields = new String[searchWord.length];
		//{ Article.getMapedFieldName("title"), Article.getMapedFieldName("category") };
		searchWord[0]=questionStr;
		occurs[0]=Occur.MUST;
		occurs[1]=Occur.MUST;
		fields[0]=Article.getMapedFieldName("title");
		fields[1]=Article.getMapedFieldName("category");
		for(int i=0;i<stopWordList.size();i++){
			searchWord[i+2]=stopWordList.get(i);
			occurs[i+2]=Occur.MUST_NOT;
			fields[i+2]=Article.getMapedFieldName("title");
		}
		
		
		//排序参数
		String[] sortField={Article.getMapedFieldName("date")};
		SortField.Type[] sortFieldType={SortField.Type.LONG};
		boolean[] reverse={true};
		boolean isRelevancy = true;

		//存在预设场景关联分类，则遍历指定的分类，否则遍历系统配置的默认分类
		if(sceneSjflList.size()>0){
			for(String str:sceneSjflList){
				//赋值分类value，分类也不再分词
				searchWord[1]="\""+str+"\"";
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
		}else{
			String qaSjfl=sceneWordService.getQaSjfl();
			if(qaSjfl!=null&&qaSjfl.length()>0){
				String[] sjfl=qaSjfl.split(";");
				for(String str:sjfl){
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
		}
		
		
		return qaResultMap;
	}
	
	
	//先按照相似度查询，返回评分大于指定分数的检索结果
	private List<Article> luceneSearchTop(Set<String> questionSet,String category,int score){
		List<Article> rsList=new ArrayList<Article>();
		String questionStr="";
		//相似性检索还是需要lucene自动分词
		for (String str : questionSet) {
			questionStr += str + " ";
		}
		
		String[] questionArr=new String[questionSet.size()];
		questionSet.toArray(questionArr);
		
		// 分类作为必须包含的字段进行检索，如下三个变量长度必须相同

		String[] searchWord = new String[stopWordList.size()+2];
		Occur[] occurs = new Occur[searchWord.length];
		String[] fields = new String[searchWord.length];
		searchWord[0]=questionStr;
		occurs[0]=Occur.MUST;
		occurs[1]=Occur.MUST;
		fields[0]=Article.getMapedFieldName("title");
		fields[1]=Article.getMapedFieldName("category");
		for(int i=0;i<stopWordList.size();i++){
			searchWord[i+2]=stopWordList.get(i);
			occurs[i+2]=Occur.MUST_NOT;
			fields[i+2]=Article.getMapedFieldName("title");
		}
		
		
		//排序参数
		String[] sortField={Article.getMapedFieldName("date")};
		SortField.Type[] sortFieldType={SortField.Type.LONG};
		boolean[] reverse={true};
		boolean isRelevancy = true;
		
		
		return rsList;
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
