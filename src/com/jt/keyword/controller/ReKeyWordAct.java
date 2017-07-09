package com.jt.keyword.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.keyword.bean.KeyWord;
import com.jt.keyword.bean.ReKeyWord;
import com.jt.keyword.service.KeyWordService;
import com.jt.keyword.service.ReKeyWordService;


@Controller
@RequestMapping("/rekeyWord")
public class ReKeyWordAct {
	
    public  static  int  pageSize=10;
	private ReKeyWordService service;
	private KeyWordService keyservice;

	@RequestMapping("/queryReKeyWord.do")
    public void  queryReKeyWord(Integer id,HttpServletRequest request,
			HttpServletResponse response) throws JSONException{
		ReKeyWord reKeyWord = service.queryById(id);
	    JSONObject json = new JSONObject();
	   Map<Integer,String>  relations=new HashMap<Integer,String>();
	   json.put("relations", 0);
        if(reKeyWord!=null&&reKeyWord.getReKeywords()!=null&&reKeyWord.getReKeywords().size()>0){
        	for (int i = 0; i < reKeyWord.getReKeywords().size(); i++) {
        		relations.put(reKeyWord.getReKeywords().get(i).id, 
        				reKeyWord.getReKeywords().get(i).getWordvalue());
			}
     	   json.put("relations",relations);
        }
        List<KeyWord> keywords = keyservice.queryAllFirst();
 	   Map<Integer,String>  allrelations=new HashMap<Integer,String>();
       if(keywords!=null){
    	   for (int i = 0; i < keywords.size(); i++) {
    		   allrelations.put(keywords.get(i).id, keywords.get(i).getWordvalue());
		}
       }
 	   json.put("allrelations",allrelations);
		renderJson(response, json.toString());
	
    }
	@RequestMapping("/updateOrSave.do")
	public  void  updateOrSaveRelation(Integer id,String ids,HttpServletRequest request,
			HttpServletResponse response) throws JSONException{
	    JSONObject json = new JSONObject();
		json.put("success", true);

		if(StringUtils.isNotBlank(ids)){
			try{
			ReKeyWord keyWord=new ReKeyWord();
			keyWord.setKid(id);
			keyWord.setKids(ids);
			service.updateOrSave(keyWord);
			}catch(Exception e){
				json.put("success", false);

			}
		}

	}
   
	
	
	@RequestMapping("/isExist.do")
	public   void   isExist(String wordvalue,HttpServletRequest request,
			HttpServletResponse response)
		throws JSONException{
	    JSONObject json = new JSONObject();
		renderJson(response, json.toString());

	}
	
	
	 public static void renderJson(HttpServletResponse response, String text)
	  {
	    render(response, "application/json;charset=UTF-8", text);
	  }
	
	 public static void render(HttpServletResponse response, String contentType, String text)
	  {
	    response.setContentType(contentType);
	    response.setHeader("Pragma", "No-cache");
	    response.setHeader("Cache-Control", "no-cache");
	    response.setDateHeader("Expires", 0L);
	    try {
	      response.getWriter().write(text);
	    } catch (IOException e) {
	    }
	  }


	public ReKeyWordService getService() {
		return service;
	}
	@Resource(name="reKeyWordService") 
	public void setService(ReKeyWordService service) {
		this.service = service;
	}
	
	public KeyWordService getKeyservice() {
		return keyservice;
	}
	@Resource(name="keyWordService") 
	public void setKeyservice(KeyWordService keyservice) {
		this.keyservice = keyservice;
	}
	public static void main(String[] args) throws JSONException {
		   JSONObject json = new JSONObject();
		   Map<Integer,String>  map=new HashMap<Integer,String>();
		   map.put(1, "2");
		   map.put(2, "2");
		   map.put(3, "2");
		   map.put(4, "2");
		   map.put(5, "2");
		   map.put(6, "2");
		   json.put("relation", map);
		   System.out.println(json.toString());

	}
	

}
