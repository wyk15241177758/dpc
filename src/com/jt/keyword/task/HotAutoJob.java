package com.jt.keyword.task;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import org.apache.log4j.Logger;
import com.jt.keyword.bean.ParseHotWord;
import com.jt.keyword.service.ParseHotWordService;
import com.jt.lucene.Article;
import com.jt.nlp.service.LuceneSearchService;
/**
 * 热点词定时任务
 * @author 邹许红
 * @date   2016/12/05
 *
 */
public class HotAutoJob {
	private static Logger logger = Logger.getLogger(HotAutoJob.class);
	private LuceneSearchService  searchService;
	private ParseHotWordService   hotWordService;

	public void  parse(List<Article> articles){
		 if(articles==null||articles.size()==0){
			   return;
		   }
		  for (int i = 0; i < articles.size(); i++) {
			  Article article= articles.get(i);
			  if(article!=null){
				  String title = article.getTitle();
			  if(!ParamUtil.titleList.contains(title)){
					ParamUtil.titleList.offer(title);
				}}
				
		}
			
		 
	}
	
	public   void  autoJobTask(){
		 ParamUtil.map=new ConcurrentHashMap<String, Integer>();
		logger.info("分词统计开始");
		int  page=1;
		while (true) {
			List<Article> articles = searchService.searchAll(page, 1000, Article.getMapedFieldName("id"), false);
		   if(articles==null||articles.size()==0){
			   break;
		   }else{
			   parse(articles);
		   }
			page++;
		}
		
		ParamUtil.totalpools=4;
		ParamUtil.parselatch=new CountDownLatch(ParamUtil.totalpools);
		int j = 0;
		long  begin=System.currentTimeMillis();
		for (; j < ParamUtil.totalpools; j++) {
			HotTask  hotTask=new HotTask();
			hotTask.start();
		}
		try {
			ParamUtil.parselatch.await();
			long  end=System.currentTimeMillis();
			logger.info("任务执行花费时间："+(end-begin));
		} catch (InterruptedException e) {
			logger.error(e);
		}
		logger.info("分词统计结束");
		
		
		logger.info("TRUNCATE开始");
		hotWordService.truncate();
		logger.info("TRUNCATE结束");
		logger.info("入库开始");
		ConcurrentHashMap<String, Integer> map = ParamUtil.map;
		  for(Map.Entry<String, Integer> me : map.entrySet()) {
			  ParseHotWord  parseHotWord=new  ParseHotWord();
			  parseHotWord.setCreateTime(new Date());
			  parseHotWord.setHotword(me.getKey());
			  parseHotWord.setNum(me.getValue());
			  hotWordService.save(parseHotWord);
	        }
			logger.info("TRUNCATE结束");
			logger.info("入库结束");

		
	}
	
	

	public LuceneSearchService getSearchService() {
		return searchService;
	}

	public void setSearchService(LuceneSearchService searchService) {
		this.searchService = searchService;
	}

	public ParseHotWordService getHotWordService() {
		return hotWordService;
	}

	public void setHotWordService(ParseHotWordService hotWordService) {
		this.hotWordService = hotWordService;
	}
	
	
	
	
	
}
