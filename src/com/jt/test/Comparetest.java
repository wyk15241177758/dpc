package com.jt.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jt.lucene.Article;

public class Comparetest {
	public static void main(String[] args) {
		List<Article> list1=new ArrayList<Article>();
		List<Article> list2=new ArrayList<Article>();
		Article art=new Article();
		art.setScore(8);
		art.setTitle("111");
		list1.add(art);
		art=new Article();
		art.setScore(2);
		art.setTitle("222");
		list2.add(art);
		art=new Article();
		art.setScore(8);
		art.setTitle("222");
		list2.add(art);
		Map<String,List<Article>> map=new LinkedHashMap<String,List<Article>>();
		map.put("111", list1);
		map.put("222", list2);
		
		List<Map.Entry<String, List<Article>>> list=new ArrayList<>(map.entrySet());
		
		Collections.sort(list, new Comparator<Map.Entry<String, List<Article>>>() {
			@Override
			public int compare(Entry<String, List<Article>> o1, Entry<String, List<Article>> o2) {
				float sum1=0;
				float sum2=0;
				for(Article article:o1.getValue()){
					sum1+=article.getScore();
				}
				for(Article article:o2.getValue()){
					sum2+=article.getScore();
				}
				System.out.println(o1.getKey()+" value="+sum1+"  "+o2.getKey()+" value="+sum2);
				return (int)(sum1-sum2);
			}
		});
		
		map.clear();
		for(Map.Entry<String, List<Article>> entry:list){
			map.put(entry.getKey(), entry.getValue());
		}
		
		for(Map.Entry<String, List<Article>> entry:map.entrySet()){
			System.out.println(entry.getKey()+" value="+entry.getValue());
		}
	}
}
