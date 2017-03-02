package com.jt.test.jsoup;

import java.io.File;
import java.io.IOException;

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
		

	@Test
	public void test() {
		File input = new File("D://test.html");
		Document doc = null;
		try {
			doc = Jsoup.parse(input, "UTF-8", "");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		getChanged(doc);
		System.out.println(doc.getElementsByTag("body").get(0));
	}

	// for(int i=0;i<loops.size();i++){
	// System.out.println("i=["+i+"] \nloop=["+loops.get(i)+"]");
	// }
	// Element e=getInnerLoop(doc);
	// System.out.println(e.html());

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
