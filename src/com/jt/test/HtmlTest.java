package com.jt.test;

import org.springframework.web.util.HtmlUtils;

public class HtmlTest {
	public static void main(String[] args) {
		String str="<div>test</div>";
		System.out.println(HtmlUtils.htmlEscape(str));;
	}
}
