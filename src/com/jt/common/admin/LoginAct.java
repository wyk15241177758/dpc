package com.jt.common.admin;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jt.bean.gateway.PageMsg;
import com.jt.common.bean.User;
import com.jt.common.util.PwdUtil;

@Controller
public class LoginAct {

	@RequestMapping(value = { "/login.act" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String input(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		model.addAttribute("msg", "请输入用户名密码！");
		model.addAttribute("success", false);

		return "redirect:/gateway/login.html";
	}

	@RequestMapping(value = { "/login.act" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	public String login(String username, String pwd, HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		if (!"admin".equals(username)) {
			model.addAttribute("msg", "不存在该用户！");
			model.addAttribute("success", false);
			return "redirect:/gateway/login.html";
		}
		String syspwd = PwdUtil.getPwd();
		if (!syspwd.equals(pwd)) {
			model.addAttribute("msg", "密码错误！");
			model.addAttribute("success", false);
			return "redirect:/gateway/login.html";
		}

		User user = new User();
		user.setUserName("admin");
		request.getSession().setAttribute("crawlerUser", user);

		return "redirect:/gateway/task.html";

	}

	@RequestMapping(value = { "/logout.act" })
	public String logout(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		request.getSession().removeAttribute("crawlerUser");
		;
		return "redirect:/gateway/login.html";
	}

	@RequestMapping(value = { "/repwd.act" })
	public String restartpwd(String oldpwd, String newpwd, String anewpwd, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String syspwd = PwdUtil.getPwd();
		if (!syspwd.equals(oldpwd)) {
			model.addAttribute("msg", "原始密码错误！");
			model.addAttribute("success", false);
			return "redirect:/gateway/repwd.html";
		}
		if (StringUtils.isBlank(newpwd) || StringUtils.isBlank(anewpwd)) {
			model.addAttribute("msg", "修改密码为空！");
			model.addAttribute("success", false);
			return "redirect:/gateway/repwd.html";
		}
		if (!newpwd.equals(anewpwd)) {
			model.addAttribute("msg", "两次密码不一致！");
			model.addAttribute("success", false);
			return "redirect:/gateway/repwd.html";
		}

		PwdUtil.createPwd(newpwd);

		model.addAttribute("msg", "重新登录！");
		model.addAttribute("success", true);
		return "redirect:/gateway/login.html";
	}

	@RequestMapping(value = { "/vrepwd.act" })
	public String vrestartpwd(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		model.addAttribute("msg", "请输入原密码！");
		model.addAttribute("success", false);

		return "redirect:/gateway/repwd.html";
	}

	@RequestMapping(value = { "/loginStatus.act" })
	public void loginStatus(HttpServletRequest request, HttpServletResponse response) {
		PageMsg msg;
		Gson gson= new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		response.setCharacterEncoding("utf-8");
		msg=new PageMsg();
		PrintWriter pw=null;
		try {
			pw=response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		
		
		User user = (User) request.getSession().getAttribute("crawlerUser");

		if (user == null) {
			msg.setSig(false);
		}else{
			msg.setSig(true);
		}
		pw.print(gson.toJson(msg));
	}

}
