package com.jt.nlp.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.SortField;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jt.bean.gateway.PageMsg;
import com.jt.gateway.util.CMyString;
import com.jt.lucene.Article;
import com.jt.nlp.service.LuceneSearchService;
import com.jt.nlp.service.NlpService;
import com.jt.nlp.service.QAService;
import com.jt.searchHis.service.SearchHisRtService;

@Controller

public class QAManager {
	private static Logger logger = Logger.getLogger(QAManager.class);

	private PageMsg msg;
	private QAService qaService;
	private NlpService nlpService;
	private Gson gson;
	private SearchHisRtService searchHisRtService;
	private LuceneSearchService searchService_searchHis;
	public QAManager(){
		gson= new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	}
	public SearchHisRtService getSearchHisRtService() {
		return searchHisRtService;
	}
	@Resource(name="searchHisRtServiceImpl") 
	public void setSearchHisRtService(SearchHisRtService searchHisRtService) {
		this.searchHisRtService = searchHisRtService;
	}
	public QAService getQaService() {
		return qaService;
	}
	@Resource(name="qaService") 
	public void setQaService(QAService qaService) {
		this.qaService = qaService;
	}
	public NlpService getNlpService() {
		return nlpService;
	}
	@Resource(name="nlpService") 
	public void setNlpService(NlpService nlpService) {
		this.nlpService = nlpService;
	}

	public LuceneSearchService getSearchService_searchHis() {
		return searchService_searchHis;
	}
	@Resource(name="searchService_searchHis") 
	public void setSearchService_searchHis(LuceneSearchService searchService_searchHis) {
		this.searchService_searchHis = searchService_searchHis;
	}
	
	/**
	 * 场景映射词
	 * 不做NLP分析，分词检索
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="luceneSearch.do")
	public void luceneSearch(HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		msg=new PageMsg();
		msg.setSig(true);
		PrintWriter pw=null;
		try {
			pw=response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		String question=request.getParameter("question");
		String sBegin=request.getParameter("begin");
		String sEnd=request.getParameter("end");

		
		//增加解码
		try {
			question=URLDecoder.decode(question, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			question="";
		}
		
		int iBegin=0;
		int iEnd=0;
		try {
			iBegin=Integer.parseInt(sBegin);
		}catch(Exception e){
			iBegin=0;
		}
		try {
			iEnd=Integer.parseInt(sEnd);
		}catch(Exception e){
			iEnd=5;
		}
		if(question==null){
			question="";
		}
		String[] arrQuestion=question.split(" ");;
				
		LuceneSearchService luceneService=qaService.getSearchService();
		
		//检索参数
		String [] searchField=new String[arrQuestion.length];
		Occur[] occurs = new Occur[arrQuestion.length]; 
		for(int i=0;i<searchField.length;i++){
			searchField[i]=Article.getMapedFieldName("title");
			occurs[i]=Occur.MUST;
		}
			
		//排序参数
		String[] sortField={Article.getMapedFieldName("date")};
		SortField.Type[] sortFieldType={SortField.Type.LONG};
		boolean[] reverse={true};
		boolean isRelevancy = true;
		
		
		List<Article> list=luceneService.searchArticle(arrQuestion, occurs, searchField, sortField, sortFieldType, reverse, isRelevancy, iBegin, iEnd);
		if(list!=null){
			msg.setMsg(list);
		}else{
			msg.setSig(false);
			msg.setMsg("没有检索到结果");
		}
		pw.print(gson.toJson(msg));
	}

	
	/**
	 * 检索历史的全文检索
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="luceneSearch_searchHis.do")
	public void luceneSearch_searchHis(HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		msg=new PageMsg();
		msg.setSig(true);
		PrintWriter pw=null;
		try {
			pw=response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		String question=request.getParameter("question");
		String sBegin=request.getParameter("begin");
		String sEnd=request.getParameter("end");
		//是否分词，适用于前台检索提示。默认为false
		String isParticle=CMyString.getStrNotNullor0(request.getParameter("isParticle"),"false");
		//问题词是否为或包含
		String isShould=CMyString.getStrNotNullor0(request.getParameter("isShould"),"false");
		
		
		//增加解码
		try {
			question=URLDecoder.decode(question, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			question="";
		}
		
		int iBegin=0;
		int iEnd=0;
		try {
			iBegin=Integer.parseInt(sBegin);
		}catch(Exception e){
			iBegin=0;
		}
		try {
			iEnd=Integer.parseInt(sEnd);
		}catch(Exception e){
			iEnd=5;
		}
		if(question==null){
			question="";
		}
		String[] arrQuestion={question};
		List<String> particleQuestion=new ArrayList<String>();
		
		if("true".equals(isParticle)){
			for(int i=0;i<arrQuestion.length;i++){
				particleQuestion.addAll(nlpService.getParticle(arrQuestion[i]));
			}
			arrQuestion=particleQuestion.toArray(arrQuestion);
		}
				
		//检索参数
		String [] searchField=new String[arrQuestion.length];
		Occur[] occurs = new Occur[arrQuestion.length]; 
		for(int i=0;i<searchField.length;i++){
			searchField[i]="SEARCHCONTENT";
			if("true".equals(isShould)){
				occurs[i]=Occur.SHOULD;
			}else{
				occurs[i]=Occur.MUST;
			}
		}
			
		//排序参数，按照相关度、检索次数、时间排序
		String[] sortField={"SEARCHTIMES","UPDATETIME"};
		SortField.Type[] sortFieldType={SortField.Type.LONG,SortField.Type.LONG};
		boolean[] reverse={true,true};
		boolean isRelevancy = true;
		
		List<Document> list=searchService_searchHis.search(arrQuestion, occurs, searchField, sortField, sortFieldType, reverse, isRelevancy, iBegin, iEnd);
		
		//按照SearchHis的字段进行格式化
		List<Map<String,String>> formatList=new ArrayList<Map<String,String>>();
		Map<String,String> columnMap=null;
		for(Document doc:list){
			columnMap=new HashMap<String,String>();
			//ID，SEARCHCONTENT,SEARCHTIMES,CREATETIME,UPDATETIME
			columnMap.put("ID",doc.get("ID"));
			columnMap.put("SEARCHCONTENT",doc.get("SEARCHCONTENT"));
			columnMap.put("SEARCHTIMES",doc.get("SEARCHTIMES"));
			columnMap.put("CREATETIME",doc.get("CREATETIME"));
			columnMap.put("UPDATETIME",doc.get("UPDATETIME"));
			formatList.add(columnMap);
		}
		
		if(formatList.size()>0){
			msg.setMsg(formatList);
		}else{
			msg.setSig(false);
			msg.setMsg("没有检索到结果");
		}
		pw.print(gson.toJson(msg));
	}
	
	/**
	 * NLP分析之后再进行检索
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="qaSearch.do")
	public void qaSearch(HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		msg=new PageMsg();
		msg.setSig(true);
		PrintWriter pw=null;
		try {
			pw=response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		String question=request.getParameter("question");
		String sBegin=request.getParameter("begin");
		String sEnd=request.getParameter("end");
		String category=request.getParameter("category");
		//增加解码
		try {
			question=URLDecoder.decode(question, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			question="";
		}
		
		int iBegin=0;
		int iEnd=0;
		try {
			iBegin=Integer.parseInt(sBegin);
		}catch(Exception e){
			iBegin=0;
		}
		try {
			iEnd=Integer.parseInt(sEnd);
		}catch(Exception e){
			iEnd=5;
		}
		if(question==null){
			question="";
		}
		List<Article> list=null;
		if(category==null||category.length()==0){
			list=qaService.QASearch(question, iBegin,iEnd);
		}else{
			list=qaService.QASearchByCategory(question, category, iBegin, iEnd);
		}
		if(list!=null){
			msg.setMsg(list);
		}else{
			msg.setSig(false);
			msg.setMsg("没有检索的结果");
		}
		pw.print(gson.toJson(msg));
		
		//是否需要记录历史，默认为false
		String isStorgeHis=CMyString.getStrNotNullor0(request.getParameter("isStorgeHis"), "false");
		if("true".equalsIgnoreCase(isStorgeHis)){
			//将问题写入缓存
			if(question.length()>0){
				String sSearchHisId=request.getParameter("searchHisId");
				Long searchHisId=0l;
				try {
					searchHisId=Long.parseLong(sSearchHisId);
				} catch (Exception e) {
					logger.info("转换检索历史ID为long失败，改为默认值0");
					searchHisId=0l;
				}
				searchHisRtService.add(question, searchHisId);
			}
		}

		
	}
}
