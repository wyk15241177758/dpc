package com.jt.nlp.util;

import java.util.Set;

import org.apdplat.qa.parser.WordParser;

public class NlpUtil {
	/**
	 * 按照标点符号分成多个问题
	 * @return
	 */
	public static Set<String> splitQuestion(String question){
		System.out.println(WordParser.parse(question));
		return null;
	}
	public static void main(String[] args) {
		splitQuestion(",");
	}
}
