package com.jt.nlp.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NlpUtil {
	/**
	 * 按照标点符号分成多个问题
	 * @return
	 */
	public static List<String> splitQuestion(String question){
		List list=new ArrayList<String>();
		question=question.replaceAll("[\\pP‘’“”]", "#");
		for(String str:question.split("#")){
			list.add(str);
		}
		return list;
	}
	public static void main(String[] args) {
		String str="我想自主创业，政府有什么政策？";
		System.out.println(splitQuestion(str));
	}
}
