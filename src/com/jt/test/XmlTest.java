package com.jt.test;

import java.io.*;
import java.util.*;
import org.jdom.*;
import org.jdom.input.*;

public class XmlTest {
	public static void main(String arge[]) {
		long lasting = System.currentTimeMillis();
		try {
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(new File("gateway.conf"));
			Element foo = doc.getRootElement();
			List allChildren = foo.getChildren();
			for (int i = 0; i < allChildren.size(); i++) {
				System.out.print("任务名称：" + ((Element) allChildren.get(i)).getChild("TASKNAME").getText());
				System.out.print("path：" + ((Element) allChildren.get(i)).getChild("INDEXPATH").getText());
				System.out.print("dbname：" + ((Element) allChildren.get(i)).getChild("DBNAME").getText());
				System.out.print("idname：" + ((Element) allChildren.get(i)).getChild("IDNAME").getText());
				Element fields=((Element) allChildren.get(i)).getChild("FIELDS");
				System.out.println("------fields begin");
				for(Object e:fields.getChildren()){
					System.out.println(((Element)e).getChildText("FIELDNAME"));
					System.out.println(((Element)e).getChildText("FIELDTYPE"));
				}
				System.out.println("------fields end");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}