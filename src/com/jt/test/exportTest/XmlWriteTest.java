package com.jt.test.exportTest;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class XmlWriteTest {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		String path = "D:\\test.xml";
		long lasting = System.currentTimeMillis();
		Element rootElement = new Element("SCENES");
		Element child = new Element("test");
		Document doc = new Document(rootElement);
		child.setText("测试");
		
		List<Object> list = new ArrayList<Object>();
		list.add(child);
		rootElement.setChildren(list);
		XMLOutputter XMLOut = new XMLOutputter();
		// 输出company_list.xml文件；
		XMLOut.output(doc, new FileOutputStream(path));
	}
}
