package com.jt.test;

import java.util.HashMap;
import java.util.Map;

import com.jt.common.util.EncryptUtils;
import com.jt.searchHis.bean.SearchHis;

public class MemoryCost {
	public static void main(String[] args) {
		 	System.gc();
	        long total = Runtime.getRuntime().totalMemory(); // byte
	        long m1 = Runtime.getRuntime().freeMemory();
	        System.out.println("before:" + ((float)(total - m1))/1024/1024);
	         
	        Map<Object,Object> map = new HashMap<Object,Object>();
	        for(int i=0; i < 1000000; i++){
	            map.put("fdsafdsatredsf"+i, new SearchHis(null, "请问政府我要创业怎么办呢请问一下哦","", 10, null, null));
	        }
	        long total1 = Runtime.getRuntime().totalMemory();
	        long m2 = Runtime.getRuntime().freeMemory();
	        System.out.println("after:" + ((float)(total - m2))/1024/1024);
//	        System.out.println(map.toString());
	}
}
