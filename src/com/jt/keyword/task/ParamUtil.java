package com.jt.keyword.task;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

/**
 * 全局变量
 * @author 邹许红
 */
public class ParamUtil {
	/**
	 * 热词统计列表
	 */
	public  static  ConcurrentHashMap<String,Integer> map=new ConcurrentHashMap<String, Integer>();
	/**
	 * 分析线程运行状态
	 */
	public  static  boolean   isrun=false;
	
    public  static  ConcurrentLinkedQueue<String>   titleList=new ConcurrentLinkedQueue<String>();

	/**
	 * 分析线程数
	 */
    public  static   int   totalpools=10;
    

    /**
     * 细缆同步辅助类
     */
    public static CountDownLatch parselatch = new CountDownLatch(totalpools);
    /**
     * 
     * @param key
     */
    public  static  synchronized  void   parseMapBykey(String  key){
    	if(!map.containsKey(key)){
    		map.put(key, 1);
    	}else{
    		int num=map.get(key);
    		map.put(key, num+1);
    	}
    }
	
	public static void main(String[] args) {
		
	}

}
