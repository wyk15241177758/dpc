package com.jt.gateway.util;

public class CMyString {
	public static String getStrNotNullor0(String str,String replace){
		if(str==null||str.length()==0){
			return replace;
		}else{
			return str;
		}
	}
}
