package com.jt.common.interceptor;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jt.common.bean.User;

public class UserYorNfilter  implements Filter {
/**
 * 用户登陆判断
 */
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		
		HttpServletRequest  req=(HttpServletRequest) arg0;
		HttpServletResponse resp=(HttpServletResponse) arg1;
		String url=req.getRequestURI();
		
		User user = (User)  req.getSession().getAttribute("crawlerUser");
		
		if (user == null) {
			arg0.getRequestDispatcher("/gateway/login.html").forward(req,
					resp);
		}
		
		else{

			arg2.doFilter(arg0, arg1);

		}

	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		
		
	}

}
