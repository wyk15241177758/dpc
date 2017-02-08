package com.jt.common.interceptor;
/**
 * 检查当前IP的访问次数，超过限制次数返回false
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jt.bean.gateway.PageMsg;
import com.jt.common.bean.Entity;

public class AccessControl  implements Filter {
	private static final Logger LOG = LoggerFactory.getLogger(AccessControl.class);

	
	private PageMsg msg;
	//间隔时间ms，10分钟
	private long interval=600000l;
	//同IP最大访问次数，1000次
	private int maxCount=1000;
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filter) throws IOException, ServletException {
		Gson gson= new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		
		
		HttpServletRequest  req=(HttpServletRequest) request;
		HttpServletResponse resp=(HttpServletResponse) response;
		ServletContext application=req.getSession().getServletContext();
		String ip=req.getRemoteAddr();
		boolean rs=true;
		long time=System.currentTimeMillis();
		
		resp.setCharacterEncoding("utf-8");
		msg=new PageMsg();
		msg.setSig(true);
		PrintWriter pw=null;
		try {
			pw=resp.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		
		
		if(application.getAttribute("db")==null){
			Entity entity=new Entity();
			entity.setDate(time);
			entity.getMap().put(ip,1);
			application.setAttribute("db",entity);
		}else{
			Entity entity=(Entity)application.getAttribute("db");
			if(time-entity.getDate()>interval){
				entity.getMap().clear();
				entity.setDate(time);
				entity.getMap().put(ip,1);
			}else{
				ConcurrentHashMap<String , Integer>map=entity.getMap();
				int count=entity.getMap().get(ip)==null?0:entity.getMap().get(ip);
				if(++count>=maxCount){
					rs=false;
				}else{
					entity.getMap().put(ip,count);
				}
			}
		}
		if(!rs){
			LOG.error("当前IP:["+ip+"]访问太频繁，禁止访问-----------------");
			msg.setSig(false);
			msg.setMsg("访问太频繁");
			pw.print(gson.toJson(msg));
			return;
		}else{
			filter.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		
		
	}

}
