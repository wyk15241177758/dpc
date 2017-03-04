package com.jt.test.jsoup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

public class JsoupTest {
	public Element hasLoop(Element e) {
		Element loopElement = null;
		for (Element curElement : e.children()) {
			if (curElement.tagName().equals("qa_loop")) {
				return curElement;
			}
		}
		return loopElement;
	}

	public void getChanged(Element e){
		if(e.children().size()==0){
			return;
		}
		Elements loopTags=e.children();
		if(loopTags.size()==1&&loopTags.get(0).tagName().equals("qa_loop")){
			e.after("替换成功");
			e.remove();
		}else{
			for(int i=0;i<loopTags.size();i++){
				getChanged(loopTags.get(i));
			}
		}
	}
		
	public void getChanged2(Element e){
		Elements children=e.children();
		if(children==null||children.size()==0){
			if(e.tagName().equalsIgnoreCase("qa_loop")){
				e.after("替换成功");
				e.remove();
			}
		}else{
			for(int i=0;i<children.size();i++){
				if(i<children.size()){
					Element curElement=children.get(i);
					getChanged2(curElement);
				}else{
					return;
				}
			}
		}
	}

	//替换qa
	public void changeQa(Element element){
		String sNum=element.attr("num");
		String sStart=element.attr("startpos");
		int num=0;
		int start=0;
		try {
			num=Integer.parseInt(sNum);
		} catch (Exception e) {
			System.out.println("num转换失败，使用默认值0");
		}
		try {
			start=Integer.parseInt(sStart);
		} catch (Exception e) {
			System.out.println("startpos转换失败，使用默认值0");
		}
		
		for(int i=start;i<num;i++){
			element.after(element.html());
		}
		element.remove();
	}
	
	
	//获得最内侧的qa_loop
	public List<Element> getInnerQa(Element element){
		Elements qaLoops=element.getElementsByTag("qa_loop");
		List<Element> innerQa=new ArrayList<Element>();
		
		for(int i=0;i<qaLoops.size();i++){
			boolean flag=true;
			Element curElement=qaLoops.get(i);
			Elements elements=curElement.getAllElements();
			for(Element ee:elements){
				if(ee!=curElement){
					if(ee.tagName().equalsIgnoreCase("qa_loop")){
						flag=false;
						break;
					}
				}
			}
			if(flag){
				innerQa.add(curElement);
			}
		}
		return innerQa;
	}
	
	public void getChanged3(Element element){
		while(element.getElementsByTag("qa_loop").size()>0){
			List<Element> list=getInnerQa(element);
			for(Element curElement:list){
				changeQa(curElement);
			}
		}
	}
	
	
	
	@Test
	public void test() {
		File input = new File("D://test.html");
		Document doc = null;
		try {
			doc = Jsoup.parse(input, "UTF-8", "");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		getChanged3(doc);
		System.out.println(doc);
//		Elements qaLoops=doc.getElementsByTag("qa_loop");
//		List<Element> innerQa=new ArrayList<Element>();
//		
//		for(int i=0;i<qaLoops.size();i++){
//			boolean flag=true;
//			Element curElement=qaLoops.get(i);
//			Elements elements=curElement.getAllElements();
//			for(Element ee:elements){
//				if(ee!=curElement){
//					if(ee.tagName().equalsIgnoreCase("qa_loop")){
//						flag=false;
//						break;
//					}
//				}
//			}
//			if(flag){
//				innerQa.add(curElement);
//			}
//		}
//		
//		for(int i=0;i<innerQa.size();i++){
//			System.out.println("i=["+i+"] element=["+innerQa.get(i)+"]");
//		}
		
	}

	public static void main(String[] args) throws IOException {
		File input = new File("D://test.html");
		Document doc = Jsoup.parse(input, "UTF-8", "");
		Elements contents = doc.getElementsByTag("qa_loop");
		Element content = contents.get(0);
		Elements elements = content.children();
		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			System.out.println("i=[" + i + "] tag=[" + e.tagName() + "]");
		}
	}
}
