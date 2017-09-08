package com.jt.test.exportTest;

import java.io.File;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class XmlReadTest {
public static void main(String[] args) {
	long lasting = System.currentTimeMillis();
	try {
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(new File("C:\\Users\\Administrator\\Desktop\\temp\\Noname1.xml"));
		Element foo = doc.getRootElement();
		List allChildren = foo.getChildren();
		for (int i = 0; i < allChildren.size(); i++) {
			Element curElement=((Element) allChildren.get(i));
			for(Object element:curElement.getChildren()){
				System.out.println(((Element)element).getChild("SCENEWORD").getChild("ENTERWORDS").getText());
				System.out.println(((Element)element).getChild("SCENEWORD").getChild("OUTWORDS").getText());
				System.out.println(((Element)element).getChild("SCENEWORD").getChild("SJFL").getText());
				System.out.println(((Element)element).getChild("SCENEPAGE").getChild("PAGETITLE").getText());
				System.out.println(((Element)element).getChild("SCENEPAGE").getChild("PAGELINK").getText());
				System.out.println(((Element)element).getChild("SCENEPAGE").getChild("SJFL").getText());
				System.out.println(((Element)element).getChild("SCENEPAGE").getChild("HTML").getText());
			}
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
}
}
