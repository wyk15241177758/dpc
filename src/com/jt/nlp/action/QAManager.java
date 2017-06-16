package com.jt.nlp.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.SortField;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jt.bean.gateway.PageMsg;
import com.jt.gateway.util.CMyString;
import com.jt.lucene.Article;
import com.jt.lucene.DocumentUtils;
import com.jt.lucene.LuceneUtils;
import com.jt.nlp.service.LuceneSearchService;
import com.jt.nlp.service.NlpService;
import com.jt.nlp.service.QAService;
import com.jt.searchHis.service.SearchHisRtService;

@Controller
@RequestMapping("/web")

public class QAManager {
	private static Logger logger = Logger.getLogger(QAManager.class);

	private PageMsg msg;
	private QAService qaService;
	private NlpService nlpService;
	private Gson gson;
	private SearchHisRtService searchHisRtService;
	private LuceneSearchService searchService_searchHis;
	public QAManager(){
		gson= new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
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
	 * 按照栏目搜索，按照时间倒序排序
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="searchByChannel.do")
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
		
		
		String channel=request.getParameter("channel");
		String sBegin=request.getParameter("begin");
		String sEnd=request.getParameter("end");
		
		//增加解码
		
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
		if(channel==null||channel.length()==0){
			msg.setSig(false);
			msg.setMsg("条件为空");
			String callback=request.getParameter("callback");
			if(callback!=null&&callback.length()>0){
				pw.print(callback+"("+gson.toJson(msg)+")");
			}else{
				pw.print(gson.toJson(msg));
			}
			return;
		}
		//Lucene检索条件
		String[] queryArr={channel};
		Occur[] occurArr={Occur.MUST};
		String[] fieldArr={Article.getMapedFieldName("channel")};
		
		//排序参数，按照时间倒序排序
		String[] sortField={Article.getMapedFieldName("date")};
		SortField.Type[] sortFieldType={SortField.Type.LONG};
		boolean[] reverse={true};
		boolean isRelevancy = true;
		
		List<Article> list=qaService.getSearchService().searchArticle(queryArr, occurArr, fieldArr, 
				sortField, sortFieldType, reverse, isRelevancy, iBegin, iEnd);
		
		if(list!=null&&list.size()>0){
			msg.setMsg(list);
		}else{
			msg.setSig(false);
			msg.setMsg("没有检索到结果");
		}
		String callback=request.getParameter("callback");
		if(callback!=null&&callback.length()>0){
			pw.print(callback+"("+gson.toJson(msg)+")");
		}else{
			pw.print(gson.toJson(msg));
		}
		
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
		String callback=request.getParameter("callback");
		if(callback!=null&&callback.length()>0){
			pw.print(callback+"("+gson.toJson(msg)+")");
		}else{
			pw.print(gson.toJson(msg));
		}
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
		msg.setSig(false);
		msg.setMsg("没有检索到结果");
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
		Map<String,List<Article>> map=null;
			map=qaService.QASearch(question, iBegin,iEnd);
		if(map!=null&&map.size()!=0){
			//map中key值对应的value为空也算没有结果
			for(Entry<String, List<Article>> entry:map.entrySet()){
				if(entry!=null&&entry.getValue()!=null&&entry.getValue().size()!=0){
					msg.setSig(true);
					break;
				}
			}
			if(msg.isSig()){
				msg.setMsg(map);
			}
		}
		String callback=request.getParameter("callback");
		if(callback!=null&&callback.length()>0){
			pw.print(callback+"("+gson.toJson(msg)+")");
		}else{
			pw.print(gson.toJson(msg));
		}
		
		
		//是否需要记录历史，默认为false
		String isStorgeHis=CMyString.getStrNotNullor0(request.getParameter("isStorgeHis"), "false");
		if("true".equalsIgnoreCase(isStorgeHis)){
			//将问题写入缓存
			if(question.length()>0){
				searchHisRtService.add(question);
			}
		}

		
	}
	

	/**
	 * 智能提示：标签相似检索
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="simSearchJson.do")
	public void simSearchJson(HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		msg=new PageMsg();
		msg.setSig(false);
		msg.setMsg("没有到检索结果");
		PrintWriter pw=null;
		try {
			pw=response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		String tag = request.getParameter("tag");
		String title=request.getParameter("title");
		String sBegin=request.getParameter("begin");
		String sEnd=request.getParameter("end");
		
		
		List<Document> list_keyWord=null;
		List<Document> list=null;
		List<Article> list_rs=new ArrayList<Article>();
		Map<Object,Object> map_rs=new HashMap<Object,Object>();
		String keyWord="";
		
		int iBegin=0;
		int iEnd=0;
		try {
			iBegin=Integer.parseInt(sBegin);
		}catch(Exception e){
			iBegin=0;
		}
		try {
			//排除自己，所以多搜索一条
			iEnd=Integer.parseInt(sEnd)+1;
		}catch(Exception e){
			iEnd=5;
		}
		if((title==null||title.trim().length()==0)&&(tag==null||tag.trim().length()==0)){
			String callback=request.getParameter("callback");
			if(callback!=null&&callback.length()>0){
				pw.print(callback+"("+gson.toJson(msg)+")");
			}else{
				pw.print(gson.toJson(msg));
			}
			return;
		}
		
		Occur[] occurs ={Occur.MUST};
		//根据标题搜索，获得相关度最高的一篇文档，再根据其标签相似性搜索
		String [] searchField={Article.getMapedFieldName("title")};
		String[] orderField={Article.getMapedFieldName("date")};
		SortField.Type[] orderFieldType={SortField.Type.LONG};
		boolean[] reverse={true};
		boolean isRelevancy = true;
		
		
		//如果有标签参数，则直接按照标签参数检索，支持传入多个标签，用英文逗号分隔。如果是多个标签则必须全部包含
		if(tag!=null&&tag.length()>0){
			isRelevancy=false;
			keyWord=tag;
			if(keyWord!=null&&keyWord.trim().length()>0){
				//根据其标签相似性搜索
				String[] arrKeyWord=keyWord.split(",");
				//不分词
				for(int i=0;i<arrKeyWord.length;i++){
					arrKeyWord[i]="\""+arrKeyWord[i]+"\"";
				}
				
				occurs =new Occur[arrKeyWord.length];
				searchField=new String[arrKeyWord.length];
				for(int i=0;i<arrKeyWord.length;i++){
					occurs[i]=Occur.MUST;
					searchField[i]=Article.getMapedFieldName("keyWord");
				}
				list_keyWord=qaService.getSearchService().search(arrKeyWord, occurs, searchField, orderField, orderFieldType, reverse, isRelevancy, iBegin, iEnd);
			}
		}else{
			//替换特殊字符
			title=LuceneUtils.escapeQueryChars(title);	
			String[] arrTitle=new String[1];
			arrTitle[0]="\""+title+"\"";
			list=qaService.getSearchService().search(arrTitle, occurs, searchField, orderField, orderFieldType, reverse, isRelevancy, iBegin, iEnd);
			
			if(list==null||list.size()==0||list.get(0)==null){
				String callback=request.getParameter("callback");
				if(callback!=null&&callback.length()>0){
					pw.print(callback+"("+gson.toJson(msg)+")");
				}else{
					pw.print(gson.toJson(msg));
				}
				return;
			}else{
				isRelevancy=false;
				//获得相关度最高的一篇文档，获得其标签
				keyWord=DocumentUtils.document2Ariticle(list.get(0)).getKeyWord();
				if(keyWord!=null&&keyWord.trim().length()>0){
					//根据其标签相似性搜索
					String[] arrKeyWord=keyWord.split(";");
					//不分词
					for(int i=0;i<arrKeyWord.length;i++){
						arrKeyWord[i]="\""+arrKeyWord[i]+"\"";
					}
					occurs =new Occur[arrKeyWord.length];
					searchField=new String[arrKeyWord.length];
					for(int i=0;i<arrKeyWord.length;i++){
						occurs[i]=Occur.SHOULD;
						searchField[i]=Article.getMapedFieldName("keyWord");
					}
					list_keyWord=qaService.getSearchService().search(arrKeyWord, occurs, searchField, orderField, orderFieldType, reverse, isRelevancy, iBegin, iEnd);
				}
			}
		}

		//有标签的就显示标签 最相近且时间最新的几个数据，如果没有标签则相似性检索，也按照时间排序
		if(list_keyWord!=null&&list_keyWord.size()!=0){
			DocumentUtils.transCollection(list_keyWord, list_rs);
		}else if(list!=null&&list.size()!=0){
			DocumentUtils.transCollection(list, list_rs);
		}
		//排除自己，只删除一条
		for(int i=0;i<list_rs.size();i++){
			if(list_rs.get(i).getTitle().equalsIgnoreCase(title)){
				list_rs.remove(i);
				break;
			}
		}
		if(list!=null&&list.size()>0){
			Map<String,String> simDoc=new HashMap<String,String>();
			Article curArticle=DocumentUtils.document2Ariticle(list.get(0));
			map_rs.put("simDoc", curArticle);
		}else{
			map_rs.put("simTitle", "");
		}
		map_rs.put("tag", keyWord);
		map_rs.put("list", list_rs);
		msg.setSig(true);
		msg.setMsg(map_rs);
		String callback=request.getParameter("callback");
		if(callback!=null&&callback.length()>0){
			pw.print(callback+"("+gson.toJson(msg)+")");
		}else{
			pw.print(gson.toJson(msg));
		}
		
		
	}
	
	@RequestMapping(value={"/simSearch.do"})
	public  String  simSearch(HttpServletRequest request,HttpServletResponse response, ModelMap model){
		
		msg=new PageMsg();
		msg.setSig(false);
		msg.setMsg("没有到检索结果");
		
		String title=request.getParameter("title");
		String sBegin=request.getParameter("begin");
		String sEnd=request.getParameter("end");
		
		
		List<Document> list_keyWord=null;
		List<Document> list=null;
		List<Article> list_rs=new ArrayList<Article>();
		Map<Object,Object> map_rs=new HashMap<Object,Object>();
		String keyWord="";
		
		int iBegin=0;
		int iEnd=0;
		try {
			iBegin=Integer.parseInt(sBegin);
		}catch(Exception e){
			iBegin=0;
		}
		try {
			//排除自己，所以多搜索一条
			iEnd=Integer.parseInt(sEnd)+1;
		}catch(Exception e){
			iEnd=5;
		}
		if(title==null||title.trim().length()==0){
			model.addAttribute("msg",msg);
			return "simSearch";
		}
		
		//替换特殊字符
		title=LuceneUtils.escapeQueryChars(title);		
		String[] arrTitle=new String[1];
		arrTitle[0]=title;
		Occur[] occurs ={Occur.MUST};
		//根据标题搜索，获得相关度最高的一篇文档，再根据其标签相似性搜索
		String [] searchField={Article.getMapedFieldName("title")};
		String[] orderField={Article.getMapedFieldName("date")};
		SortField.Type[] orderFieldType={SortField.Type.LONG};
		boolean[] reverse={true};
		boolean isRelevancy = true;
		
		
		list=qaService.getSearchService().search(arrTitle, occurs, searchField, orderField, orderFieldType, reverse, isRelevancy, iBegin, iEnd);
		
		if(list==null||list.size()==0||list.get(0)==null){
			model.addAttribute("msg",msg);
			return "simSearch";
		}else{
			//获得相关度最高的一篇文档，获得其标签
			keyWord=DocumentUtils.document2Ariticle(list.get(0)).getKeyWord();
			if(keyWord!=null&&keyWord.trim().length()>0){
				//根据其标签相似性搜索
				String[] arrKeyWord=keyWord.split(";");
				occurs =new Occur[arrKeyWord.length];
				searchField=new String[arrKeyWord.length];
				for(int i=0;i<arrKeyWord.length;i++){
					occurs[i]=Occur.SHOULD;
					searchField[i]=Article.getMapedFieldName("keyWord");
				}
				list_keyWord=qaService.getSearchService().search(arrKeyWord, occurs, searchField, orderField, orderFieldType, reverse, isRelevancy, iBegin, iEnd);
			}
		}
		

		//有标签的就显示标签 最相近且时间最新的几个数据，如果没有标签则相似性检索，也按照时间排序
		if(list_keyWord!=null&&list_keyWord.size()!=0){
			DocumentUtils.transCollection(list_keyWord, list_rs);
		}else{
			DocumentUtils.transCollection(list, list_rs);
		}
		//排除自己，只删除一条
		for(int i=0;i<list_rs.size();i++){
			if(list_rs.get(i).getTitle().equalsIgnoreCase(title)){
				list_rs.remove(i);
				break;
			}
		}
		map_rs.put("keyWord", keyWord);
		map_rs.put("list", list_rs);
		msg.setSig(true);
		msg.setMsg(map_rs);
		model.addAttribute("msg",msg);
				return "simSearch";
	}
	
	
}
