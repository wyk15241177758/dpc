package com.jt.scene.action;
/**
 * 场景映射词action
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jt.bean.gateway.PageMsg;
import com.jt.gateway.util.CMyString;
import com.jt.scene.bean.Scene;
import com.jt.scene.bean.ScenePage;
import com.jt.scene.bean.SceneWord;
import com.jt.scene.service.SceneService;
import com.jt.scene.service.SceneWordService;
import com.mysql.jdbc.log.Log;

@Controller
@RequestMapping("/scene")
public class SceneWordAction {
	private static final Logger LOG = LoggerFactory.getLogger(SceneWordAction.class);

	private SceneWordService sceneWordService;
	private SceneService sceneService;
	private PageMsg msg;
	private Gson gson;
	public SceneWordAction() {
		msg = new PageMsg();
		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	}
	public SceneWordService getSceneWordService() {
		return sceneWordService;
	}
	@Resource(name="sceneWordServiceImpl") 
	public void setSceneWordService(SceneWordService sceneWordService) {
		this.sceneWordService = sceneWordService;
	}


	public SceneService getSceneService() {
		return sceneService;
	}
	
	@Resource(name="sceneServiceImpl") 
	public void setSceneService(SceneService sceneService) {
		this.sceneService = sceneService;
	}
	@RequestMapping(value = "/addSceneWord.do")
	public void addSceneWord(HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		msg = new PageMsg();
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		SceneWord sceneWord = null;
		// 参数是否合法:同时包含场景名称、入口词、出口词即可
		String sSceneId = request.getParameter("sceneId");
		String sceneName = request.getParameter("sceneName");
		String enterWords = request.getParameter("enterWords");
		String outWords = request.getParameter("outWords");
		String sjfl=request.getParameter("sjfl");
		String pageTitles=CMyString.getStrNotNullor0(request.getParameter("pageTitles"), "");
		String pageLinks=CMyString.getStrNotNullor0(request.getParameter("pageLinks"),"");
		String pageIds=CMyString.getStrNotNullor0(request.getParameter("pageIds"),"");
		String pageSjfls=CMyString.getStrNotNullor0(request.getParameter("pageSjfls"),"");
		String[] pageTitleArr=pageTitles.split(";");
		String[] pageLinkArr=pageLinks.split(";");
		String[] sPageIdArr=pageIds.split(";");
		Integer[] pageIdArr=new Integer[sPageIdArr.length];
		
		//需要考虑关联分类为空的情况
		String[] pageSjflArr=new String[pageTitleArr.length];
		String[] temp=pageSjfls.split(";");
		for(int i=0;i<pageSjflArr.length;i++){
			if(temp.length<=i){
				pageSjflArr[i]=null;
			}else{
				//如参数为;的情况
				if(temp[i].length()==0){
					pageSjflArr[i]=null;					
				}else{
					pageSjflArr[i]=temp[i];
				}
			}
		}
		
		Integer sceneId = 0;
		try {
			sceneId = Integer.parseInt(CMyString.getStrNotNullor0(sSceneId, "0"));
		} catch (Exception e) {
			e.printStackTrace();
			msg.setMsg("添加场景映射词失败,错误信息为[转换sceneId类型失败]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
		if (sceneName != null && sceneName.length() > 0 && enterWords != null && enterWords.length() > 0
				&& outWords != null && outWords.length() > 0) {
			try {
				//对应的场景是否存在，不存在就报错
				Scene scene = sceneService.getSceneById(sceneId);
				if (sceneId != 0 && scene != null) {
					try {
						Date date = new Date();
						sceneWord = new SceneWord(null,sceneId ,sceneName, enterWords, outWords, date,date, sjfl,null);
						sceneWordService.addSceneWord(sceneWord);
						//是否存在预设页面，且预设页面的title和link数量一致
						if(pageTitles.length()>0&&pageLinks.length()>0&&pageIds.length()>0
								&&(pageTitleArr.length==pageLinkArr.length
								&&(pageTitleArr.length==pageIdArr.length)
								&&(pageTitleArr.length==pageSjflArr.length)
								)){
							List<ScenePage> scenePageList=new ArrayList<ScenePage>();
							for(int i=0;i<pageTitleArr.length;i++){
								try {
									pageIdArr[i]=Integer.parseInt(sPageIdArr[i]);
								} catch (Exception e) {
									pageIdArr[i]=0;
								}
								if(pageSjflArr[i]!=null){
									pageSjflArr[i]=pageSjflArr[i].replace(",", ";");
								}
								ScenePage curScenePage=new ScenePage(pageIdArr[i],sceneWord.getSceneWordId(),pageTitleArr[i],pageLinkArr[i],
										pageSjflArr[i],date,date);
								scenePageList.add(curScenePage);
							}
							sceneWord.setScenePageList(scenePageList);
							//需要先保存sceneWord获取ID
							sceneWordService.updateSceneWord(sceneWord);;
						}
						msg.setSig(true);
						msg.setMsg("新增场景映射词[" + sceneWord.getEnterWords() + "]成功");
						pw.print(gson.toJson(msg));
						return;
					} catch (Exception e) {
						e.printStackTrace();
						msg.setMsg("新增场景映射词[" + sceneWord.getSceneWordId() + "]失败，错误信息为:["+e.getMessage()+"]");
						pw.print(gson.toJson(msg));
						return;
					}
				} else {
					msg.setMsg("添加场景映射词到场景id=[" + sceneId + "]失败,错误信息为[未获得场景ID或不存在指定的场景]");
					msg.setSig(false);
					pw.print(gson.toJson(msg));
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
				msg.setMsg("添加场景映射词到场景id=[" + sceneId + "]失败，错误信息为:[" + e.getMessage() + "]");
				msg.setSig(false);
				pw.print(gson.toJson(msg));
				return;
			}
		} else {
			msg.setMsg("添加场景映射词到场景id=[" + sceneId + "]失败，错误信息为:[参数错误]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
	}

	// 删除场景映射词
	@RequestMapping(value = "delSceneWord.do")
	public void delSceneWord(HttpServletRequest request, HttpServletResponse response) {
		msg = new PageMsg();
		response.setCharacterEncoding("utf-8");
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}

		Integer sceneWordId = 0;
		try {
			sceneWordId = Integer.parseInt(CMyString.getStrNotNullor0(request.getParameter("sceneWordId"), "0"));
		} catch (Exception e) {
			e.printStackTrace();
			msg.setMsg("删除场景映射词id=[" + sceneWordId + "]失败,错误信息为[转换sceneWordId类型失败]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
		SceneWord sceneWord = sceneWordService.getSceneWordById(sceneWordId);
		if (sceneWordId != 0 && sceneWord != null) {
			try {
				// 删除sceneWord信息
				sceneWordService.deleteSceneWord(sceneWord);
			} catch (Exception e) {
				msg.setMsg("删除场景映射词id=[" + sceneWord.getSceneWordId() + "]失败，错误信息:[" + e.getMessage() + "]");
				pw.print(gson.toJson(msg));
				return;
			}
			msg.setSig(true);
			msg.setMsg("删除场景映射词id=[" + sceneWord.getSceneWordId() + "]成功");
			pw.print(gson.toJson(msg));
			return;
		} else {
			msg.setMsg("删除场景映射词id=[" + sceneWordId + "]失败,错误信息为[未获得场景ID或不存在指定的场景]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
	}

	// 修改场景映射词
	@RequestMapping(value = "updateSceneWord.do")
	public void updateSceneWord(HttpServletRequest request, HttpServletResponse response) {
		msg = new PageMsg();
		response.setCharacterEncoding("utf-8");
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}

		SceneWord sceneWord = null;
		// 参数是否合法:同时包含入口词、出口词即可。不允许修改所属场景
		String enterWords = request.getParameter("enterWords");
		String outWords = request.getParameter("outWords");
		String sjfl=request.getParameter("sjfl");
		String pageTitles=CMyString.getStrNotNullor0(request.getParameter("pageTitles"), "");
		String pageLinks=CMyString.getStrNotNullor0(request.getParameter("pageLinks"),"");
		String pageIds=CMyString.getStrNotNullor0(request.getParameter("pageIds"),"");
		//需要考虑关联分类为空的情况
		String pageSjfls=CMyString.getStrNotNullor0(request.getParameter("pageSjfls"),"");
		String[] pageTitleArr=pageTitles.split(";");
		String[] pageLinkArr=pageLinks.split(";");
		String[] sPageIdArr=pageIds.split(";");
		Integer[] pageIdArr=new Integer[sPageIdArr.length];
		Integer sceneId = 0;
		Integer sceneWordId = 0;
		//需要考虑关联分类为空的情况
		String[] pageSjflArr=new String[pageTitleArr.length];
		String[] temp=pageSjfls.split(";");
		for(int i=0;i<pageSjflArr.length;i++){
			if(temp.length<=i){
				pageSjflArr[i]=null;
			}else{
				//如参数为;的情况
				if(temp[i].length()==0){
					pageSjflArr[i]=null;					
				}else{
					pageSjflArr[i]=temp[i];
				}
			}
		}
		
		
		try {
			sceneId = Integer.parseInt(CMyString.getStrNotNullor0(request.getParameter("sceneId"), "0"));
			sceneWordId = Integer.parseInt(CMyString.getStrNotNullor0(request.getParameter("sceneWordId"), "0"));
		} catch (Exception e) {
			e.printStackTrace();
			msg.setMsg("修改场景映射词失败,错误信息为[转换sceneId/sceneWordId类型失败]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
		
		if (enterWords != null && enterWords.length() > 0
				&& outWords != null && outWords.length() > 0) {
			try {
				//对应的场景是否存在，不存在就报错
				Scene scene = sceneService.getSceneById(sceneId);
				sceneWord = sceneWordService.getSceneWordById(sceneWordId);
				if (sceneId != 0 && scene != null&&sceneWordId!=0&&sceneWord!=null) {
					try {
						Date date = new Date();
						sceneWord.setEnterWords(enterWords);
						sceneWord.setOutWords(outWords);
						sceneWord.setUpdateTime(new Date());
						sceneWord.setSjfl(sjfl);
						
						//是否存在预设页面，且预设页面的title和link数量一致
						if(pageTitles.length()>0&&pageLinks.length()>0&&pageIds.length()>0
								&&(pageTitleArr.length==pageLinkArr.length
								&&(pageTitleArr.length==pageIdArr.length)
								&&(pageTitleArr.length==pageSjflArr.length)
										)){
							List<ScenePage> scenePageList=new ArrayList<ScenePage>();
							for(int i=0;i<pageTitleArr.length;i++){
								try {
									pageIdArr[i]=Integer.parseInt(sPageIdArr[i]);
								} catch (Exception e) {
									pageIdArr[i]=0;
								}
								if(pageSjflArr[i]!=null){
									pageSjflArr[i]=pageSjflArr[i].replace(",", ";");
								}
								ScenePage curScenePage= sceneWordService.getScenePageService().getScenePageById(pageIdArr[i]);
								if(curScenePage==null){
									curScenePage = new ScenePage(pageIdArr[i],sceneWord.getSceneWordId(),
											pageTitleArr[i],pageLinkArr[i],pageSjflArr[i],date,date);
								}else{
									curScenePage.setPageTitle(pageTitleArr[i]);
									curScenePage.setPageLink(pageLinkArr[i]);
									curScenePage.setSjfl(pageSjflArr[i]);
									curScenePage.setUpdateTime(date);
								}
								scenePageList.add(curScenePage);
							}
							sceneWord.setScenePageList(scenePageList);
						}
						
						
						
						sceneWordService.updateSceneWord(sceneWord);
					} catch (Exception e) {
						e.printStackTrace();
						msg.setMsg("修改场景映射词[" + sceneWord.getSceneWordId() + "]失败，错误信息为:["+e.getMessage()+"]");
						pw.print(gson.toJson(msg));
						return;
					}
					msg.setSig(true);
					msg.setMsg("修改场景映射词[" + sceneWord.getEnterWords() + "]成功");
					pw.print(gson.toJson(msg));
					return;
				} else {
					msg.setMsg("修改场景映射词Id=["+sceneWordId+"] sceneId=["+sceneId+"]失败,错误信息为[未获得关联的场景或未获得指定的场景映射词]");
					msg.setSig(false);
					pw.print(gson.toJson(msg));
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
				msg.setMsg("修改场景映射词Id=["+sceneWordId+"] sceneId=["+sceneId+"]失败,错误信息为["+e.getMessage()+"]");
				msg.setSig(false);
				pw.print(gson.toJson(msg));
				return;
			}
		} else {
			msg.setMsg("修改场景映射词id=[" + sceneWordId + "]失败，错误信息为:[参数错误]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
	}

	@RequestMapping("/saveOrUpdateSceneWord.do")
	public void saveOrUpdateSceneWord(HttpServletRequest request, HttpServletResponse response) {
		String sceneWordId=request.getParameter("sceneWordId");
		//新增模式
		if(sceneWordId==null||sceneWordId.length()==0||"0".equals(sceneWordId)){
			addSceneWord(request, response);
		}else{
			//修改模式
			updateSceneWord(request, response);
		}
	}
	
	//可以传入sceneId，则查询关联的场景词
	@RequestMapping("/listSceneWords.do")
	public void listSceneWords(HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		msg = new PageMsg();
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		
		List<SceneWord> list=null;
		Integer sceneId = 0;
		try {
			sceneId = Integer.parseInt(CMyString.getStrNotNullor0(request.getParameter("sceneId"), "0"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(sceneId!=0){
			list = sceneWordService.getWordsBySceneId(sceneId);
		}else{
			list = sceneWordService.getAllSceneWords();
		}
		pw.print(gson.toJson(list));
	}
	
	@RequestMapping("/getSceneWord.do")
	public void getSceneWord(HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		msg = new PageMsg();
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		SceneWord sceneWord = null;
		Integer sceneWordId = 0;
		try {
			sceneWordId = Integer.parseInt(CMyString.getStrNotNullor0(request.getParameter("sceneWordId"), "0"));
		} catch (Exception e) {
			e.printStackTrace();
			msg.setMsg("获得场景映射词id=[" + sceneWordId + "]失败,错误信息为[转换sceneWordId类型失败]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
		sceneWord = sceneWordService.getSceneWordById(sceneWordId);
		if (sceneWordId != 0 && sceneWord != null) {
			try {
				// 该场景不存在则不能修改
				sceneWord = sceneWordService.getSceneWordById(sceneWordId);
				if (sceneWord == null) {
					msg.setMsg("获得场景映射词[" + sceneWordId + "]失败，错误信息为:[该场景映射词不存在]");
					msg.setSig(false);
					pw.print(gson.toJson(msg));
					return;
				}
				msg.setMsg(sceneWord);
				msg.setSig(true);
				pw.print(gson.toJson(msg));
				return;
			} catch (Exception e) {
				e.printStackTrace();
				msg.setMsg("获得场景映射词[" + sceneWordId + "]失败，错误信息为:[" + e.getMessage() + "]");
				msg.setSig(false);
				pw.print(gson.toJson(msg));
				return;
			}
		} else {
			msg.setMsg("获得场景映射词[" + sceneWordId + "]失败,错误信息为[未获得场景映射词ID或不存在指定的场景映射词]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
	}
	
	@RequestMapping("/getQaSjfl.do")
	public void getQaSjfl(HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		msg = new PageMsg();
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		String sjfl=sceneWordService.getQaSjfl();
		System.out.println("sjfl=["+sjfl+"]");
		if(sjfl!=null&&sjfl.length()>0){
			msg.setMsg(sjfl.split(","));
			msg.setSig(true);
		}else{
			LOG.error("关联分类列表为空，请检查jdbc.properties是否有qa.sjfl属性");
			msg.setMsg("关联分类列表为空，请检查jdbc.properties是否有qa.sjfl属性");
			msg.setSig(false);
		}
		pw.print(gson.toJson(msg));
		return;
	}
}
