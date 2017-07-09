package com.jt.searchHis.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jt.bean.gateway.PageMsg;
import com.jt.searchHis.bean.SearchHis;
import com.jt.searchHis.service.SearchHisRtService;

@Controller
@RequestMapping("/searchHisRt")
public class SearchHisRtAction {
//	private Log log = LogFactory.getLog(this.getClass());
	private SearchHisRtService searchHisRtService;
	private PageMsg msg;
	private Gson gson;

	
	public SearchHisRtService getSearchHisRtService() {
		return searchHisRtService;
	}
	
	@Resource(name="searchHisRtServiceImpl") 
	public void setSearchHisRtService(SearchHisRtService searchHisRtService) {
		this.searchHisRtService = searchHisRtService;
	}

	public SearchHisRtAction() {
		msg = new PageMsg();
		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	}

	@RequestMapping(value = "/list.do")
	public void listSearchHisRt(HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		msg = new PageMsg();
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		
		Map<String ,SearchHis> map=searchHisRtService.tempList();
		msg.setMsg(map);
		msg.setSig(false);
		pw.print(gson.toJson(msg));
	}
	/**
	 * 将内存中数据同步到数据库
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/syncToDb.do")
	public void syncToDb(HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		msg = new PageMsg();
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		
		String rs=searchHisRtService.doExecute(null);
		msg.setMsg(rs);
		msg.setSig(true);
		pw.print(gson.toJson(msg));
	}
	
}
