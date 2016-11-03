package com.jt.test.spring;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
@Service
public class SpringTest {
	private String pro;
	
	public SpringTest(){
		System.out.println("#########################in construct");
	}
	
	public String getPro() {
		return pro;
	}


	public void setPro(String pro) {
		this.pro = pro;
	}


 	public void test(){
		System.out.println("#########################pro=["+pro+"]");
	}
	
}
