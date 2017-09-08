package com.jt.test.exportTest;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

public class ExportSceneTest {
	private String path;
	public ExportSceneTest(){
		
	}
	public void writeXml(String path,Object exportObject,Element element){
//		if(element==null){
//			if(exportObject instanceof List){
//				for(Object obj:(List)exportObject){
//					String elementName=obj.getClass().getSimpleName()+"s";
//					element=new Element(elementName);
//					Element child = new Element("test");
//					Document doc = new Document(rootElement);
//					child.setText("测试");
//					
//					List<Object> list = new ArrayList<Object>();
//					list.add(child);
//					rootElement.setChildren(list);
//				}
//			}
//		}
//		
//		
//		for(Object obj:exportList){
//			String elementName=obj.getClass().getSimpleName()+"s";
//			Element rootElement = new Element(elementName);
//			Element child = new Element("test");
//			Document doc = new Document(rootElement);
//			child.setText("测试");
//			
//			List<Object> list = new ArrayList<Object>();
//			list.add(child);
//			rootElement.setChildren(list);
//		}
	}
	public static void main(String[] args) {
		ExportSceneTest test=new ExportSceneTest();
		List<Object> list=new ArrayList<Object>();
		list.add(test);
//		test.writeXml("", list);
	}
	
}
