package com.jt.searchHis.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jt.base.page.Param;
import com.jt.bean.gateway.PageMsg;
import com.jt.common.util.EncryptUtils;
import com.jt.gateway.util.CMyString;
import com.jt.searchHis.bean.SearchHis;
import com.jt.searchHis.service.SearchHisService;

@Controller
@RequestMapping("/searchHis")
public class SearchHisAction {
	private Log log = LogFactory.getLog(this.getClass());
	private SearchHisService searchHisService;
	private PageMsg msg;
	private Gson gson;

	
	public SearchHisService getSearchHisService() {
		return searchHisService;
	}
	
	@Resource(name="searchHisServiceImpl") 
	public void setSearchHisService(SearchHisService searchHisService) {
		this.searchHisService = searchHisService;
	}
	public SearchHisAction() {
		msg = new PageMsg();
		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	}

	@RequestMapping(value = "/addSearchHis.do")
	public void addSearchHis(HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		msg = new PageMsg();
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		SearchHis searchHis = null;
		// 参数是否合法:包含检索内容，检索次数默认为0
		String searchContent = request.getParameter("searchContent");
		String sSearchTimes = request.getParameter("searchTimes");
		int searchTimes=0;
		try {
			searchTimes=Integer.parseInt(sSearchTimes);
		} catch (NumberFormatException e1) {
			searchTimes=0;
			log.info("sSearchTimes=["+sSearchTimes+"] searchTimes=0");
		}
		if (searchContent != null && searchContent.length() > 0 ) {
			try {
				Date date = new Date();
				searchHis = new SearchHis(null, searchContent,EncryptUtils.encodeMD5(searchContent), searchTimes,  date, date);
				searchHisService.addSearchHis(searchHis,true);
				msg.setMsg("新增检索历史[" + searchContent + "]成功");
				msg.setSig(true);
				pw.print(gson.toJson(msg));
				return;
			} catch (Exception e) {
				e.printStackTrace();
				msg.setMsg("新增检索历史[" + searchContent + "]失败，错误信息为:[" + e.getMessage() + "]");
				msg.setSig(false);
				pw.print(gson.toJson(msg));
				return;
			}
		} else {
			msg.setMsg("新增检索历史失败，错误信息为:[参数错误]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}

	}

	// 删除
	@RequestMapping(value = "delSearchHis.do")
	public void delSearchHis(HttpServletRequest request, HttpServletResponse response) {
		msg = new PageMsg();
		response.setCharacterEncoding("utf-8");
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}

		Long searchHisId = 0l;
		try {
			searchHisId = Long.parseLong(CMyString.getStrNotNullor0(request.getParameter("searchHisId"), "0"));
		} catch (Exception e) {
			e.printStackTrace();
			msg.setMsg("删除检索历史id=[" + searchHisId + "]失败,错误信息为[转换searchHisId类型失败]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
		SearchHis searchHis = searchHisService.getSearchHisById(searchHisId);
		if (searchHisId != 0 && searchHis != null) {
			try {
				searchHisService.deleteSearchHis(searchHis,true);
			} catch (Exception e) {
				msg.setMsg("删除[" + searchHis.getSearchContent() + "]失败，错误信息:[" + e.getMessage() + "]");
				pw.print(gson.toJson(msg));
				return;
			}
			msg.setSig(true);
			msg.setMsg("删除[" + searchHis.getSearchContent() + "]成功");
			pw.print(gson.toJson(msg));
			return;
		} else {
			msg.setMsg("删除id=[" + searchHisId + "]失败,错误信息为[未获得ID或不存在指定的检索历史]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
	}

	// 批量删除
	@RequestMapping(value = "delSearchHises.do")
	public void delSearchHises(HttpServletRequest request, HttpServletResponse response) {
		msg = new PageMsg();
		response.setCharacterEncoding("utf-8");
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}

		String searchHisIds=request.getParameter("searchHisIds");
		if(searchHisIds==null||searchHisIds.length()==0){
			msg.setMsg("参数searchHisIds为空");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}else{
			String[] ids=searchHisIds.split(",");
			String sMsg="";
			String successMsg="";
			String failMsg="";
			for(String id:ids){
				Long searchHisId = 0l;
				try {
					searchHisId = Long.parseLong(id);
				} catch (Exception e) {
					e.printStackTrace();
					failMsg+="删除检索历史id=[" + id + "]失败,错误信息为[转换Long类型失败]<br>";
					continue;
				}
				SearchHis searchHis = searchHisService.getSearchHisById(searchHisId);
				if (searchHisId != 0 && searchHis != null) {
					try {
						searchHisService.deleteSearchHis(searchHis,true);
					} catch (Exception e) {
						failMsg+="删除[" + searchHis.getSearchContent() + "]失败，错误信息:[" + e.getMessage() + "]<br>";
						continue;
					}
					if(successMsg.length()==0){
						successMsg=searchHis.getSearchContent();
					}else{
						successMsg+=","+searchHis.getSearchContent();
					}
				} else {
					failMsg+="删除id=[" + searchHisId + "]失败,错误信息为[未获得ID或不存在指定的检索历史]<br>";
				}
			}
			sMsg="删除记录成功:"+successMsg+"<br>------<br>删除记录失败:"+failMsg;
			msg.setSig(true);
			msg.setMsg(sMsg);
			pw.print(gson.toJson(msg));
			return;
		}
	}
	
	
	@RequestMapping(value = "updateSearchHis.do")
	public void updateSearchHis(HttpServletRequest request, HttpServletResponse response) {
		msg = new PageMsg();
		response.setCharacterEncoding("utf-8");
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}

		SearchHis searchHis = null;
		// 参数是否合法:包含检索内容、检索ID，检索次数默认为0
		String searchContent = request.getParameter("searchContent");
		String sSearchTimes = request.getParameter("searchTimes");
		int searchTimes=0;
		Long searchHisId = 0l;
		try {
			searchHisId = Long.parseLong(CMyString.getStrNotNullor0(request.getParameter("searchHisId"), "0"));
		} catch (Exception e) {
			e.printStackTrace();
			msg.setMsg("修改检索历史id=[" + searchHisId + "]失败,错误信息为[转换searchHisId类型失败]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}				
		try {
			searchTimes=Integer.parseInt(sSearchTimes);
		} catch (NumberFormatException e1) {
			searchTimes=0;
			log.info("sSearchTimes=["+sSearchTimes+"] searchTimes=0");
		}
		searchHis = searchHisService.getSearchHisById(searchHisId);
		String oldQuestion=searchHis.getSearchContent();
		if (searchHisId != 0 && searchHis != null) {
			if (searchContent != null && searchContent.length() > 0 ) {
				try {
					Date date = new Date();
					searchHis.setSearchContent(searchContent);
					searchHis.setContentMd5(EncryptUtils.encodeMD5(searchContent));
					searchHis.setSearchTimes(searchTimes);
					searchHis.setUpdateTime(date);
					searchHisService.updateSearchHis(searchHis,oldQuestion,true);
					msg.setMsg("修改检索历史[" + searchContent + "]成功");
					msg.setSig(true);
					pw.print(gson.toJson(msg));
					return;
				} catch (Exception e) {
					e.printStackTrace();
					msg.setMsg("修改检索历史[" + searchContent + "]失败，错误信息为:[" + e.getMessage() + "]");
					msg.setSig(false);
					pw.print(gson.toJson(msg));
					return;
				}
			} else {
				msg.setMsg("修改检索历史id=[" + searchHisId + "]失败,错误信息为[检索内容为空]");
				msg.setSig(false);
				pw.print(gson.toJson(msg));
				return;
			}
		} else {
			msg.setMsg("修改检索历史id=[" + searchHisId + "]失败,错误信息为[未获得ID或不存在指定的检索历史]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
	}

	@RequestMapping("/saveOrUpdateSearchHis.do")
	public void saveOrUpdateSearchHis(HttpServletRequest request, HttpServletResponse response) {
		String searchHisId=request.getParameter("searchHisId");
		//新增模式
		if(searchHisId==null||searchHisId.length()==0||"0".equals(searchHisId)){
			addSearchHis(request, response);
		}else{
			//修改模式
			updateSearchHis(request, response);
		}
	}

	
	@RequestMapping("/getCount.do")
	public void getCount(HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		pw.print(searchHisService.getCount(null));
	}
	
	
	@RequestMapping("/listSearchHis.do")
	public void listSearchHis(HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		msg = new PageMsg();
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		String sPageSize=request.getParameter("length");
		String sPageIndex=request.getParameter("start");
		String sDraw=request.getParameter("draw");
		String sOrder=request.getParameter("order[0][column]");
		String sOrderDir=request.getParameter("order[0][dir]");
		String sSearch=request.getParameter("search[value]");
		String order=null;
		int pageSize=0;
		int pageIndex=0;
		Param param=null;
		List<Param> paramList=new ArrayList<Param>();

		try {
			pageSize=Integer.parseInt(sPageSize);
			pageIndex=Integer.parseInt(sPageIndex)/pageSize;
		} catch (NumberFormatException e) {
			log.info("pageSize和pageIndex参数错误，置为默认值10和0");
			pageSize=10;
			pageIndex=0;
		}
		//映射排序字段
		//第3列：搜索次数
		if(sOrder.equals("3")){
			order="searchTimes"+" "+sOrderDir;
		}else{
			//否则按照创建时间排序
			order="createTime "+sOrderDir;
		}
		//检索功能
		if(sSearch!=null&&sSearch.trim().length()>0){
			sSearch=sSearch.trim();
			param=new Param();
			param.setLike(true);
			param.setParamName("searchContent");
			param.setType(Types.VARCHAR);
			param.setValue("%"+sSearch+"%");
			paramList.add(param);
		}
		List<SearchHis> list=searchHisService.queryByPage(pageIndex, pageSize,paramList,order);
		String[][] arr=new String[list.size()][];
		for(int i=0;i<list.size();i++){
			SearchHis curHis=list.get(i);
			arr[i]=new String[5];
			arr[i][0]=curHis.getId()+"";
			arr[i][1]=curHis.getId()+"";
			arr[i][2]=curHis.getSearchContent()+"";
			arr[i][3]=curHis.getSearchTimes()+"";
			arr[i][4]=curHis.getCreateTime()+"";
		}
		HashMap map=new HashMap();
		map.put("recordsTotal", searchHisService.getCount(null));
		map.put("recordsFiltered", searchHisService.getCount(paramList));
		map.put("data", arr);
		map.put("draw",sDraw);
		pw.print(gson.toJson(map));
	}
	
	@RequestMapping("/getSearchHis.do")
	public void getSearchHis(HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		msg = new PageMsg();
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		SearchHis searchHis = null;
		Long searchHisId = 0l;
		try {
			searchHisId = Long.parseLong(CMyString.getStrNotNullor0(request.getParameter("searchHisId"), "0"));
		} catch (Exception e) {
			e.printStackTrace();
			msg.setMsg("获得检索历史id=[" + searchHisId + "]失败,错误信息为[转换searchHisId类型失败]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
		searchHis = searchHisService.getSearchHisById(searchHisId);
		if (searchHisId != 0 && searchHis != null) {
			try {
				msg.setMsg(searchHis);
				msg.setSig(true);
				pw.print(gson.toJson(msg));
				return;
			} catch (Exception e) {
				e.printStackTrace();
				msg.setMsg("获得检索历史[" + searchHisId + "]失败，错误信息为:[" + e.getMessage() + "]");
				msg.setSig(false);
				pw.print(gson.toJson(msg));
				return;
			}
		} else {
			msg.setMsg("获得检索历史[" + searchHisId + "]失败,错误信息为[未获得检索历史ID或不存在指定的检索历史]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
	}
}
