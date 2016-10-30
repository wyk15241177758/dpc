package com.jt.keyword.controller;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.keyword.service.KeyWordService;


@Controller
@RequestMapping("/keyWord")
public class KeyWordAct {
	

	private KeyWordService service;
	@RequestMapping("/test.do")
	public  void   test(){
		service.addKeyword(null, null);
	}
	
	

	public KeyWordService getService() {
		return service;
	}
	@Resource(name="keyWordService") 
	public void setService(KeyWordService service) {
		this.service = service;
	}

}
