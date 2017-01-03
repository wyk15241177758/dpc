package com.jt.searchHis.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
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
import com.jt.bean.gateway.PageMsg;
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
				searchHis = new SearchHis(null, searchContent, searchTimes, date, null);
				searchHisService.addSearchHis(searchHis);
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
				searchHisService.deleteSearchHis(searchHis);
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
		if (searchHisId != 0 && searchHis != null) {
			if (searchContent != null && searchContent.length() > 0 ) {
				try {
					Date date = new Date();
					searchHis.setSearchContent(searchContent);
					searchHis.setSearchTimes(searchTimes);
					searchHis.setUpdateTime(date);
					searchHisService.updateSearchHis(searchHis);
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
		String sPageSize=request.getParameter("pageSize");
		String sPageIndex=request.getParameter("pageIndex");
		
		int pageSize=0;
		int pageIndex=0;
		try {
			pageSize=Integer.parseInt(sPageSize);
			pageIndex=Integer.parseInt(sPageIndex);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			msg.setMsg("获得检索历史失败,传入的参数错误，错误信息为:[" + e.getMessage() + "]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
		List<SearchHis> list=searchHisService.queryByPage(pageIndex, pageSize);
		pw.print(gson.toJson(list));
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
