package com.jt.keyword.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.keyword.bean.KeyWord;
import com.jt.keyword.service.KeyWordService;


@Controller
@RequestMapping("/keyWord")
public class KeyWordAct {
	
    public  static  int  pageSize=10;
	private KeyWordService service;

	
    /**
     * 左侧一级菜单
     * @param word
     * @param request
     * @param response
     */
	@RequestMapping("/leftWord.do")
	public  void  leftWord(String word,HttpServletRequest request,
			HttpServletResponse response){
		String json = service.queryPage(word);
		System.out.println(json);
		renderJson(response, json);

	}
	/**
	 * 左侧单击事件
	 * @param pid
	 * @param request
	 * @param response
	 */
	@RequestMapping("/leftClikWord.do")
	public  void  leftClikWord(Integer pid,HttpServletRequest request,
			HttpServletResponse response){
		String json = service.queryPage(null, 1, pageSize, pid);
		renderJson(response, json);

	}
	/**
	 * 右侧分页查询
	 * @param word
	 * @param pid
	 * @param pageNum
	 * @param request
	 * @param response
	 */
	@RequestMapping("/rightWord.do")
	public  void  rightWord(String word,Integer pid,Integer pageNum,HttpServletRequest request,
			HttpServletResponse response){
		String json = service.queryPage(word, pageNum, pageSize, pid);
		renderJson(response, json);

	}
	/**
	 * 右侧分页查询
	 * @param word
	 * @param pid
	 * @param pageNum
	 * @param request
	 * @param response
	 */
	@RequestMapping("/rightPage.do")
	public  void  rightPage(String word,Integer pageNum,HttpServletRequest request,
			HttpServletResponse response){
		String json = service.queryPage(word, pageNum, pageSize);
		renderJson(response, json);

	}
	/**
	 * 右侧分页首次查询
	 * @param word
	 * @param pid
	 * @param pageNum
	 * @param request
	 * @param response
	 */
	@RequestMapping("/rightPageFirst.do")
	public  void  rightPage(HttpServletRequest request,
			HttpServletResponse response){
		String json = service.queryPage(null, 1, pageSize);
		renderJson(response, json);

	}
	@RequestMapping("/delWord.do")
	public  void   delWord(String ids,HttpServletRequest request,
			HttpServletResponse response) throws JSONException{
		JSONObject json = new JSONObject();
		
		service.delete(KeyWord.class, ids);
		json.put("success", true);
		renderJson(response, json.toString());

	}
	@RequestMapping("/add.do")
	public  void   add(String keyvalue, Integer pId,HttpServletRequest request,
			HttpServletResponse response) throws JSONException{
	   JSONObject json = new JSONObject();
		if(pId==-1)
		service.addKeyword(keyvalue, null);
		else
			service.addKeyword(keyvalue, pId);
	
		json.put("success", true);
		renderJson(response, json.toString());
	}
	@RequestMapping("/edit.do")
	public  void   edit(Integer id,HttpServletRequest request,
			HttpServletResponse response) throws JSONException{
		    JSONObject json = new JSONObject();
			KeyWord word = (KeyWord) service.queryById(KeyWord.class, id);
			if(word!=null){
			json.put("success", true);
			json.put("word", word.wordvalue);}else{
				json.put("success", false);
			}

			renderJson(response, json.toString());
	}
	@RequestMapping("/update.do")
	public  void   update(String wordvalue,Integer id,HttpServletRequest request,
			HttpServletResponse response)
		throws JSONException{
		    JSONObject json = new JSONObject();
			service.updateKeyword(wordvalue, id);
			json.put("success", true);
			

			renderJson(response, json.toString());
	}
	@RequestMapping("/isExist.do")
	public   void   isExist(String wordvalue,HttpServletRequest request,
			HttpServletResponse response)
		throws JSONException{
	    JSONObject json = new JSONObject();
		boolean exist = service.isExist(wordvalue);
		if(exist)
		json.put("isExist", 1);
		else
			json.put("isExist", 0);
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
	public KeyWordService getService() {
		return service;
	}
	@Resource(name="keyWordService") 
	public void setService(KeyWordService service) {
		this.service = service;
	}

}
