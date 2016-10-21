package com.jt.nlp.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jt.bean.gateway.PageMsg;
import com.jt.lucene.Article;
import com.jt.nlp.service.LuceneSearchService;
import com.jt.nlp.service.QAService;

@Controller

public class QAManager {
	private PageMsg msg;
	private QAService qaService;
	private Gson gson;
	public QAManager(){
		gson= new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
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
		List<Article> list=luceneService.searchArticle(arrQuestion, "xq_title", iBegin, iEnd);
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
		List<Article> list=qaService.QASearch(question, iBegin,iEnd);
		if(list!=null){
			msg.setMsg(list);
		}else{
			msg.setMsg("没有检索的结果");
		}
		pw.print(gson.toJson(msg));
	}
}
