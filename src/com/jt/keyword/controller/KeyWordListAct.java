package com.jt.keyword.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.jt.keyword.bean.KeyWord;
import com.jt.keyword.bean.QueryResutList;
import com.jt.keyword.bean.ReKeyWord;
import com.jt.keyword.service.KeyWordService;
import com.jt.keyword.service.ReKeyWordService;
import com.jt.keyword.service.RekeyWordListService;
import com.jt.keyword.task.KeyWordTaskUtil;
import com.jt.keyword.task.ParamUtil;
import com.jt.keyword.util.ResponseUtils;
@Controller
@RequestMapping("/search")
public class KeyWordListAct {
	 public  static  int  pageSize=10;
     private KeyWordService service;
     private  RekeyWordListService  rekeyWordListService;
     private ReKeyWordService  reKeyWordService;
	@RequestMapping("/list.do")
	public  void  queryList(String word,HttpServletRequest request, HttpServletResponse response) throws JSONException{
	
		try{JSONObject json = new JSONObject();
		json.put("ok", false);
		if(ParamUtil.keywords!=null&&ParamUtil.keywords.size()>0){
	}else{
		ParamUtil.keywords=service.queryAll();
	}
	//	json.put("keywords", ParamUtil.keywords);

	String keyword=KeyWordTaskUtil.parse(word, ParamUtil.keywords);
	if(StringUtils.isBlank(keyword)){
		ResponseUtils.renderJson(response, json.toString());
		return;
	}
	json.put("keyword", keyword);
	ReKeyWord reKeyWord = reKeyWordService.queryByName(keyword);
	 List<String> reKeyWords=new ArrayList<String>();
    if(reKeyWord!=null)
	reKeyWords=KeyWordTaskUtil.parseAr(reKeyWord.reKeywords);
    if(reKeyWords==null||reKeyWords.size()==0){
    	reKeyWords=new ArrayList<String>();
    	List<KeyWord> keywordFirst = service.queryAllFirst();
    	for (int i = 0; i < keywordFirst.size()&&i<10; i++) {
    		reKeyWords.add(keywordFirst.get(i).getWordvalue());
		}
    }
	json.put("reKeyWords", reKeyWords);
	QueryResutList queryResutList = rekeyWordListService.queryByKeyWord(keyword, 1, pageSize);
	Gson gson=new Gson();
	json.put("queryResutList", gson.toJson(queryResutList));
	json.put("ok", true);
	ResponseUtils.renderJson(response, json.toString()); }catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}   
	}
	@RequestMapping("/ajaxlist.do")
	public  void  ajaxQueryList(String keyword,String fl,String source,Integer pageNum,HttpServletRequest request, HttpServletResponse response) throws JSONException{
		JSONObject json = new JSONObject();
		json.put("ok", false);
		if(ParamUtil.keywords!=null&&ParamUtil.keywords.size()>0){
	}else{
		ParamUtil.keywords=service.queryAll();
	}
	//json.put("keywords", ParamUtil.keywords);
	json.put("keyword", keyword);
	ReKeyWord reKeyWord = reKeyWordService.queryByName(keyword);
	 List<String> reKeyWords=new ArrayList<String>();
    if(reKeyWord!=null)
	reKeyWords=KeyWordTaskUtil.parseAr(reKeyWord.reKeywords);
    if(reKeyWords==null||reKeyWords.size()==0){
    	reKeyWords=new ArrayList<String>();
    	List<KeyWord> keywordFirst = service.queryAllFirst();
    	for (int i = 0; i < keywordFirst.size()&&i<10; i++) {
    		reKeyWords.add(keywordFirst.get(i).getWordvalue());
		}
    }
	json.put("reKeyWords", reKeyWords);
	QueryResutList queryResutList = new  QueryResutList();
	if(StringUtils.isNotBlank(fl)&&StringUtils.isNotBlank(source)){
		queryResutList = rekeyWordListService.queryByKSF(keyword, source, fl, pageNum, pageSize);
	}
	if(StringUtils.isNotBlank(fl)&&!StringUtils.isNotBlank(source)){
		queryResutList = rekeyWordListService.queryByKF(keyword, fl, pageNum, pageSize);
	}
	if(!StringUtils.isNotBlank(fl)&&StringUtils.isNotBlank(source)){
		queryResutList = rekeyWordListService.queryByKS(keyword, source, pageNum, pageSize);
	}
	if(!StringUtils.isNotBlank(fl)&&!StringUtils.isNotBlank(source)){
		queryResutList = rekeyWordListService.queryByKeyWord(keyword, pageNum, pageSize);
	}
	Gson gson=new Gson();
	json.put("queryResutList", gson.toJson(queryResutList));
	json.put("ok", true);
	ResponseUtils.renderJson(response, json.toString());   
		
	}
	
	
	public KeyWordService getService() {
		return service;
	}
	@Resource(name="keyWordService") 
	public void setService(KeyWordService service) {
		this.service = service;
	}
	public RekeyWordListService getRekeyWordListService() {
		return rekeyWordListService;
	}
	@Resource(name="reKeyWordListService") 
	public void setRekeyWordListService(RekeyWordListService rekeyWordListService) {
		this.rekeyWordListService = rekeyWordListService;
	}
	public ReKeyWordService getReKeyWordService() {
		return reKeyWordService;
	}
	@Resource(name="reKeyWordService") 
	public void setReKeyWordService(ReKeyWordService reKeyWordService) {
		this.reKeyWordService = reKeyWordService;
	}

}
