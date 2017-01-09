package com.jt.nlp.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.search.SortField;
import org.apache.lucene.search.BooleanClause.Occur;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jt.bean.gateway.PageMsg;
import com.jt.lucene.Article;
import com.jt.nlp.service.LuceneSearchService;
import com.jt.nlp.service.QAService;
import com.jt.searchHis.service.SearchHisRtService;

@Controller

public class QAManager {
	private PageMsg msg;
	private QAService qaService;
	private Gson gson;
	private SearchHisRtService searchHisRtService;
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
	/**
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
		String[] arrQuestion=question.split(" ");
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
			msg.setMsg("没有检索的结果");
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
			msg.setMsg("没有检索的结果");
		}
		pw.print(gson.toJson(msg));
		
		//将问题写入缓存
		if(question.length()>0){
			String sSearchHisId=request.getParameter("searchHisId");
			Long searchHisId=0l;
			try {
				searchHisId=Long.parseLong(sSearchHisId);
			} catch (Exception e) {
				System.out.println("转换检索历史ID为long失败，改为默认值0");
				searchHisId=0l;
			}
			searchHisRtService.add(question, searchHisId);
		}
		
	}
}
